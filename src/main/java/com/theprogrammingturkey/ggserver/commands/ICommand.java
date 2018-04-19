package com.theprogrammingturkey.ggserver.commands;

public abstract interface ICommand
{
	public boolean onCommand(String[] args);
	public String getCommandBase();
	public String getDescription();
	public String getUsage();
}