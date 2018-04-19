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

	public static void startServices()
	{
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
		ServerCore.output(Level.Info, "Starting Service from file" + file.getName() + "...");
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
				String name = service.getServiceID();
				services.put(name, service);
				serviceFiles.put(name, file);
				ServerCore.output(Level.Info, "Started Service " + service.getServiceName() + " from file " + file.getName());
			}
			else
			{
				ServerCore.output(Level.Error, "Unable to start service from file " + file.getName() + "! Core class does not implement IServiceCore!");
			}

		} catch(Exception e)
		{
			ServerCore.output(Level.Error, "Unable to start service " + file.getName() + ".");
			ServerCore.output(Level.Error, e.getMessage());
		}
	}

	public static void stopService(String serviceID)
	{
		try
		{
			services.get(serviceID).stop();
			IServiceCore service = services.remove(serviceID);
			serviceFiles.remove(serviceID);
			ServerCore.output(Level.Info, service.getServiceName() + " Stopped.");
		} catch(Exception e)
		{
			ServerCore.output(Level.Error, "Unable to stop service with the id " + serviceID + ".");
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
}
