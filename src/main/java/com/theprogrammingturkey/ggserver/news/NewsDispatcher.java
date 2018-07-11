package com.theprogrammingturkey.ggserver.news;

import com.google.gson.JsonObject;
import com.theprogrammingturkey.ggserver.ServerCore;
import com.theprogrammingturkey.ggserver.ServerCore.Level;
import com.theprogrammingturkey.ggserver.services.IServiceCore;
import com.theprogrammingturkey.ggserver.services.ServiceManager;
import com.theprogrammingturkey.ggserver.ui.UICore;
import com.wedevol.xmpp.util.Util;

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
		json.addProperty("to", ServerCore.myAppID);
		String messageID = Util.getUniqueMessageId();
		json.addProperty("message_id", messageID);

		if(news.hasNotification())
		{
			JsonObject notification = new JsonObject();
			notification.addProperty("title", news.getTitle());
			notification.addProperty("body", news.getDesc());
			json.add("notification", notification);
		}

		JsonObject data = new JsonObject();
		data.addProperty("purpose", "News");
		JsonObject newsData = new JsonObject();
		newsData.addProperty("service_name", service.getServiceName());
		newsData.addProperty("service_id", service.getServiceID());
		newsData.addProperty("title", news.getTitle());
		newsData.addProperty("desc", news.getDesc());
		newsData.addProperty("data", news.getData());
		data.add("news_data", newsData);
		json.add("data", data);
		ServerCore.sendFCMMessage(messageID, json.toString());
		ServerCore.output(Level.Info, "Pi Server", "New news! '" + news.getTitle() + "' by '" + service.getServiceID() + "'");
	}
}
