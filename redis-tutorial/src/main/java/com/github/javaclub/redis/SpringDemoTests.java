/*
 * @(#)SpringDemoTests.java	2017-03-22
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.redis;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * SpringDemoTests
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: SpringDemoTests.java 2017-03-22 2017-03-22 14:53:03 Exp $
 */
public class SpringDemoTests {

	static ApplicationContext context;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		context = new ClassPathXmlApplicationContext("conf/applicationContext.xml");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testSpringContext() throws Exception {
		JedisPool pool = (JedisPool) context.getBean("jedisPool");
		Jedis jedis = null;
		try {
			jedis = pool.getResource();

			jedis.set("name", "lisi");
			String name = jedis.get("name");
			System.out.println(name);
		} finally {
			if (jedis != null) {
				// 关闭连接
				jedis.close();
			}
		}
	}

	@Test
	public void testJedisSingle() {

		Jedis jedis = new Jedis("192.168.56.111", 6379);
		jedis.auth("admin");

		jedis.set("name", "World");
		String name = jedis.get("name");
		System.out.println(name);
		jedis.close();

	}

}
