package spring.dm.osgi.hibernate.test;

import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import spring.dm.osgi.hibernate.ogm.persistence.AgencyDM;
import spring.dm.osgi.hibernate.ogm.services.AgencyService;
import spring.dm.osgi.hibernate.ogm.services.AgencyServiceImpl;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

//		Thread thread = new AgencyServiceTestThread();
//		
//		thread.start();
		
//		ServiceReference<AgencyService> serviceReference = bundleContext.getServiceReference(AgencyService.class);
//		System.out.println(serviceReference);
//		
//		AgencyService agencyService = bundleContext.getService(serviceReference);
//		System.out.println(agencyService);
//		
//		List<AgencyDM> findAll = agencyService.findAll();
//		
//		System.out.println("Find All in Activator: " + findAll.size());
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
