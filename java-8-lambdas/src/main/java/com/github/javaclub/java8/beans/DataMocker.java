/*
 * @(#)DataMocker.java	2018年11月6日
 *
 * Copyright (c) 2018. All Rights Reserved.
 *
 */

package com.github.javaclub.java8.beans;

import java.util.List;

import com.github.javaclub.sword.core.Numbers;
import com.github.javaclub.sword.core.Strings;
import com.google.common.collect.Lists;

/**
 * DataMocker
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: DataMocker.java 2018年11月6日 21:06:29 Exp $
 */
public abstract class DataMocker {

	public static List<User> generateUserList() {
		List<User> list = Lists.newArrayList();
		for (int i = 0; i < Numbers.random(10, 100); i++) {
			User user = new User();
			user.setId(Numbers.random(10000L, 99999L));
			user.setName("Name_" + Strings.fixed(16));
			user.setAge(Numbers.random(18, 100));
			user.setName(com.github.javaclub.sword.util.DataMocker.generateName());
			list.add(user);
		}
		return list;
	}
}
