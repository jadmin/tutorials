/*
 * @(#)DemoTests.java	2017-03-28
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.java8.somehowuse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.javaclub.java8.beans.Apple;
import com.github.javaclub.java8.beans.DataMocker;
import com.github.javaclub.java8.beans.User;
import com.github.javaclub.sword.core.Numbers;
import com.github.javaclub.sword.core.Strings;
import com.google.common.collect.Lists;

/**
 * DemoTests
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: DemoTests.java 2017-03-28 2017-03-28 13:17:15 Exp $
 */
public class DemoTests {

	static List<Apple> appleList = new ArrayList<>();
	
	static List<User> userList = Lists.newArrayList();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Apple apple1 = new Apple(1, "苹果1", new BigDecimal("3.25"), 10);
		Apple apple12 = new Apple(1, "苹果2", new BigDecimal("1.35"), 20);
		Apple apple2 = new Apple(2, "香蕉", new BigDecimal("2.89"), 30);
		Apple apple3 = new Apple(3, "荔枝", new BigDecimal("9.99"), 40);

		appleList.add(apple1);
		appleList.add(apple12);
		appleList.add(apple2);
		appleList.add(apple3);
		
		User u1 = new User(Numbers.random(999999L), Strings.random(20), "", Numbers.random(18, 88), "15968870949");
		User u2 = new User(Numbers.random(999999L), Strings.random(20), "", Numbers.random(18, 88), "15968870949");
		User u3 = new User(Numbers.random(999999L), Strings.random(20), "", Numbers.random(18, 88), "13688241388");
		User u4 = new User(Numbers.random(999999L), Strings.random(20), "", Numbers.random(18, 88), "13688241388");
		userList.add(u1);
		userList.add(u2);
		userList.add(u3);
		userList.add(u4);
	}
	
	/**
	 * 针对List集合中对象的指定字段进行去重
	 */
	@Test
	public void testRemoveDup() {
		List<User> list = userList.stream().collect(Collectors.collectingAndThen(
				Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(User::getMobile))), ArrayList::new));
		System.out.println(list);
	}
	
	@Test
	public void testChangeBeanProp() { // 改变实体中的属性
		userList.forEach(e -> {
			e.setAge(Numbers.random(10, 99));
			e.setName(com.github.javaclub.sword.util.DataMocker.generateName());
		});
		System.out.println(userList);
		
		Long i = 20190305111031128L;
	}
	
	@Test
	public void beansPropAsMap() { // 收集实体的属性作为map
		List<User> list = DataMocker.generateUserList();
		Map<Long, String> map = list.stream().collect(Collectors.toMap(User::getId, User::getName));
		System.out.println(map);
	}
	
	@Test
	public void beansAsMap() { // 收集成实体本身map
		List<User> list = DataMocker.generateUserList();
		Map<Long, User> map = list.stream().collect(Collectors.toMap(User::getId, user -> user));
		System.out.println(map);
	}
	
	@Test
	public void beansAsMap_V2() { // 收集成实体本身map
		List<User> list = DataMocker.generateUserList();
		Map<Long, User> map = list.stream().collect(Collectors.toMap(User::getId, Function.identity()));
		System.out.println(map);
	}

	/**
	 * 分组 List里面的对象元素，以某个属性来分组，例如，以id分组，将id相同的放在一起：
	 */
	@Test
	public void testGroupBy() {
		// List 以ID分组 Map<Integer,List<Apple>>
		Map<Integer, List<Apple>> groupBy = appleList.stream().collect(Collectors.groupingBy(Apple::getId));
		System.out.println(groupBy);
	}

	/**
	 * List -> Map 需要注意的是： toMap 如果集合对象有重复的key，会报错Duplicate key ....
	 * apple1,apple12的id都为1。 可以用 (k1,k2)->k1 来设置，如果有重复的key,则保留key1,舍弃key2
	 */
	@Test
	public void testAsMap() {
		Map<Integer, Apple> appleMap = appleList.stream()
				.collect(Collectors.toMap(Apple::getId, Function.identity(), (k1, k2) -> k1));
		System.out.println(appleMap);
	}

	/**
	 * 从集合中过滤出来符合条件的元素
	 */
	@Test
	public void testFilter() {
		List<Apple> filterList = appleList.stream().filter(a -> a.getName().equals("香蕉")).collect(Collectors.toList());

		System.out.println("filterList:" + filterList);
	}

	/**
	 * 将集合中的数据按照某个属性求和
	 */
	@Test
	public void testSum() {
		// 计算 总金额
		BigDecimal totalMoney = appleList.stream().map(Apple::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
		System.out.println("totalMoney = " + totalMoney); // totalMoney:17.48
	}


}
