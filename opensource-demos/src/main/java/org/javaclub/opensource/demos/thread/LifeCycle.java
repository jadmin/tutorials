/*
 * @(#)LifeCycle.java	2010-3-21
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.thread;

/**
 * desc
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: LifeCycle.java 2010-3-21 下午08:39:27$
 */
public class LifeCycle extends Thread {

	@Override
	public void run() {
		int n = 0;
		while ((++n) < 1000)
			;
	}
	
	public static void main(String[] args) throws Exception {
		LifeCycle thread1 = new LifeCycle();
        System.out.println("isAlive: " + thread1.isAlive());
        thread1.start();
        System.out.println("isAlive: " + thread1.isAlive());
        thread1.join();  // 等线程thread1结束后再继续执行 
        System.out.println("thread1已经结束!");
        System.out.println("isAlive: " + thread1.isAlive());

	}

}
