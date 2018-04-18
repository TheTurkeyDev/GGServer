package com.theprogrammingturkey.turkeyPiServer.client;

import com.google.gson.JsonObject;
import com.theprogrammingturkey.turkeyPiServer.ServerCore;
import com.theprogrammingturkey.turkeyPiServer.util.JsonHelper;
import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.server.MessageHelper;
import com.wedevol.xmpp.util.Util;

public class FirebaseClient implements ClientConnection
{
	private String name;
	private String clientID;

	public FirebaseClient(String name, String clientID)
	{
		this.name = name;
		this.clientID = clientID;
	}

	@Override
	public void closeConnection()
	{

	}

	@Override
	public String getName()
	{
		return name;
	}

	public void setClientID(String id)
	{
		this.clientID = id;
	}

	@Override
	public void sendMessage(JsonObject data)
	{
		String messageId = Util.getUniqueMessageId();
		JsonObject dataPayload = new JsonObject();
		dataPayload.addProperty(Util.PAYLOAD_ATTRIBUTE_MESSAGE, data.toString());

		CcsOutMessage message = new CcsOutMessage(clientID, messageId, JsonHelper.gsonToSimpleString(dataPayload));

		if (data.has("notification_title") && data.has("notification_body"))
		{
			JsonObject notificationPayload = new JsonObject();
			notificationPayload.addProperty("title", data.get("notification_title").getAsString());
			notificationPayload.addProperty("body", data.get("notification_body").getAsString());
			message.setNotificationPayload(JsonHelper.gsonToSimpleString(notificationPayload));
		}

		ServerCore.sendFCMMessage(MessageHelper.createJsonOutMessage(message));
	}

	@Override
	public ClientType getClientType()
	{
		return ClientType.FCM;
	}
}
