package com.theprogrammingturkey.ggserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import org.jivesoftware.smack.SmackException;

import com.theprogrammingturkey.ggserver.ServerCore.Level;
import com.theprogrammingturkey.ggserver.files.FileManager;
import com.theprogrammingturkey.ggserver.ui.UICore;
import com.theprogrammingturkey.ggserver.util.ConsoleHelper;
import com.theprogrammingturkey.ggserver.util.Settings;

public class ServerLauncher
{
	public static void main(String[] args) throws SmackException, IOException, InterruptedException
	{
		for(String arg : args)
		{
			if(arg.equalsIgnoreCase("lightgui"))
				Settings.lightGUI = true;
			else if(arg.equalsIgnoreCase("debug"))
				Settings.debugMode = true;
		}

		PrintStream ps = new PrintStream(new ConsoleHelper(), true);
		System.setOut(ps);
		System.setErr(ps);
		
		UICore.initUI(args);

		FileManager.initFiles();

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
