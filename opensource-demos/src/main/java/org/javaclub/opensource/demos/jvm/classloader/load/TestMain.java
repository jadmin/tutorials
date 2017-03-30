/*
 * @(#)TestMain.java	2017-03-27
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.classloader.load;

/**
 * TestMain
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: TestMain.java 2017-03-27 2017-03-27 19:41:24 Exp $
 */
public class TestMain {

	public static void main(String[] args) throws Exception {
		ClassLoader loader = TestMain.class.getClassLoader();
		System.out.println(loader);
		// 使用ClassLoader.loadClass()来加载类，不会执行初始化块
		// loader.loadClass("org.javaclub.opensource.demos.jvm.classloader.load.ClazzDemo");
		
		// 使用Class.forName()来加载类，默认会执行初始化块
		// Class.forName("org.javaclub.opensource.demos.jvm.classloader.load.ClazzDemo");
		
		// 使用Class.forName()来加载类，并指定ClassLoader，初始化时不执行静态块
		Class.forName("org.javaclub.opensource.demos.jvm.classloader.load.ClazzDemo", false, loader);

	}

}
