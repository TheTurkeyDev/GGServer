package com.theprogrammingturkey.ggserver.ui;

import com.theprogrammingturkey.ggserver.news.NewsHolder;
import com.theprogrammingturkey.ggserver.services.ActiveServiceWrapper;

public interface IUI
{
	public void consoleMessage(String message);

	public void dispatchNews(NewsHolder news);

	public void updateService(ActiveServiceWrapper service);
}
