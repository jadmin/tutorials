/*
 * @(#)PrintNumThread.java	2010-3-18
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.thread.syc;

/**
 * 利用类成员变量被所有类的实例所共享这一特性，因此可以将lock用静态成员对象来实现
 *
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: PrintNumThread.java 2010-3-18 上午01:11:51$
 */
public class SycThread2 implements Runnable {
	
	private int threadId;
    private static Object lock = new Object();

	public SycThread2(int threadId) {
		super();
		this.threadId = threadId;
	}

	public void run() {
		synchronized(lock)
        {
            for (int i = 0; i < 100; ++i)
            {
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
		for (int i = 0; i < 10; ++i)
        {
            new Thread(new SycThread2(i)).start();
            Thread.sleep(1);
        }


	}

}
