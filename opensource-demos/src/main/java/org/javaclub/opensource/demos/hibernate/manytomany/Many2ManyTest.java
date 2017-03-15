/*
 * @(#)Many2ManyTest.java	2011-9-13
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.hibernate.manytomany;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Many2ManyTest
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Many2ManyTest.java 2011-9-13 下午03:54:43 Exp $
 */
public class Many2ManyTest {
	
	static Session session;
	
	@BeforeClass
	public static void setUp() {
		session = HibernateUtils.getSession();
		session.beginTransaction();
	}
	
	@AfterClass
	public static void tearDown() {
		session.getTransaction().commit();
		HibernateUtils.closeSession(session);
	}

	@Test
	public void save_0() {
		Person p = new Person();
		p.setName("zhang");
        p.setAge(22);
		Address addr = null;
		for (int i = 0; i < 28; i++) {
			addr = new Address();
			addr.setAddressdetail("武汉大街" + (i + 1));
			p.getAddresses().add(addr);
		}
		session.save(p);
	}
	
	@Test
	public void load_0() {
		Person p = (Person) session.load(Person.class, 1);
		System.out.println(p.getAddresses());
	}
}
