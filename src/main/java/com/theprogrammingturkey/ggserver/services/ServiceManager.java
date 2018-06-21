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
	private static Map<String, ActiveServiceWrapper> services = new HashMap<>();

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
			if(instanceClazz instanceof IServiceCore)
			{
				IServiceCore service = (IServiceCore) instanceClazz;
				ActiveServiceWrapper wrapper = new ActiveServiceWrapper(service, ServiceStatus.STARTING, file);
				try
				{
					service.init();
					ServerCore.output(Level.Info, "Pi Server", "Started Service " + service.getServiceName() + " from file " + file.getName());
					wrapper.setServiceStatus(ServiceStatus.RUNNING);
				} catch(Exception e)
				{
					ServerCore.output(Level.Error, "Pi Server", "Service " + service.getServiceName() + " failed to start from file " + file.getName());
					ServerCore.output(Level.Error, "Pi Server", e.getMessage());
					e.printStackTrace();
					wrapper.setServiceStatus(ServiceStatus.STOPPED);
				} finally
				{
					services.put(service.getServiceID(), wrapper);
				}
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

	public static void startService(String serviceID)
	{
		if(services.containsKey(serviceID))
		{
			ActiveServiceWrapper service = services.get(serviceID);
			if(service.getServiceStatus().equals(ServiceStatus.RUNNING))
				ServiceManager.startService(service.getServiceFile());
		}
	}

	public static void stopService(String serviceID)
	{
		ActiveServiceWrapper wrapper = services.get(serviceID);
		if(wrapper != null)
		{
			ServerCore.output(Level.Info, "Pi Server", wrapper.getService().getServiceName() + " Stopped.");
			wrapper.setServiceStatus(ServiceStatus.STOPPED);
		}
		else
		{
			ServerCore.output(Level.Error, "Pi Server", "Attempted to stop a service with the id '" + serviceID + "', but it does not exsist.");
		}

	}

	public static void restartService(String serviceID)
	{
		if(services.containsKey(serviceID))
		{
			ServiceManager.stopService(serviceID);
			ServiceManager.startService(services.get(serviceID).getServiceFile());
		}
		else
		{
			ServerCore.output(Level.Error, "Pi Server", "Attempted to restart a service with the id '" + serviceID + "', but it does not exsist.");
		}
	}

	public static IServiceCore getServiceFromID(String id)
	{
		return services.get(id).getService();
	}

	public static Set<String> getServices()
	{
		return services.keySet();
	}

	public static void setServiceStatus(String serviceID, ServiceStatus status)
	{
		services.get(serviceID).setServiceStatus(status);
	}

	public static ServiceStatus getServiceStatus(String serviceID)
	{
		return services.get(serviceID).getServiceStatus();
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
