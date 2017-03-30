/*
 * @(#)ClassLoaderInfo.java	2017-03-25
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.classloader;

/**
 * ClassLoaderInfo
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ClassLoaderInfo.java 2017-03-25 2017-03-25 00:19:51 Exp $
 */
public class ClassLoaderInfo {

	public static void main(String[] args) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        System.out.println("当前类加载器：" + loader);
        System.out.println("当前类的父亲加载器（根加载器）：" + loader.getParent());
        System.out.println("当前类父亲的父亲加载器（无）：" + loader.getParent().getParent());
    }
}
