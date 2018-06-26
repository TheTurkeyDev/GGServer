package com.theprogrammingturkey.ggserver.news;

import com.theprogrammingturkey.ggserver.services.IServiceCore;

public class NewsDispatcher
{
	private IServiceCore service;

	public NewsDispatcher(IServiceCore service)
	{
		this.service = service;
	}
}
