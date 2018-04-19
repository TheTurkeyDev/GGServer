package com.theprogrammingturkey.ggserver.commands;

import java.util.HashMap;
import java.util.Map;

import com.theprogrammingturkey.ggserver.ServerCore;
import com.theprogrammingturkey.ggserver.ServerCore.Level;

public class CommandManager
{
	private static Map<String, ICommand> commandList = new HashMap<String, ICommand>();

	private static Map<String, String> aliases = new HashMap<String, String>();

	public static void registerCommand(ICommand command)
	{
		commandList.put(command.getCommandBase().toUpperCase(), command);
	}

	public static void registerCommandAlias(String command, String alias)
	{
		registerCommandAlias(commandList.get(command.toUpperCase()), alias);
	}

	public static void registerCommandAlias(ICommand command, String alias)
	{
		if(command != null)
			aliases.put(alias.toUpperCase(), command.getCommandBase());
	}

	public static void processCommand(String command)
	{
		if(command.length() > 0 && command.charAt(0) == '/')
		{
			int firstSpace = command.indexOf(" ");
			if(firstSpace == -1)
				firstSpace = command.length();
			String base = command.substring(1, firstSpace).toUpperCase();
			String[] params;
			if(firstSpace == command.length())
				params = new String[0];
			else
				params = command.substring(firstSpace + 1).split(" ");

			String commandBase = aliases.get(base);
			if(commandBase == null)
				commandBase = base;

			if(commandBase.equals("help"))
			{
				if(params.length > 0)
				{
					String helpComBase = aliases.get(params[0].toUpperCase());
					if(helpComBase == null)
						helpComBase = params[0].toUpperCase();
					if(commandList.containsKey(helpComBase))
						ServerCore.output(Level.Info, "Pi Server", "Usage: " + commandList.get(helpComBase).getUsage());
				}
				else
				{
					ServerCore.output(Level.Info, "Pi Server", "Usage: /help [commandName]");
				}
			}
			else if(commandBase.equals("commands"))
			{
				// TODO
			}

			if(commandList.containsKey(commandBase))
				commandList.get(commandBase).onCommand(params);
			else
				ServerCore.output(Level.Error, "Pi Server", "Sorry, " + base + " is not a valid command!");
		}
	}
}
