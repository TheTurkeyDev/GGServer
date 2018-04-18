package com.theprogrammingturkey.turkeyPiServer.services;

public interface IServiceCore
{
	public void init();
	
	public void stop();
	
	public String getServiceName();
}
