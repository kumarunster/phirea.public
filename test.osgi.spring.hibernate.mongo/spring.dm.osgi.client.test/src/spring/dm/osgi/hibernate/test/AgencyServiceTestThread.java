package spring.dm.osgi.hibernate.test;

import java.util.List;

import spring.dm.osgi.hibernate.ogm.persistence.AgencyDM;
import spring.dm.osgi.hibernate.ogm.services.AgencyService;

public class AgencyServiceTestThread extends Thread {
	
	private AgencyService agencyService;
	
	public AgencyService getAgencyService() {
		return agencyService;
	}

	public void setAgencyService(AgencyService agencyService) {
		this.agencyService = agencyService;
	}

	@Override
	public synchronized void start() {
		System.out.println("Agency Test Thread Start started");
		super.start();
	}

	@Override
	public void run() {

		System.out.println("Agency Test Thread Start RUN");
		try {
			Thread.currentThread().sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AgencyService agencyService = getAgencyService();
		
		
		AgencyDM agency = new AgencyDM();
		agency.setName("Testing Agency!");
		
		agencyService.store(agency);
		
		System.out.println("New Agency ID: " + agency.getId());
		
		List<AgencyDM> findAll = agencyService.findAll();
		
		System.out.println("found " + findAll.size() + " agencies");
	}

	@Override
	public void interrupt() {
		// TODO Auto-generated method stub
		super.interrupt();
	}
	
	

}
