package com.theprogrammingturkey.ggserver.commands;

public abstract interface ICommand
{
	public void onCommand(String[] args);
	public String getCommandBase();
	public String getDescription();
	public String getUsage();
}