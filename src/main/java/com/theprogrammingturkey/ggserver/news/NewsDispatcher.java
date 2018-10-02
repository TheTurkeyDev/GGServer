package com.theprogrammingturkey.ggserver.news;

import com.google.gson.JsonObject;
import com.theprogrammingturkey.ggserver.ServerCore;
import com.theprogrammingturkey.ggserver.ServerCore.Level;
import com.theprogrammingturkey.ggserver.client.ClientManager;
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

		JsonObject json = new JsonObject();
		json.addProperty("purpose", "News");

		if(news.hasNotification())
		{
			json.addProperty("notification_title", news.getTitle());
			json.addProperty("notification_body", news.getDesc());
		}

		JsonObject data = new JsonObject();

		data.addProperty("service_name", service.getServiceName());
		data.addProperty("service_id", service.getServiceID());
		data.addProperty("title", news.getTitle());
		data.addProperty("desc", news.getDesc());
		data.addProperty("data", news.getData());

		json.add("data", data);

		ClientManager.sendMessageToAll(json);
		ServerCore.output(Level.Info, "Pi Server", "New news! '" + news.getTitle() + "' by '" + service.getServiceID() + "'");
	}
}
