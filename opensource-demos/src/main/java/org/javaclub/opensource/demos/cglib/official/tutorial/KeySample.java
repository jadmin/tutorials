/*
 * @(#)KeySample.java	2011-8-31
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.cglib.official.tutorial;

import net.sf.cglib.core.KeyFactory;

/**
 * KeySample
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: KeySample.java 2011-8-31 下午08:02:31 Exp $
 */
public class KeySample {
	private interface MyFactory {
		public Object newInstance(int a, char[] b, String d);
	}

	public static void main(String[] args) {
		MyFactory f = (MyFactory) KeyFactory.create(MyFactory.class);
		Object key1 = f.newInstance(20, new char[] { 'a', 'b' }, "hello");
		Object key2 = f.newInstance(20, new char[] { 'a', 'b' }, "hello");
		Object key3 = f.newInstance(20, new char[] { 'a', '_' }, "hello");
		System.out.println(key1.equals(key2));
		System.out.println(key2.equals(key3));
	}
}
