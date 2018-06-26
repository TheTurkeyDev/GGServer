package com.theprogrammingturkey.ggserver.services;

import java.io.File;

import com.theprogrammingturkey.ggserver.services.ServiceManager.ServiceStatus;
import com.theprogrammingturkey.ggserver.ui.UICore;

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

	public File getServiceFile()
	{
		return serviceFile;
	}

	public String getServiceID()
	{
		return this.service.getServiceID();
	}

	public String getServiceName()
	{
		return this.service.getServiceName();
	}

	public String getServiceStatusString()
	{
		return serviceStatus.name();
	}

	public ServiceStatus getServiceStatus()
	{
		return serviceStatus;
	}

	public void setServiceStatus(ServiceStatus status)
	{
		UICore.updateService(this);
		this.serviceStatus = status;
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof ActiveServiceWrapper && ((ActiveServiceWrapper) o).getServiceID().equals(this.getServiceID()))
			return true;
		return false;
	}

	@Override
	public String toString()
	{
		return this.getService().getServiceName() + " (" + this.getServiceStatus().toString() + ")";
	}

}
