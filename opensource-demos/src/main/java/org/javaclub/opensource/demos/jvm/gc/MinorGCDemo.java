/*
 * @(#)MinorGCDemo.java	2017-03-30
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.gc;

/**
 * MinorGCDemo
 * 
 * 演示新生代GC minor  gc
 * 
 * VM Args:  -Xmx20m -Xms20m -Xmn10m -XX:PrintGCDetails -XX:SurvivorRatio=8
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: MinorGCDemo.java 2017-03-30 2017-03-30 16:03:45 Exp $
 */
public class MinorGCDemo {

	private static final int _1MB = 1024 * 1024;

	public static void main(String[] args) {
		testAllocation();
	}

	private static void testAllocation() {
		byte[] allocation1, allocation2, allocation3, allocation4;
		allocation1 = new byte[2 * _1MB];
		allocation2 = new byte[2 * _1MB];
		allocation3 = new byte[2 * _1MB];
		allocation4 = new byte[4 * _1MB]; // 发生一次minor gc
	}

}
