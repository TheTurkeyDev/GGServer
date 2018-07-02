package com.theprogrammingturkey.ggserver.files;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.theprogrammingturkey.ggserver.ServerCore;
import com.theprogrammingturkey.ggserver.ServerCore.Level;

public class FileManager
{
	private static String[] folders = new String[] { "config", "services", "logs" };

	private static LogFile log;

	public static void initFiles()
	{
		File folderFile;
		for(String folder : folders)
		{
			folderFile = new File(folder);
			if(!folderFile.exists())
				folderFile.mkdirs();
		}

		log = new LogFile("logs", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".txt");
		if(!log.createFile())
		{
			ServerCore.output(Level.Error, "Pi Server", "Failed to create log file!!!");
		}
	}

	public static void closeFiles()
	{
		log.close();
	}

	public static LogFile getLogFile()
	{
		return log;
	}
}
