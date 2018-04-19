package com.theprogrammingturkey.ggserver.events;

import com.google.gson.JsonElement;

public abstract class PacketRecievedEvent
{
	private String destinationID;

	public PacketRecievedEvent(String destinationID)
	{
		this.destinationID = destinationID;
	}

	public abstract void onPacketRecievedEvent(JsonElement json);

	public String getdestinationID()
	{
		return this.destinationID;
	}
}
