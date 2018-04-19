package com.theprogrammingturkey.ggserver.events;

public abstract class ConsoleCommandEvent
{
	private String commandBase;

	public ConsoleCommandEvent(String commandBase)
	{
		this.commandBase = commandBase;
	}

	public abstract void onConsoleCommandEvent(String args[]);

	public String getCommandBase()
	{
		return this.commandBase;
	}
}
