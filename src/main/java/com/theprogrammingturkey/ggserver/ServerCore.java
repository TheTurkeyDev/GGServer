package com.theprogrammingturkey.ggserver;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.theprogrammingturkey.ggserver.client.SocketClient;
import com.theprogrammingturkey.ggserver.commands.CommandManager;
import com.theprogrammingturkey.ggserver.events.EventManager;
import com.theprogrammingturkey.ggserver.services.ServiceManager;
import com.theprogrammingturkey.ggserver.ui.UICore;
import com.theprogrammingturkey.ggserver.util.JsonHelper;
import com.wedevol.xmpp.bean.CcsInMessage;
import com.wedevol.xmpp.server.CcsClient;
import com.wedevol.xmpp.util.Util;

public class ServerCore extends CcsClient
{
	public static boolean debug = false;
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss", Locale.US);

	private Thread socketThread;

	private static ServerCore instance;

	public static String myAppID = "";

	protected static final Logger logger = LoggerFactory.getLogger("PI Server");

	public ServerCore(String projectId, String apiKey)
	{
		super(projectId, apiKey, debug);
		instance = this;
		output(Level.Info, "Pi Server", "Starting Firebase Connection...");
		if(connectToFirebase())
			output(Level.Info, "Pi Server", "Firebase Connected!");
		output(Level.Info, "Pi Server", "Starting Server...");
		// socketThread = new Thread(new SocketManager());
		// socketThread.start();
		output(Level.Info, "Pi Server", "Server Started!");
		output(Level.Info, "Pi Server", "Loading Services...");
		ServiceManager.startServices();
		output(Level.Info, "Pi Server", "Services Loaded!");
		CommandManager.registerDefaultCommands();
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

	// TODO: Figure out a better logging strategy
	public static void output(Level level, String sender, String message)
	{
		if(level == Level.DeBug && !debug)
			return;

		UICore.consoleMessage("[" + dateFormatter.format(new Date()) + "][" + sender + "] [" + level.getLevel() + "]: " + message);
	}

	@Override
	public void handlePacketRecieved(CcsInMessage packet)
	{
		JsonObject json = JsonHelper.getJsonFromString(packet.getDataPayload().get("json_data")).getAsJsonObject();
		if(json.has("purpose") && json.get("purpose").getAsString().equalsIgnoreCase("handshake"))
		{
			myAppID = json.getAsJsonObject("data").get("id").getAsString();
			String uuid = Util.getUniqueMessageId();
			JsonObject response = new JsonObject();
			response.addProperty("to", myAppID);
			response.addProperty("message_id", uuid);
			response.addProperty("purpose", "handshake");
			sendDownstreamMessage(uuid, response.toString());
			output(Level.Info, "Pi Server", "APP Connected");
			return;
		}
		EventManager.firePacketRecievedEvent(packet.getDataPayload().get("destination"), JsonHelper.getJsonFromMap(packet.getDataPayload()));
		output(Level.Alert, "Pi Server", "Packet Recived! From:" + packet.getDataPayload().toString());
	}

	public static void sendFCMMessage(String uuid, String message)
	{
		instance.sendDownstreamMessage(uuid, message);
	}

	public static void StopServer()
	{
		output(Level.Info, "Pi Server", "Stopping...");
		for(String s : ServiceManager.getServices())
			ServiceManager.stopService(s);

		instance.disconnectGracefully();

		while(instance.isAlive())
		{
		}

		System.exit(1);
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
