package com.theprogrammingturkey.ggserver.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.theprogrammingturkey.ggserver.ServerCore;
import com.theprogrammingturkey.ggserver.ServerCore.Level;
import com.theprogrammingturkey.ggserver.events.EventManager;
import com.theprogrammingturkey.ggserver.services.ServiceManager;

public class ClientManager
{
	private static List<FirebaseClient> clients = new ArrayList<FirebaseClient>();

	public static void addClient(FirebaseClient client)
	{
		clients.add(client);
	}

	public static void closeAllClients()
	{
		for(FirebaseClient c : clients)
			c.closeConnection();
	}

	public static FirebaseClient getClientFromName(String name)
	{
		for(FirebaseClient c : clients)
			if(name.equals(c.getName()))
				return c;
		return null;
	}

	public static FirebaseClient getClientFromID(String id)
	{
		for(FirebaseClient c : clients)
			if(id.equals(c.getID()))
				return c;
		return null;
	}

	public static void closeConnection(String client, String reason)
	{
		FirebaseClient clientConnect = getClientFromName(client);
		if(clientConnect != null)
			closeConnection(clientConnect, reason);
	}

	public static void closeConnection(FirebaseClient client, String reason)
	{
		client.closeConnection();
		clients.remove(client);
		ServerCore.output(Level.Info, "Pi Server", client.getName() + " was disconnected with the given reason: " + reason);
	}

	public static void sendMessageToAll(JsonObject message)
	{
		for(FirebaseClient clientConnect : clients)
			sendClientMessage(clientConnect, message);
	}

	public static void sendClientMessage(String client, JsonObject message)
	{
		FirebaseClient clientConnect = getClientFromName(client);
		if(clientConnect != null)
			sendClientMessage(clientConnect, message);
	}

	public static void sendClientMessage(FirebaseClient client, JsonObject message)
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
