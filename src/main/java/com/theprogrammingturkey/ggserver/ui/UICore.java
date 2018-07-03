package com.theprogrammingturkey.ggserver.ui;

import com.theprogrammingturkey.ggserver.news.NewsHolder;
import com.theprogrammingturkey.ggserver.services.ActiveServiceWrapper;
import com.theprogrammingturkey.ggserver.util.Settings;

public class UICore
{
	private static IUI uiInstance;

	public static void initUI(String[] args)
	{
		if(Settings.lightGUI)
		{
			uiInstance = new UILight();
		}
		else
		{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					UIJavaFx.init(args);
				}
			}).start();

			// TODO: Don't do this
			try
			{
				Thread.sleep(3000);
			} catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	// TODO: This seems really stupid, but idk a way around this
	public static void setUIIstance(IUI instance)
	{
		uiInstance = instance;
	}

	public static void consoleMessage(String message)
	{
		uiInstance.consoleMessage(message);
	}

	public static void dispatchNews(NewsHolder news)
	{
		uiInstance.dispatchNews(news);
	}

	public static void updateService(ActiveServiceWrapper service)
	{
		uiInstance.updateService(service);
	}
}
