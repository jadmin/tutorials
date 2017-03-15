/*
 * @(#)Functions.java	2012-3-12
 *
 * Copyright (c) 2012. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.faq;

/**
 * Functions
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Functions.java 2012-3-12 10:51:26 Exp $
 */
public class Functions {
	
	/**
	 * 计算n以内的奇数和
	 *
	 * @param n n值
	 */
	public static long sumodd(int n) {
		if(n <= 0) return 0;
		long sum = 0; 
		int max = (n % 2 == 0) ? n - 1 : n;
		for (int i = 1; i <= max; i += 2) {
			sum += i;
		}
		return sum;
	}
	
	/**
	 * 计算n以内的偶数和
	 *
	 * @param n n值
	 */
	public static long sumeven(int n) {
		if(n <= 0) return 0;
		long sum = 0; 
		int max = (n % 2 == 0) ? n : n - 1;
		for (int i = 2; i <= max; i += 2) {
			sum += i;
		}
		return sum;
	}

	public static void main(String[] args) {
		System.out.println(sumeven(9));
	}
}
