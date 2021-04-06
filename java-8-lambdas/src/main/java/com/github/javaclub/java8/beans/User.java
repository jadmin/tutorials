/*
 * @(#)User.java	2018年11月6日
 *
 * Copyright (c) 2018. All Rights Reserved.
 *
 */

package com.github.javaclub.java8.beans;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * User
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: User.java 2018年11月6日 20:59:55 Exp $
 */
@Data
@ToString
@EqualsAndHashCode
public class User {

	private Long id;
	private String name;
	private String job;
	private Integer age;
	
	private String mobile;
	
	private Date topDate;
	
	public User() {
	}
	
	public User(Long id, String name, String job, int age, String mobile) {
		this.id = id;
		this.name = name;
		this.job = job;
		this.age = age;
		this.mobile = mobile;
	}
}
