/*
 * @(#)Consumer.java	2017-03-24
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread.synchlock.lock;

import java.util.concurrent.locks.Lock;

/**
 * Consumer
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Consumer.java 2017-03-24 2017-03-24 13:26:14 Exp $
 */
public class Consumer implements Runnable {

	private Lock lock;

	public Consumer(Lock lock) {
		this.lock = lock;
	}

	@Override
	public void run() {
		int count = 10;
		while (count > 0) {
			try {
				lock.lock();
				count--;
				System.out.println(this.getClass().getSimpleName() + " => B");
			} finally {
				lock.unlock(); // 主动释放锁
				try {
					Thread.sleep(91L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
