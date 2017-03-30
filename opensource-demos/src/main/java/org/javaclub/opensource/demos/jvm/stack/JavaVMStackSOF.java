/*
 * @(#)JavaVMStackSOF.java	2017-03-30
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.stack;

/**
 * JavaVMStackSOF
 * 
 * VM Args: -Xss128k
 * 
 * <li> 如果线程请求的栈深度大于虚拟机所允许的最大深度，将抛出StackOverflowError
 * <li> 如果虚拟机在扩展栈时无法申请到足够的内存空间，则抛出OutOfMemoryError
 * 
 * 在单个线程下，无论是由于栈帧太大还是虚拟机栈容量太小，当内存无法分配的时候，虚拟机抛出的
 * 都是 StackOverflowError 异常
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: JavaVMStackSOF.java 2017-03-30 2017-03-30 09:58:27 Exp $
 */
public class JavaVMStackSOF {

	private int stackLength = 1;
	
	public void stackLeak() {
		stackLength++;
		stackLeak();
	}
	
	public static void main(String[] args) throws Throwable {
		JavaVMStackSOF oom = new JavaVMStackSOF();
		try {
			oom.stackLeak();
		} catch (Throwable e) {
			System.out.println("stack length: " + oom.stackLength);
			throw e;
		}
	}
}
