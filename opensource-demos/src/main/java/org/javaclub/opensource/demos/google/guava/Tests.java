/*
 * @(#)Tests.java	2017-03-23
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.google.guava;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.primitives.Ints;

/**
 * Tests
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Tests.java 2017-03-23 2017-03-23 02:12:42 Exp $
 */
public class Tests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testInts() {
		int[] a = new int[] { 1, 2, 3, 4, 6};
		assertTrue(Ints.contains(a, 6));
		
		int[] b = new int[] { 8, 9 ,11, 20, 28};
		int[] c = Ints.concat(a, b);
		for (int i : c) {
			System.out.println(i);
		}
		
		System.out.println("==============================");
		
		System.out.println(Ints.max(c));
		System.out.println(Ints.min(c));
	}

}
