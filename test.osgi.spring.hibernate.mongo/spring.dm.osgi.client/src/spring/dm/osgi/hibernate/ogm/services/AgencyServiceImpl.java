package spring.dm.osgi.hibernate.ogm.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.Session;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;
import org.springframework.osgi.util.OsgiBundleUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import spring.dm.osgi.hibernate.ogm.Activator;
import spring.dm.osgi.hibernate.ogm.persistence.AgencyDM;


@Component("agencyService")
public class AgencyServiceImpl implements AgencyService  {
	
	@Autowired
	EntityManagerFactory emf;
	
	@PersistenceContext(type= PersistenceContextType.EXTENDED)
	EntityManager em;
	
	@Transactional
	@Override
	public void store(AgencyDM agency)
	{
//		EntityManager em = emf.createEntityManager();
//		EntityTransaction transaction = em.getTransaction();
		
//		transaction.begin();
//		em.joinTransaction();
		
		em.persist(agency);
//		em.flush();
		
//		em.getTransaction().commit();
	}
	
	@Transactional
	@Override
	public AgencyDM find(String id)
	{
		return em.find(AgencyDM.class, id);
	}

	@Transactional
	@Override
	public void store(List<AgencyDM> agencies) {
		
		for (AgencyDM agency : agencies) {
			em.persist(agency);
		}
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	@Override
	public List<AgencyDM> findAll() {
		
		System.out.println("In FindAll");
		
		if(em == null) {
			
		Bundle bundle = FrameworkUtil.getBundle(AgencyServiceImpl.class);
		
		BundleContext context = bundle.getBundleContext();
		
		ServiceReference webContext;
		try {
			webContext = context.getAllServiceReferences("org.springframework.context.ApplicationContext", 
					"(org.springframework.context.service.name=spring.dm.osgi.client)")[0];
			ApplicationContext ac =  (ApplicationContext)context.getService(webContext) ;
			
			OsgiBundleXmlApplicationContext oac = (OsgiBundleXmlApplicationContext) ac; 
			
			System.out.println(oac);
			Object bean = oac.getBean("entityManagerFactory");
			
			System.out.println(bean);
			
			EntityManagerFactoryInfo emfInfo = (EntityManagerFactoryInfo) bean;
			emf = emfInfo.getNativeEntityManagerFactory();
			
			em = emf.createEntityManager();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		}
		
		
		Session session = (Session) em.getDelegate();
		
		List result = session.createQuery("select a from AgencyDM a").list();
		
		return result;
	}

}
