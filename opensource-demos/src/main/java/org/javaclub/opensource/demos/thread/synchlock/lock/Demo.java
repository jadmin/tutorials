/*
 * @(#)Demo.java	2017-03-24
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread.synchlock.lock;

/**
 * Demo
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Demo.java 2017-03-24 2017-03-24 13:44:23 Exp $
 */
public class Demo {

	public static void main(String[] args) {
		Runnable t1 = new MyThread();
		new Thread(t1, "t1").start();
		new Thread(t1, "t2").start();
	}

}

class MyThread implements Runnable {

	@Override
	public void run() {
		synchronized (this) {
			for (int i = 0; i < 10; i++)
				System.out.println(Thread.currentThread().getName() + ":" + i);
		}

	}

}
