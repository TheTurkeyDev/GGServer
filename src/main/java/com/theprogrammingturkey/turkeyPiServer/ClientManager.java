package com.theprogrammingturkey.turkeyPiServer;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.theprogrammingturkey.turkeyPiServer.ServerCore.Level;
import com.theprogrammingturkey.turkeyPiServer.client.ClientConnection;

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
		ServerCore.output(Level.Info, client.getName() + " was disconnected with the given reason: " + reason);
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

	public static boolean isTurkeyBotConnected()
	{
		return getClientFromName("TurkeyBot") != null;
	}

	public static JsonObject getBaseJson(String purpose)
	{
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("purpose", purpose);
		jsonObj.add("data", new JsonObject());
		return jsonObj;
	}
}
