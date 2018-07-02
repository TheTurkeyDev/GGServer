package com.theprogrammingturkey.ggserver.util;

import java.io.IOException;
import java.io.OutputStream;

import com.theprogrammingturkey.ggserver.files.FileManager;
import com.theprogrammingturkey.ggserver.ui.UICore;

public class ConsoleHelper extends OutputStream
{
	private String messagePart = "";

	@Override
	public void write(int i) throws IOException
	{
		if(i != '\n')
		{
			messagePart += String.valueOf((char) i);
		}
		else
		{
			write(messagePart);
			messagePart = "";
		}
	}

	public static void write(String message)
	{
		UICore.consoleMessage(message);
		FileManager.getLogFile().appendLine(message);
	}

}
