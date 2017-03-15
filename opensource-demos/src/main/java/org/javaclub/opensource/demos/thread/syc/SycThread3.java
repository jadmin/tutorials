/*
 * @(#)SycThread3.java	2010-3-18
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.thread.syc;

/**
 * <pre>
 * 实例方法中加入sychronized关键字封锁的是this对象本身，
 * 而在静态方法中加入sychronized关键字封锁的就是类本身。
 * 静态方法是所有类实例对象所共享的，因此线程对象在访问此静态方法时是互斥访问的，从而可以实现线程的同步
 * </pre>
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: SycThread3.java 2010-3-18 下午08:38:23$
 */
public class SycThread3 {

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			new Thread(new MyThread(i)).start();
			Thread.sleep(1000);
		}
	}

}

class MyThread implements Runnable {

	private int threadId;

	public MyThread(int id) {
		this.threadId = id;
	}

	public void run() {
		taskHandler(this.threadId);
	}

	private static synchronized void taskHandler(int threadId) {
		for (int i = 0; i < 100; ++i) {
			System.out.println("Thread ID: " + threadId + " : " + i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
