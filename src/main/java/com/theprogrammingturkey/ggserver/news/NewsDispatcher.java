package com.theprogrammingturkey.ggserver.news;

import com.theprogrammingturkey.ggserver.ServerCore;
import com.theprogrammingturkey.ggserver.ServerCore.Level;
import com.theprogrammingturkey.ggserver.services.IServiceCore;
import com.theprogrammingturkey.ggserver.services.ServiceManager;
import com.theprogrammingturkey.ggserver.ui.UICore;

public class NewsDispatcher
{
	public static void dispatch(INewsData news)
	{
		IServiceCore service = ServiceManager.getServiceFromID(news.getServiceID());
		if(service == null)
		{
			ServerCore.output(Level.Error, "Pi Server", "The given Service ID \"" + news.getServiceID() + "\" is invalid! The News could not be dispatched.");
			return;
		}
		UICore.dispatchNews(new NewsHolder(service, news));
		// TODO: Send to the app
	}
}
