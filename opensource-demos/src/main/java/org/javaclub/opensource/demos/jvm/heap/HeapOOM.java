/*
 * @(#)HeapOOM.java	2017-03-29
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.heap;

import java.util.ArrayList;
import java.util.List;

/**
 * HeapOOM Java堆内存溢出异常测试
 * 
 * VM Args：-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: HeapOOM.java 2017-03-29 2017-03-29 11:37:39 Exp $
 */
public class HeapOOM {
	
	static class OOMObject {
		
	}

	public static void main(String[] args) {
		List<OOMObject> list = new ArrayList<OOMObject>();
		
		int counter = 0;
		while(true) {
			list.add(new OOMObject());
			counter++;
			System.out.println("OOMObject countter = " + counter);
		}

	}

}
