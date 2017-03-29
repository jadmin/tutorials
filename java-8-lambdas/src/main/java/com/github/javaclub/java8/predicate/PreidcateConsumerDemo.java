/*
 * @(#)PreidcateConsumerDemo.java	2017-03-28
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.java8.predicate;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * PreidcateConsumerDemo
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: PreidcateConsumerDemo.java 2017-03-28 2017-03-28 13:24:46 Exp $
 */
public class PreidcateConsumerDemo {

	public static void main(String[] args) {
		Student student1 = new Student("Ashok","Kumar", 9.5);

	    student1 = updateStudentFee(student1,
	                                //Lambda expression for Predicate interface
	                                student -> student.grade > 8.5,
	                                //Lambda expression for Consumer inerface
	                                student -> student.feeDiscount = 30.0);
	    student1.printFee();
	    
	    System.out.println("===============================");

	    Student student2 = new Student("Rajat","Verma", 8.0);
	    student2 = updateStudentFee(student2,
	                                student -> student.grade >= 8,
	                                student -> student.feeDiscount = 20.0);

	    student2.printFee();

	}

	public static Student updateStudentFee(Student student, Predicate<Student> predicate, Consumer<Student> consumer) {

		// Use the predicate to decide when to update the discount.
		if (predicate.test(student)) {
			// Use the consumer to update the discount value.
			consumer.accept(student);
		}

		return student;
	}

}
