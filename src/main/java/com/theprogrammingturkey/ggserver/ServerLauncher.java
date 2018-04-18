package com.theprogrammingturkey.ggserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.jivesoftware.smack.SmackException;

import com.theprogrammingturkey.ggserver.ServerCore.Level;

public class ServerLauncher
{
	public static void main(String[] args) throws SmackException, IOException
	{
		Properties properties = new Properties();
		File file = new File("settings.prop");
		if(!file.exists())
		{
			file.createNewFile();
			try
			{
				//TODO: Copy a default file
				properties.store(new FileOutputStream(file), "");
			} catch(Exception e)
			{
			}
			ServerCore.output(Level.Severe, "Settings file generated. Please enter information in file.");
			return;
		}
		FileInputStream iStream = new FileInputStream(file);
		properties.load(iStream);
		String projID = properties.getProperty("ProjectID");
		String apiKey = properties.getProperty("APIKey");
		new ServerCore(projID, apiKey);
	}
}
