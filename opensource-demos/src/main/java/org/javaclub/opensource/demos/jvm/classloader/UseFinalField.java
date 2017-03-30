/*
 * @(#)UseFinalField.java	2017-03-25
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.classloader;

/**
 * Final字段不会被引起初始化
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: UseFinalField.java 2017-03-25 2017-03-25 00:42:42 Exp $
 */
public class UseFinalField {

	public static void main(String[] args) {
		System.out.println(FinalFieldClass.CONST_STR);
	}
}

class FinalFieldClass {
	public static final String CONST_STR = "CONSTSTR";

	static {
		System.out.println("FinalFieldClass init");
	}
}
