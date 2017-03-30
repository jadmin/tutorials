/*
 * @(#)RuntimeConstantPoolOOM.java	2017-03-30
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * RuntimeConstantPoolOOM
 * 
 * VM Args: -XX:PermSize=10M -XX:MaxPermSize=10M
 * 
 * 限制方法区的大小，从而间接控制其常量池的容量
 * 
 * 本例子在jdk 1.6下运行会抛出“java.lang.OutOfMemoryError: PermGen space”
 * 
 * 而在jdk 1.7下运行则不会（JDK 1.7 开始逐步 “去永久代”）
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: RuntimeConstantPoolOOM.java 2017-03-30 2017-03-30 12:59:49 Exp $
 */
public class RuntimeConstantPoolOOM {

	public static void main(String[] args) {

		// 使用list保持着常量池引用，避免Full GC回收常量池行为
		List<String> list = new ArrayList<String>();
		// 10M的PermSize在integet范围内足够产生OOM了
		int i = 0;
		while(true) {
			list.add(String.valueOf(i++).intern());
		}
	}

}
