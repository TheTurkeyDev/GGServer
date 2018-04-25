package com.theprogrammingturkey.ggserver.services;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.theprogrammingturkey.ggserver.ServerCore;
import com.theprogrammingturkey.ggserver.ServerCore.Level;

public class ServiceManager
{
	private static Map<String, IServiceCore> services = new HashMap<>();
	private static Map<String, ServiceStatus> serviceStatus = new HashMap<>();
	private static Map<String, File> serviceFiles = new HashMap<>();

	private static File configFolder;

	public static void startServices()
	{
		configFolder = new File("config");

		for(File file : new File("services").listFiles())
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
				serviceStatus.put(name, ServiceStatus.STARTING);
				services.put(name, service);
				serviceFiles.put(name, file);
				ServerCore.output(Level.Info, "Pi Server", "Started Service " + service.getServiceName() + " from file " + file.getName());
				serviceStatus.put(name, ServiceStatus.RUNNING);
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
		serviceStatus.put(serviceID, ServiceStatus.STOPPING);
		try
		{
			services.get(serviceID).stop();
			IServiceCore service = services.remove(serviceID);
			serviceFiles.remove(serviceID);
			ServerCore.output(Level.Info, "Pi Server", service.getServiceName() + " Stopped.");
			serviceStatus.put(serviceID, ServiceStatus.STOPPED);
		} catch(Exception e)
		{
			serviceStatus.put(serviceID, ServiceStatus.ERRORED);
			ServerCore.output(Level.Error, "Pi Server", "Unable to stop service with the id " + serviceID + ".");
		}
	}

	public static void restartService(String serviceID)
	{
		File file = serviceFiles.get(serviceID);
		ServiceManager.stopService(serviceID);
		ServiceManager.startService(file);
	}

	public static IServiceCore getServiceFromID(String id)
	{
		return services.get(id);
	}

	public static Set<String> getServices()
	{
		return services.keySet();
	}

	public static void setServiceStatus(String serviceID, ServiceStatus status)
	{
		serviceStatus.put(serviceID, status);
	}

	public static ServiceStatus getServiceStatus(String serviceID)
	{
		return serviceStatus.get(serviceID);
	}

	private static String getFileExtension(String name)
	{
		return name.substring(name.lastIndexOf("."));
	}

	public static File getConfigFolder()
	{
		return configFolder;
	}

	public enum ServiceStatus
	{
		STOPPED, RUNNING, STOPPING, STARTING, ERRORED;
	}
}
