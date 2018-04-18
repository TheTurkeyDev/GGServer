package com.theprogrammingturkey.ggserver.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.theprogrammingturkey.ggserver.ServerCore;
import com.theprogrammingturkey.ggserver.ServerCore.Level;

public class ServiceManager
{
	private static Map<String, IServiceCore> services = new HashMap<>();
	private static Map<String, File> serviceFiles = new HashMap<>();

	public static void startServices()
	{
		for(File file : new File("/Services").listFiles())
		{
			if(getFileExtension(file.getName()).equalsIgnoreCase(".jar"))
			{
				startService(file);
			}
		}
	}

	public static void startService(File file)
	{
		String serviceName = getFileName(file.getName());
		ServerCore.output(Level.Info, "Starting Service " + serviceName + "...");
		try
		{
			URLClassLoader loader = new URLClassLoader(new URL[] { file.toURI().toURL() });

			Properties properties = new Properties();
			properties.load(loader.getResourceAsStream("plugin.properties"));

			String classPath = properties.getProperty("Base Class Path");

			Class<?> clazz = Class.forName(classPath, true, loader);
			Object instanceClazz = clazz.newInstance();
			// static call
			if(instanceClazz instanceof IServiceCore)
			{
				IServiceCore service = (IServiceCore) instanceClazz;
				service.init();
				String name = service.getServiceName();
				services.put(name, service);
				serviceFiles.put(name, file);
				ServerCore.output(Level.Info, "Started Service " + serviceName);
			}
			else
			{
				ServerCore.output(Level.Error, "Unable to start service! Core class does not implement IServiceCore!");
			}

		} catch(Exception e)
		{
			ServerCore.output(Level.Error, "Unable to start service " + serviceName + ".");
			ServerCore.output(Level.Error, e.getMessage());
		}
	}

	public static void stopService(String serviceName)
	{
		try
		{
			services.get(serviceName).stop();
			services.remove(serviceName);
			serviceFiles.remove(serviceName);
			ServerCore.output(Level.Info, serviceName + " Stopped.");
		} catch(Exception e)
		{
			ServerCore.output(Level.Error, "Unable to stop service " + serviceName + ".");
		}
	}
	
	public static void restartService(String serviceName)
	{
		File file = serviceFiles.get(serviceName);
		ServiceManager.stopService(serviceName);
		ServiceManager.startService(file);
	}

	private static String getFileExtension(String name)
	{
		return name.substring(name.lastIndexOf("."));
	}

	private static String getFileName(String name)
	{
		return name.substring(0, name.lastIndexOf("."));
	}

	public static class ConsoleOutput implements Runnable
	{
		private Process p;

		public ConsoleOutput(Process p)
		{
			this.p = p;
		}

		@Override
		public void run()
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			try
			{
				while((line = reader.readLine()) != null)
					ServerCore.output(Level.Info, line);
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
