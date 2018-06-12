package com.theprogrammingturkey.ggserver.commands;

public abstract class SimpleCommand implements ICommand
{
	private String command;
	private String description;
	private String usage;

	public SimpleCommand(String command, String desc)
	{
		this(command, desc, "/" + command);
	}

	public SimpleCommand(String command, String desc, String usage)
	{
		this.command = command;
		this.description = desc;
		this.usage = usage;
	}

	@Override
	public String getCommandBase()
	{
		return command;
	}

	@Override
	public String getDescription()
	{
		return description;
	}

	@Override
	public String getUsage()
	{
		return usage;
	}
}