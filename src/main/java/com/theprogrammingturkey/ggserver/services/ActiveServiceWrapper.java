package com.theprogrammingturkey.ggserver.services;

import java.io.File;

import com.theprogrammingturkey.ggserver.services.ServiceManager.ServiceStatus;

public class ActiveServiceWrapper
{
	private IServiceCore service;
	private ServiceStatus serviceStatus;
	private File serviceFile;

	public ActiveServiceWrapper(IServiceCore service, ServiceStatus serviceStatus, File serviceFile)
	{
		this.service = service;
		this.serviceStatus = serviceStatus;
		this.serviceFile = serviceFile;
	}

	public IServiceCore getService()
	{
		return service;
	}

	public ServiceStatus getServiceStatus()
	{
		return serviceStatus;
	}

	public File getServiceFile()
	{
		return serviceFile;
	}

	public String getServiceID()
	{
		return this.service.getServiceID();
	}

	public void setServiceStatus(ServiceStatus status)
	{
		this.serviceStatus = status;
	}

}
