package com.theprogrammingturkey.ggserver.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.theprogrammingturkey.ggserver.ServerCore;
import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.util.MessageMapper;
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
		Map<String, String> dataPayload = new HashMap<>();
		dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, data.toString());

		CcsOutMessage message = new CcsOutMessage(clientID, messageId, dataPayload);

		if(data.has("notification_title") && data.has("notification_body"))
		{
			Map<String, String> notificationPayload = new HashMap<>();
			notificationPayload.put("title", data.get("notification_title").getAsString());
			notificationPayload.put("body", data.get("notification_body").getAsString());
			message.setNotificationPayload(notificationPayload);
		}

		ServerCore.sendFCMMessage(MessageMapper.toJsonString(message));
	}

	@Override
	public ClientType getClientType()
	{
		return ClientType.FCM;
	}
}
