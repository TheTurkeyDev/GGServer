package com.theprogrammingturkey.ggserver.client;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.theprogrammingturkey.ggserver.ClientManager;
import com.theprogrammingturkey.ggserver.ServerCore;
import com.theprogrammingturkey.ggserver.ServerCore.Level;
import com.theprogrammingturkey.ggserver.services.ServiceManager;

public class SocketClient implements Runnable, ClientConnection
{
	private static final JsonParser PARSER = new JsonParser();
	private DataOutputStream output;
	private BufferedInputStream input;
	private Socket connect;

	private String name;

	private static boolean running = false;

	public SocketClient(Socket connection, String name)
	{
		connect = connection;
		DataOutputStream o = null;
		BufferedInputStream i = null;
		try
		{
			o = new DataOutputStream(connection.getOutputStream());
			o.flush();
			i = new BufferedInputStream(connection.getInputStream());
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		output = o;
		input = i;

		this.name = name;
	}

	public void run()
	{
		running = true;
		while(running)
		{
			if(input != null)
			{
				try
				{
					StringBuilder buffer = new StringBuilder();
					int chars_read;
					while((chars_read = input.read()) != -1)
					{
						buffer.append((char) chars_read);
						if(buffer.toString().endsWith("<SPLIT>"))
							break;
					}

					String message = buffer.toString().replaceAll("<SPLIT>", "");

					ServerCore.output(Level.DeBug, "Pi Server", "Packet IN -> " + message);

					if(message.trim().equals(""))
						continue;
					JsonObject json = PARSER.parse(message).getAsJsonObject();
					if(!json.has("purpose"))
						continue;
					String purpose = json.get("purpose").getAsString();
					if(purpose.equals("handshake"))
					{
						this.name = json.get("data").getAsJsonObject().get("name").getAsString();
						ServerCore.output(Level.Info, "Pi Server", name + " has connected!");
					}
					else if(purpose.equals("message"))
					{
						ClientManager.sendClientMessage(json.get("destination").getAsString(), json.get("data").getAsJsonObject());
					}
					else if(purpose.equals("server"))
					{
						String action = json.get("data").getAsJsonObject().get("action").getAsString();
						if(action.equalsIgnoreCase("TurkeyBot Restart"))
						{
							ServiceManager.restartService("turkeybot");
						}
					}
					else if(purpose.equals("disconnect"))
					{
						running = false;
					}
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}

	}

	public void closeConnection()
	{
		running = false;
		try
		{
			output.close();
			input.close();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public String getName()
	{
		return this.name;
	}

	public void sendMessage(JsonObject message)
	{
		try
		{
			output.writeBytes(message.toString() + "<SPLIT>");
			output.flush();
		} catch(IOException e)
		{
			e.printStackTrace();
		}

	}

	public InetAddress getIP()
	{
		return connect.getInetAddress();
	}

	@Override
	public ClientType getClientType()
	{
		return ClientType.SOCKET;
	}
}