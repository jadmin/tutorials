/*
 * @(#)MainTest.java	2012-3-20
 *
 * Copyright (c) 2012 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.algorithm;

/**
 * desc
 *
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: MainTest.java 2012-3-20 21:53:16$
 */
public class MainTest {

	public static void main(String[] args) {
		long val = HashAlgorithm.CRC32_HASH.hash("9080kjkjkty5646");
		
		System.out.println(val);
	}

}
