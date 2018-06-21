package com.theprogrammingturkey.ggserver.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.theprogrammingturkey.ggserver.ServerCore;
import com.theprogrammingturkey.ggserver.ServerCore.Level;
import com.theprogrammingturkey.ggserver.services.IServiceCore;
import com.theprogrammingturkey.ggserver.services.ServiceManager;

public class CommandManager
{
	private static Map<String, ICommand> commandList = new HashMap<String, ICommand>();

	private static Map<String, String> aliases = new HashMap<String, String>();

	public static void registerCommand(ICommand command)
	{
		commandList.put(command.getCommandBase().toLowerCase(), command);
	}

	public static void registerCommandAlias(String command, String alias)
	{
		registerCommandAlias(commandList.get(command.toLowerCase()), alias);
	}

	public static void registerCommandAlias(ICommand command, String alias)
	{
		if(command != null)
			aliases.put(alias.toLowerCase(), command.getCommandBase());
	}

	public static void registerDefaultCommands()
	{
		registerCommand(new SimpleCommand("help", "Command help", "/help <command>")
		{
			@Override
			public void onCommand(String[] params)
			{
				if(params.length > 0)
				{
					String helpComBase = aliases.get(params[0].toLowerCase());
					if(helpComBase == null)
						helpComBase = params[0].toLowerCase();
					if(commandList.containsKey(helpComBase))
						ServerCore.output(Level.Info, "Pi Server", "Usage: " + commandList.get(helpComBase).getUsage());
				}
				else
				{
					ServerCore.output(Level.Info, "Pi Server", "Usage: /help <commandName>");
				}
			}
		});
		registerCommandAlias("help", "h");

		registerCommand(new SimpleCommand("stop", "Stop and exit the server safely", "/stop")
		{
			@Override
			public void onCommand(String[] args)
			{
				ServerCore.StopServer();
			}
		});

		registerCommand(new SimpleCommand("commands", "List available commands", "/commands [page]")
		{
			@Override
			public void onCommand(String[] params)
			{
				int page = 1;
				if(params.length > 0)
				{
					try
					{
						page = Integer.parseInt(params[0]);
					} catch(Exception e)
					{
						ServerCore.output(Level.Error, "Pi Server", params[0] + "is not a valid number!");
						return;
					}
				}

				List<String> commands = new ArrayList<String>(commandList.keySet());
				commands.sort(new Comparator<String>()
				{

					@Override
					public int compare(String s1, String s2)
					{
						return s1.compareTo(s2);
					}

				});

				if((page - 1) * 10 >= commands.size())
				{
					page = (commands.size() - 1) / 10 + 1;
				}

				StringBuilder builder = new StringBuilder();
				builder.append("\n");
				builder.append("====== COMMANDS LIST ======");
				builder.append("\n");
				builder.append("====== Page ");
				builder.append(page);
				builder.append("/");
				builder.append((commands.size() - 1) / 10 + 1);
				builder.append(" ======");
				builder.append("\n");
				ICommand com;
				for(int i = (10 * (page - 1)); i < 10 * page; i++)
				{
					if(i >= commands.size())
						break;

					builder.append("- ");
					builder.append(commands.get(i));
					builder.append("\n");
					builder.append("\t Description: ");
					com = commandList.get(commands.get(i));
					builder.append(com.getDescription());
					builder.append("\n");
					builder.append("\t Usage: ");
					builder.append(com.getUsage());
					builder.append("\n");
				}

				ServerCore.output(Level.Error, "Pi Server", builder.toString());
			}
		});

		registerCommand(new SimpleCommand("ping", "Ding Dong!", "/ping")
		{
			@Override
			public void onCommand(String[] args)
			{
				ServerCore.output(Level.Info, "Pi Server", "Pong!");
			}
		});

		registerCommand(new SimpleCommand("serviceinfo", "Get info of a specific service", "/serviceinfo <serviceID>")
		{
			@Override
			public void onCommand(String[] args)
			{
				if(args.length > 0)
				{
					IServiceCore service = ServiceManager.getServiceFromID(args[0]);
					if(service != null)
					{
						ServerCore.output(Level.Info, "Pi Server", "-Services Info-");
						ServerCore.output(Level.Info, "Pi Server", "\tService Name: " + service.getServiceName());
						ServerCore.output(Level.Info, "Pi Server", "\tService ID: " + service.getServiceID());
						ServerCore.output(Level.Info, "Pi Server", "\tService Status: " + ServiceManager.getServiceStatus(service.getServiceID()));
					}
					else
					{
						ServerCore.output(Level.Info, "Pi Server", args[0] + " is not a valid service ID.");
					}
				}
				else
				{
					ServerCore.output(Level.Info, "Pi Server", "Usage: /serviceinfo <commandName>");
				}
			}
		});
		registerCommandAlias("serviceinfo", "si");

		registerCommand(new SimpleCommand("services", "Get a list of running services", "/services")
		{
			@Override
			public void onCommand(String[] args)
			{
				ServerCore.output(Level.Info, "Pi Server", "Services:");
				for(String s : ServiceManager.getServices())
					ServerCore.output(Level.Info, "Pi Server", "\t" + s);
			}
		});
	}

	public static void processCommand(String command)
	{
		if(command.length() > 0 && command.charAt(0) == '/')
		{
			int firstSpace = command.indexOf(" ");
			if(firstSpace == -1)
				firstSpace = command.length();
			String base = command.substring(1, firstSpace).toLowerCase();
			String[] params;
			if(firstSpace == command.length())
				params = new String[0];
			else
				params = command.substring(firstSpace + 1).split(" ");

			String commandBase = aliases.get(base);
			if(commandBase == null)
				commandBase = base;

			if(commandList.containsKey(commandBase))
			{
				commandList.get(commandBase).onCommand(params);
			}
			else
			{
				ServerCore.output(Level.Error, "Pi Server", "Sorry, /" + base + " is not a valid command!");
			}
		}
	}
}
