/*
 * @(#)MapsPlay.java	2017-03-23
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.google.guava;

import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

/**
 * MapsPlay
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: MapsPlay.java 2017-03-23 2017-03-23 01:29:40 Exp $
 */
public class MapsPlay {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		Map<String, String> map = Maps.newHashMap();
		map.put("1", "1");
		map.put("2", "2");
		map.put("3", "3");
		map.put("4", "4");
		System.out.println(map);
	}
	
	/**
	 * 创建一键多值的map
	 */
	@Test
	public void testMultiMap() {
		Multimap<String, Integer> multimap = ArrayListMultimap.create();
		multimap.put("Kate", 20);
		multimap.put("Kate", 21);
		multimap.put("Kate", 28);
		multimap.put("Jack", 100);
		multimap.put("Jack", 128);
		multimap.put("Tom", 58);
		
		List<Integer> kate  = (List<Integer>) multimap.get("Kate");
		System.out.println(kate);
		
		List<Integer> jack  = (List<Integer>) multimap.get("Jack");
		System.out.println(jack);
		
		List<Integer> tom  = (List<Integer>) multimap.get("Tom");
		System.out.println(tom);
		
		System.out.println(multimap.asMap());
	}
	
	@Test
	public void testDiffrence() {
		Map<String, Long> map1 = Maps.newHashMap();
		map1.put("Kate", 15L);
		map1.put("Jack", 18L);
		
		Map<String, Long> map2 = Maps.newHashMap();
		map1.put("Kate", 12L);
		map1.put("Jack", 18L);
		map1.put("Tome", 16L);
		
		MapDifference<String, Long> diff = Maps.difference(map1, map2);
		System.out.println(diff);
	}
	
	/**
	 * 不可变map创建
	 *
	 */
	@Test(expected=java.lang.UnsupportedOperationException.class)
	public void testImmutable() {
		ImmutableMap<String, String> a = ImmutableMap.of("1", "map1", "2", "map2");
		a.put("1", "ook");
		System.out.println(a);
	}
	
	/**
	 * 双向map
	 */
	@Test
	public void testBiMap() {
		BiMap<Integer,String> map = HashBiMap.create();
		
		map.put(20, "kate");
		map.put(21, "tom");
		map.put(80, "jack");
		
		Assert.assertTrue("jack".equals(map.get(80)));
		Assert.assertTrue(21 == map.inverse().get("tom"));
		
		BiMap<String,Integer> inverseMap = map.inverse();
		System.out.println(inverseMap);
		
		System.out.println(map);
	}

}
