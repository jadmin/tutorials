/*
 * @(#)User.java	2011-7-27
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.cglib;

import java.io.Serializable;

/**
 * User
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: User.java 2011-7-27 下午05:10:43 Exp $
 */
public class User implements Serializable {

	/** desc */
	private static final long serialVersionUID = 5671256751793104201L;
	
	private int age;
	
	private String name;

	public User() {
		super();
	}

	public User(int age, String name) {
		super();
		this.age = age;
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [age=" + age + ", name=" + name + "]";
	}
	
}
