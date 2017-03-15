/*
 * @(#)Person.java	2011-9-13
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.hibernate.manytomany;

import java.util.HashSet;
import java.util.Set;

/**
 * 一个人可对应多个地址，一个地址也可以对应多个人。
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Person.java 2011-9-13 下午02:12:55 Exp $
 */
public class Person {

	private int personid;
    private String name;
    private int age;
    private Set addresses=new HashSet();
    
	public Person() {
		super();
	}
	
	public int getPersonid() {
		return personid;
	}
	public void setPersonid(int personid) {
		this.personid = personid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Set getAddresses() {
		return addresses;
	}
	public void setAddresses(Set addresses) {
		this.addresses = addresses;
	}
}
