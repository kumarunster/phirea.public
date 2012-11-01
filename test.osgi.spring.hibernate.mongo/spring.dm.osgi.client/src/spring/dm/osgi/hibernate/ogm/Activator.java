package spring.dm.osgi.hibernate.ogm;

import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import spring.dm.osgi.hibernate.ogm.persistence.AgencyDM;
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
		
		System.out.println("Started!");
		
//		Runnable testExecutor = new Runnable() {
//
//			@Override
//			public void run() {
//				
//				try {
//					Thread.currentThread().sleep(10000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				AgencyService agencyService = new AgencyService();
//				
//				List<Agency> findAll = agencyService.findAll();
//				
//				System.out.println("found " + findAll.size() + " agencies");
//			}
//			
//		};
//		
//		Thread thread = new Thread(testExecutor);
//		
//		thread.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
