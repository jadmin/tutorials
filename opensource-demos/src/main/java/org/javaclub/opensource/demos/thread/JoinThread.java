/*
 * @(#)JoinThread.java	2010-2-5
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.thread;

/**
 * desc
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: JoinThread.java 2010-2-5 下午06:46:52$
 */
public class JoinThread extends Thread {
	public static int n = 0;

	static synchronized void inc() {
		n++;
	}

	public void run() {
		for (int i = 0; i < 10; i++)
			try {
				inc();
				sleep(3); // 为了使运行结果更随机，延迟3毫秒

			} catch (Exception e) {
			}
	}

	public static void main(String[] args) throws Exception {

		Thread threads[] = new Thread[100];
		for (int i = 0; i < threads.length; i++)
			// 建立100个线程
			threads[i] = new JoinThread();
		for (int i = 0; i < threads.length; i++)
			// 运行刚才建立的100个线程
			threads[i].start();
		
		for (int i = 0; i < threads.length; i++)
			// 100个线程都执行完后继续
			threads[i].join();
		System.out.println("n=" + JoinThread.n);
	}
}
