package com.theprogrammingturkey.ggserver.files;

import java.io.IOException;

public class LogFile extends BaseFile
{
	public LogFile(String path, String name)
	{
		super(path, name);
	}

	public void appendLine(String line)
	{
		if(!file.exists() || writer == null)
			return;

		try
		{
			writer.append(line + "\n");
			writer.flush();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}
