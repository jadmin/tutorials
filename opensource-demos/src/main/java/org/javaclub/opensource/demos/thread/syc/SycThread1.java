/*
 * @(#)PrintNumThread.java	2010-3-18
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.thread.syc;

/**
 * <pre>
 * 在创建启动线程之前，先创建一个线程之间竞争使用的Object对象，
 * 然后将这个Object对象的引用传递给每一个线程对象的lock成员变量。
 * 这样一来，每个线程的lock成员都指向同一个Object对象。我们在run方法中，
 * 对lock对象使用synchronzied块进行局部封锁，这样就可以让线程去竞争这个唯一的共享的对象锁，从而实现同步。
 * </pre>
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: PrintNumThread.java 2010-3-18 上午01:11:51$
 */
public class SycThread1 implements Runnable {

	private int threadId;
	private Object lock;

	public SycThread1(int threadId, Object obj) {
		super();
		this.threadId = threadId;
		this.lock = obj;
	}

	public void run() {
		synchronized (lock) {
			for (int i = 0; i < 100; ++i) {
				System.out.println("Thread ID: " + this.threadId + " : " + i);
				try {
					Thread.sleep(1 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Object obj = new Object();
		for (int i = 0; i < 10; ++i) {
			new Thread(new SycThread1(i, obj)).start();
			Thread.sleep(1);
		}

	}

}
