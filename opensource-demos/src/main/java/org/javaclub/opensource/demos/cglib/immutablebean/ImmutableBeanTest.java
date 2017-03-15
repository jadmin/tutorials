/*
 * @(#)ImmutableBeanTest.java	2011-7-27
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.cglib.immutablebean;

import net.sf.cglib.beans.ImmutableBean;

import org.javaclub.opensource.demos.cglib.User;

/**
 * ImmutableBeanTest
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ImmutableBeanTest.java 2011-7-27 下午05:20:06 Exp $
 */
public class ImmutableBeanTest {

	public static void main(String[] args) {
		User user = new User(20, "Gerald");
		Object obj = ImmutableBean.create(user);

		System.out.println(obj.getClass());
	}

}
