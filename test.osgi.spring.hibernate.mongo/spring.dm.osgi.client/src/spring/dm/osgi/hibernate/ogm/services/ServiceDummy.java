package spring.dm.osgi.hibernate.ogm.services;

import org.springframework.stereotype.Component;

@Component("serviceDummy")
public class ServiceDummy {
	
	public String getName()
	{
		return "name dummy service";
	}

}
