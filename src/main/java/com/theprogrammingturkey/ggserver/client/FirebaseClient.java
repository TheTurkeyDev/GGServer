package com.theprogrammingturkey.ggserver.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.theprogrammingturkey.ggserver.ServerCore;
import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.util.MessageMapper;
import com.wedevol.xmpp.util.Util;

public class FirebaseClient
{
	private String name;
	private String clientID;

	public FirebaseClient(String name, String clientID)
	{
		this.name = name;
		this.clientID = clientID;
	}

	public String getName()
	{
		return name;
	}

	public String getID()
	{
		return this.clientID;
	}

	public void setClientID(String id)
	{
		this.clientID = id;
	}

	public void sendMessage(JsonObject data)
	{
		Map<String, String> dataPayload = new HashMap<>();
		dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, data.toString());

		CcsOutMessage message = new CcsOutMessage(clientID, Util.getUniqueMessageId(), dataPayload);
		ServerCore.sendFCMMessage(message.getMessageId(), MessageMapper.toJsonString(message));

		// For some reason you can send a message with both data an a notification or else the
		// notification doesn't trigger. To get around this we send 2 separate messages
		if(data.has("notification_title") && data.has("notification_body"))
		{
			message = new CcsOutMessage(clientID, Util.getUniqueMessageId(), null);
			Map<String, String> notificationPayload = new HashMap<>();
			notificationPayload.put("title", data.get("notification_title").getAsString());
			notificationPayload.put("body", data.get("notification_body").getAsString());
			message.setNotificationPayload(notificationPayload);

			ServerCore.sendFCMMessage(message.getMessageId(), MessageMapper.toJsonString(message));
		}
	}

	public void closeConnection()
	{

	}
}
