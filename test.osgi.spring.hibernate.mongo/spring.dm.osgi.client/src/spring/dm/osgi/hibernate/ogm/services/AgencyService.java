package spring.dm.osgi.hibernate.ogm.services;

import java.util.List;

import spring.dm.osgi.hibernate.ogm.persistence.AgencyDM;

public interface AgencyService {

	public void store(AgencyDM agency);

	public AgencyDM find(String id);

	public void store(List<AgencyDM> agencies);

	public List<AgencyDM> findAll();

}
