/*
 * @(#)ClientTests.java	2017-03-22
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.redis.springdata;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.javaclub.redis.springdata.dao.UserDAO;

/**
 * ClientTests
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ClientTests.java 2017-03-22 2017-03-22 15:36:25 Exp $
 */
public class ClientTests {

	public static void main(String[] args) {
		
		ApplicationContext ac =  new ClassPathXmlApplicationContext("classpath:/conf/applicationContext-springdata.xml");
        UserDAO userDAO = (UserDAO) ac.getBean("userDAO");
        
        User user1 = new User();
        user1.setId(1);
        user1.setName("obama");
        userDAO.saveUser(user1);
        
        User user2 = userDAO.getUser(1);
        System.out.println(user2.getName());

	}

}
