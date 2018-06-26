package com.theprogrammingturkey.ggserver.news;

import com.theprogrammingturkey.ggserver.services.IServiceCore;

public class NewsHolder
{
	private IServiceCore service;
	private INewsData news;

	public NewsHolder(IServiceCore service, INewsData news)
	{
		this.service = service;
		this.news = news;
	}

	public IServiceCore getService()
	{
		return service;
	}

	public INewsData getNews()
	{
		return news;
	}

}
