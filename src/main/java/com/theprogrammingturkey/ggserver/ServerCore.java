package com.theprogrammingturkey.ggserver;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.theprogrammingturkey.ggserver.client.SocketClient;
import com.theprogrammingturkey.ggserver.services.ServiceManager;
import com.wedevol.xmpp.server.CcsClient;

public class ServerCore extends CcsClient
{
	public static boolean debug = false;
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss", Locale.US);

	private Thread socketThread;

	private static ServerCore instance;

	public ServerCore(String projectId, String apiKey)
	{
		super(projectId, apiKey, debug);
		instance = this;
		output(Level.Info, "Starting Firebase Connection...");
		if(connectToFirebase())
			output(Level.Info, "Firebase Connected!");
		output(Level.Info, "Starting Server...");
		socketThread = new Thread(new SocketManager());
		socketThread.start();
		output(Level.Info, "Server Started!");
		ServiceManager.startServices();
	}

	public boolean connectToFirebase()
	{
		try
		{
			super.connect();
		} catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void log(java.util.logging.Level level, String message)
	{
		output(Level.Info, message);
	}

	public static void output(Level level, String message)
	{
		if(level == Level.DeBug && !debug)
			return;

		if(message.contains("[TurkeyBot]"))
			System.out.println("[" + dateFormatter.format(new Date()) + "]" + message);
		else
			System.out.println("[" + dateFormatter.format(new Date()) + "][Pi Server] [" + level.getLevel() + "]: " + message);
	}

	public static void sendFCMMessage(String message)
	{
		instance.sendPacket(message);
	}

	public static enum Level
	{
		None(""), Info("Info"), Important("IMPORTANT"), Alert("Alert"), Warning("Warning"), DeBug("DeBug"), Error("Error"), Severe("SEVERE");

		private String level;

		private Level(String level)
		{
			this.level = level;
		}

		public String getLevel()
		{
			return this.level;
		}
	}

	private static class SocketManager implements Runnable
	{
		private boolean running = false;

		private ServerSocket server;

		public void run()
		{
			running = true;
			try
			{
				server = new ServerSocket(44949);
				try
				{
					while(running)
					{
						Socket connection = server.accept();
						SocketClient client = new SocketClient(connection, connection.getInetAddress().toString());
						Thread thread = new Thread(client);
						thread.start();
						ClientManager.addClient(client);
						output(Level.Info, client.getIP() + " has connected");
					}
				} catch(EOFException eofException)
				{
					output(Level.Info, "\n Server ended the connection! ");
				} finally
				{
					output(Level.Info, "Closing connections...");
					try
					{
						ClientManager.closeAllClients();
						server.close();
					} catch(IOException ioException)
					{
						ioException.printStackTrace();
					}
				}
			} catch(IOException ioException)
			{
				output(Level.Error, "There is already a server running on that port!");
			}
		}
	}
}
