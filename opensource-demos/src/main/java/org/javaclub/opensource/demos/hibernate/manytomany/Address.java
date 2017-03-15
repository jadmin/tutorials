/*
 * @(#)Address.java	2011-9-13
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
 * @version $Id: Address.java 2011-9-13 下午02:13:11 Exp $
 */
public class Address {

	private int addressid;
    private String addressdetail;
    private Set persons = new HashSet();
    
	public Address() {
		super();
	}
	
	public int getAddressid() {
		return addressid;
	}
	public void setAddressid(int addressid) {
		this.addressid = addressid;
	}
	public String getAddressdetail() {
		return addressdetail;
	}
	public void setAddressdetail(String addressdetail) {
		this.addressdetail = addressdetail;
	}
	public Set getPersons() {
		return persons;
	}
	public void setPersons(Set persons) {
		this.persons = persons;
	}
    
    
}
