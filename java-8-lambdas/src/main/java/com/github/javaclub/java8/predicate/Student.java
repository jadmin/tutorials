/*
 * @(#)Student.java	2017-03-28
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.java8.predicate;

/**
 * Student
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Student.java 2017-03-28 2017-03-28 13:23:23 Exp $
 */
public class Student {

	String firstName;
	String lastName;
	Double grade;
	Double feeDiscount = 0.0;
	Double baseFee = 20000.0;

	public Student(String firstName, String lastName, Double grade) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.grade = grade;
	}

	public void printFee() {

		Double newFee = baseFee - ((baseFee * feeDiscount) / 100);
		System.out.println("The fee after discount: " + newFee);
	}

}
