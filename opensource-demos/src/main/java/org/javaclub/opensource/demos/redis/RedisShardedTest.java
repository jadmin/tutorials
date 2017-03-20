/*
 * @(#)RedisShardedTest.java	2017-03-20
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.redis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * RedisShardedTest 
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: RedisShardedTest.java 2017-03-20 2017-03-20 17:54:09 Exp $
 */
public class RedisShardedTest {

	static ShardedJedis jedis;
	
	static JedisCluster cluster;
	
	static ShardedJedisPool pool;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		List<JedisShardInfo> shards = Arrays.asList(
				new JedisShardInfo("192.168.200.200", 6379),
				new JedisShardInfo("192.168.200.201", 6379), 
				new JedisShardInfo("192.168.200.202", 6379)
		);
		jedis = new ShardedJedis(shards);
		
		/*cluster = new JedisCluster(new HashSet<HostAndPort>(Arrays.asList(
				new HostAndPort("192.168.200.200", 6379),
				new HostAndPort("192.168.200.201", 6379), 
				new HostAndPort("192.168.200.202", 6379)
		)));*/
		cluster = new JedisCluster(new HostAndPort("192.168.200.202", 6379));
		
		JedisPoolConfig config =new JedisPoolConfig();//Jedis池配置
		config.setMaxTotal(500);//最大活动的对象个数
		config.setMaxIdle(1000 * 60);//对象最大空闲时间
		config.setMaxWaitMillis(1000 * 10);//获取对象时最大等待时间
		config.setTestOnBorrow(true);
		pool =new ShardedJedisPool(config, shards);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		jedis.close();
		cluster.close();
	}

	@Test
	public void testSetKv() {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			String result = cluster.set("sn" + i, "n" + i);
			System.out.println("sn" + i + " => " + result);
		}
		long end = System.currentTimeMillis();
		System.out.println("Simple@Sharing SET: " + ((end - start) / 1000.0) + " seconds");

	}
	
	@Test
	public void testPool() {
		for (int i = 0; i < 100; i++) {
			String key = generateKey();
			ShardedJedis jds = null;
			try {
				jds = pool.getResource();
				System.out.println(key + ":" + jds.getShard(key).getClient().getHost());
				System.out.println(jds.set(key, Math.random() + ""));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				pool.returnResource(jds);
			}
		}

	}
	
	 private static int index = 1;
	 public static String generateKey() {
	 	return String.valueOf(Thread.currentThread().getId())+"_"+(index++);
	 }

}
