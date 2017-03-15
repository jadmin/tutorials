package org.javaclub.opensource.demos.hibernate.onetoone;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
	private static SessionFactory factory;
	static{
		try{
			Configuration cfg = new Configuration().configure("org/javaclub/opensource/demos/hibernate/onetoone/hibernate.cfg.xml");
			factory = cfg.buildSessionFactory();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	private HibernateUtils(){
		
	}
	public static SessionFactory getSessionFactory(){
		return factory;
	}
	
	public static Session getSession(){
		return factory.openSession();
	}
	
	public static void closeSession(Session session){
		if(session!=null){
			if(session.isOpen()){
				session.close();
			}
		}
	}
}
