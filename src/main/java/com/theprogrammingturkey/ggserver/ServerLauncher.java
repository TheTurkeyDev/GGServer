package com.theprogrammingturkey.ggserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.jivesoftware.smack.SmackException;

import com.theprogrammingturkey.ggserver.ServerCore.Level;
import com.theprogrammingturkey.ggserver.ui.UICore;

public class ServerLauncher
{
	private static String[] folders = new String[] { "config", "services" };

	public static void main(String[] args) throws SmackException, IOException, InterruptedException
	{
		// TODO: Idk what the best way is, but this works for now
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				UICore.init(args);
			}
		}).start();

		// TODO: Don't do this
		Thread.sleep(3000);

		File folderFile;
		for(String folder : folders)
		{
			folderFile = new File(folder);
			if(!folderFile.exists())
				folderFile.mkdirs();
		}

		Properties properties = new Properties();
		File file = new File("settings.prop");
		if(!file.exists())
		{
			file.createNewFile();
			try
			{
				properties.setProperty("ProjectID", "");
				properties.setProperty("APIKey", "");
				properties.store(new FileOutputStream(file), "");
			} catch(Exception e)
			{
			}
			ServerCore.output(Level.Severe, "Pi Server", "Settings file generated. Please enter information in file.");
			return;
		}
		FileInputStream iStream = new FileInputStream(file);
		properties.load(iStream);
		String projID = properties.getProperty("ProjectID");
		String apiKey = properties.getProperty("APIKey");
		new ServerCore(projID, apiKey);
	}
}
