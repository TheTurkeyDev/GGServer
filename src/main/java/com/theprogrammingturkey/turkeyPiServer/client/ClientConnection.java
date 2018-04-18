package com.theprogrammingturkey.turkeyPiServer.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public interface ClientConnection
{
	static final JsonParser PARSER = new JsonParser();

	public void closeConnection();

	public String getName();

	public void sendMessage(JsonObject message);

	public ClientType getClientType();

	public enum ClientType
	{
		SOCKET, FCM;
	}
}
