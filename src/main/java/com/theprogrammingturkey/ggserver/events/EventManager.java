package com.theprogrammingturkey.ggserver.events;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;

public class EventManager
{
	private static List<PacketRecievedEvent> packetListeners = new ArrayList<>();

	public static void registerPacketListener(PacketRecievedEvent listener)
	{
		packetListeners.add(listener);
	}

	public static void firePacketRecievedEvent(String destination, JsonElement json)
	{
		for(PacketRecievedEvent listener : packetListeners)
		{
			if(destination.equals(listener.getdestinationID()))
			{
				listener.onPacketRecievedEvent(json);
			}
		}
	}
}
