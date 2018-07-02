package com.theprogrammingturkey.ggserver.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BaseFile
{
	protected File file;
	protected BufferedWriter writer;
	protected BufferedReader reader;

	public BaseFile(String path, String name)
	{
		file = new File(path, name);
		try
		{
			writer = new BufferedWriter(new FileWriter(file));
			reader = new BufferedReader(new FileReader(file));
		} catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close()
	{
		try
		{
			writer.flush();
			writer.close();
			reader.close();
		} catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean createFile()
	{
		try
		{
			file.createNewFile();
		} catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean exists()
	{
		return file.exists();
	}
}
