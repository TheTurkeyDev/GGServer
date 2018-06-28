package com.theprogrammingturkey.ggserver.news;

public interface INewsData
{
	public String getTitle();

	public String getDesc();
	
	public String getData();
	
	public String getServiceID();
	
	public boolean hasNotification();
}
