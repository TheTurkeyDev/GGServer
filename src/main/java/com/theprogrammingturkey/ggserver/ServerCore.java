package com.theprogrammingturkey.ggserver;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.theprogrammingturkey.ggserver.client.SocketClient;
import com.theprogrammingturkey.ggserver.commands.CommandManager;
import com.theprogrammingturkey.ggserver.commands.SimpleCommand;
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
		output(Level.Info, "Pi Server", "Starting Firebase Connection...");
		if(connectToFirebase())
			output(Level.Info, "Pi Server", "Firebase Connected!");
		output(Level.Info, "Pi Server", "Starting Server...");
		socketThread = new Thread(new SocketManager());
		socketThread.start();
		output(Level.Info, "Pi Server", "Server Started!");
		output(Level.Info, "Pi Server", "Loading Services...");
		ServiceManager.startServices();
		output(Level.Info, "Pi Server", "Services Loaded!");
		initConsoleUnput();
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
		output(Level.Info, "Pi Server", message);
	}

	public static void output(Level level, String sender, String message)
	{
		if(level == Level.DeBug && !debug)
			return;

		System.out.println("[" + dateFormatter.format(new Date()) + "][" + sender + "] [" + level.getLevel() + "]: " + message);
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

	private void initConsoleUnput()
	{
		CommandManager.registerCommand(new SimpleCommand("ping", "Ding Dong!", "/ping")
		{
			@Override
			public void onCommand(String[] args)
			{
				ServerCore.output(Level.Info, "Pi Server", "Pong!");
			}
		});

		InputCheckThread input = new InputCheckThread();
		input.init();
	}

	private class InputCheckThread implements Runnable
	{
		private boolean run = false;
		private Thread thread;

		public void init()
		{
			run = true;
			if(thread == null || !thread.isAlive())
			{
				thread = new Thread(this);
				thread.start();
			}
		}

		public void run()
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while(run)
			{
				try
				{
					CommandManager.processCommand(br.readLine());
				} catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			try
			{
				thread.interrupt();
				thread.join();
			} catch(InterruptedException e)
			{
			}
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
						output(Level.Info, "Pi Server", client.getIP() + " has connected");
					}
				} catch(EOFException eofException)
				{
					output(Level.Info, "Pi Server", "\n Server ended the connection! ");
				} finally
				{
					output(Level.Info, "Pi Server", "Closing connections...");
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
				output(Level.Error, "Pi Server", "There is already a server running on that port!");
			}
		}
	}
}
