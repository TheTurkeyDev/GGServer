package com.theprogrammingturkey.ggserver.services;

import java.io.File;
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

	private static File configFolder;

	public static void startServices()
	{
		configFolder = new File("config");
		if(!configFolder.exists())
			configFolder.mkdirs();

		File servicesFolder = new File("services");
		if(!servicesFolder.exists())
			servicesFolder.mkdirs();

		for(File file : servicesFolder.listFiles())
		{
			if(getFileExtension(file.getName()).equalsIgnoreCase(".jar"))
			{
				startService(file);
			}
		}
	}

	public static void startService(File file)
	{
		ServerCore.output(Level.Info, "Pi Server", "Starting Service from file" + file.getName() + "...");
		try
		{
			URLClassLoader loader = new URLClassLoader(new URL[] { file.toURI().toURL() });

			Properties properties = new Properties();
			properties.load(loader.getResourceAsStream("plugin.properties"));

			String classPath = properties.getProperty("Base_Class_Path");

			Class<?> clazz = Class.forName(classPath, true, loader);
			Object instanceClazz = clazz.newInstance();
			// static call
			if(instanceClazz instanceof IServiceCore)
			{
				IServiceCore service = (IServiceCore) instanceClazz;
				service.init();
				String name = service.getServiceID();
				services.put(name, service);
				serviceFiles.put(name, file);
				ServerCore.output(Level.Info, "Pi Server", "Started Service " + service.getServiceName() + " from file " + file.getName());
			}
			else
			{
				ServerCore.output(Level.Error, "Pi Server", "Unable to start service from file " + file.getName() + "! Core class does not implement IServiceCore!");
			}

		} catch(Exception e)
		{
			ServerCore.output(Level.Error, "Pi Server", "Unable to start service " + file.getName() + ".");
			e.printStackTrace();
		}
	}

	public static void stopService(String serviceID)
	{
		try
		{
			services.get(serviceID).stop();
			IServiceCore service = services.remove(serviceID);
			serviceFiles.remove(serviceID);
			ServerCore.output(Level.Info, "Pi Server", service.getServiceName() + " Stopped.");
		} catch(Exception e)
		{
			ServerCore.output(Level.Error, "Pi Server", "Unable to stop service with the id " + serviceID + ".");
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

	public static File getConfigFolder()
	{
		return configFolder;
	}
}
