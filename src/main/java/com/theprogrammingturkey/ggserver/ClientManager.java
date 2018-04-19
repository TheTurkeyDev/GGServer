package com.theprogrammingturkey.ggserver;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.theprogrammingturkey.ggserver.ServerCore.Level;
import com.theprogrammingturkey.ggserver.client.ClientConnection;
import com.theprogrammingturkey.ggserver.events.EventManager;
import com.theprogrammingturkey.ggserver.services.ServiceManager;

public class ClientManager
{
	private static List<ClientConnection> clients = new ArrayList<ClientConnection>();

	public static void addClient(ClientConnection client)
	{
		clients.add(client);
	}

	public static void closeAllClients()
	{
		for(ClientConnection c : clients)
			c.closeConnection();
	}

	public static ClientConnection getClientFromName(String name)
	{
		for(ClientConnection c : clients)
			if(name.equals(c.getName()))
				return c;
		return null;
	}

	public static void closeConnection(String client, String reason)
	{
		ClientConnection clientConnect = getClientFromName(client);
		if(clientConnect != null)
			closeConnection(clientConnect, reason);
	}

	public static void closeConnection(ClientConnection client, String reason)
	{
		client.closeConnection();
		clients.remove(client);
		ServerCore.output(Level.Info, "Pi Server", client.getName() + " was disconnected with the given reason: " + reason);
	}

	public static void sendClientMessage(String client, JsonObject message)
	{
		ClientConnection clientConnect = getClientFromName(client);
		if(clientConnect != null)
			sendClientMessage(clientConnect, message);
	}

	public static void sendClientMessage(ClientConnection client, JsonObject message)
	{
		client.sendMessage(message);
	}

	public static void handlePacket(String destination, String purpose, JsonObject data)
	{
		if(purpose.equals("message"))
		{
			ClientManager.sendClientMessage(destination, data);
		}
		else if(purpose.equals("server"))
		{
			if(data.get("action").getAsString().equalsIgnoreCase("Restart Service"))
			{
				ServiceManager.restartService(data.get("serviceID").getAsString());
			}
		}
		else
		{
			EventManager.firePacketRecievedEvent(destination, data);
		}
	}

	public static JsonObject getBaseJson(String purpose)
	{
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("purpose", purpose);
		jsonObj.add("data", new JsonObject());
		return jsonObj;
	}
}
