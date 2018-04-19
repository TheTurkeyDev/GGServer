package com.theprogrammingturkey.ggserver.events;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;

public class EventManager
{
	private static List<ConsoleCommandEvent> consoleCommandListeners = new ArrayList<>();
	private static List<PacketRecievedEvent> packetListeners = new ArrayList<>();

	public static void registerConsoleCommandListener(ConsoleCommandEvent listener)
	{
		consoleCommandListeners.add(listener);
	}

	public static void registerPacketListener(ConsoleCommandEvent listener)
	{
		consoleCommandListeners.add(listener);
	}

	public static void fireConsoleCommandEvent(String commandBase, String[] args)
	{
		for(ConsoleCommandEvent listener : consoleCommandListeners)
		{
			if(commandBase.equals(listener.getCommandBase()))
			{
				listener.onConsoleCommandEvent(args);
			}
		}
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
