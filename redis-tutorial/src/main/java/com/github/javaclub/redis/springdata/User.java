/*
 * @(#)User.java	2017-03-22
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.redis.springdata;

/**
 * User
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: User.java 2017-03-22 2017-03-22 15:31:08 Exp $
 */
public class User {

	private long id;
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
