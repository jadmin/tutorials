/*
 * @(#)Consumer.java	2017-03-24
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread.synchlock.synchronize;

/**
 * Consumer
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Consumer.java 2017-03-24 2017-03-24 13:26:14 Exp $
 */
public class Consumer implements Runnable {

	@Override
	public void run() {
		int count = 10;
		while (count > 0) {
			synchronized (Test.obj) {

				System.out.println(this.getClass().getSimpleName() + " => B");
				count--;
				Test.obj.notify(); // 主动释放对象锁

				try {
					Test.obj.wait();

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

}
