/*
 * @(#)JavaVMStackOOM.java	2017-03-30
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.stack;

/**
 * JavaVMStackOOM
 * 
 * VM Args: -Xss20M （这里为了实验效果，值不妨设置大些）
 * 
 * 这段程序的执行需要谨慎，会造成系统假死
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: JavaVMStackOOM.java 2017-03-30 2017-03-30 09:58:27 Exp $
 */
public class JavaVMStackOOM {

	
	public void stackLeakByThread() {
		while (true) {
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					dontStop();
				}
			});
			thread.start();
		}
	}
	
	private void dontStop() {
		while (true) {
		}
	}
	
	public static void main(String[] args) throws Throwable {
		JavaVMStackOOM oom = new JavaVMStackOOM();
		oom.stackLeakByThread();
	}
}
