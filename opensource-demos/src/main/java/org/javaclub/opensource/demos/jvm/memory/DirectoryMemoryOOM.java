/*
 * @(#)DirectoryMemoryOOM.java	2017-03-30
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.memory;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * DirectoryMemoryOOM
 * 
 * VM Args： -Xmx20M -XX:MaxDirectMemorySize=10M
 * 
 * 直接通过反射获取Unsafe实例直接进行内存分配，真正向底层操作系统申请分配内存
 * 
 * Unsafe.allocateMemory();
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: DirectoryMemoryOOM.java 2017-03-30 2017-03-30 13:52:40 Exp $
 */
public class DirectoryMemoryOOM {
	
	static final int _1MB = 1024 * 1024;

	public static void main(String[] args) throws Exception, Throwable {
		
		// 获得-XX:MaxDirectMemorySize的设置的值
		System.out.println(sun.misc.VM.maxDirectMemory());
		
		Field unsafeField = Unsafe.class.getDeclaredFields()[0];
		unsafeField.setAccessible(true);
		Unsafe unsafe = (Unsafe) unsafeField.get(null);
		while (true) {
			unsafe.allocateMemory(_1MB);
		}

	}

}
