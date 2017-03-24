/*
 * @(#)Producer.java	2017-03-24
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread.synchlock.synchronize;

/**
 * Producer
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Producer.java 2017-03-24 2017-03-24 13:27:28 Exp $
 */
public class Producer implements Runnable {

	@Override
	public void run() {
		int count = 10;
		while (count > 0) {
			synchronized (Test.obj) {

				// System.out.print("count = " + count);
				System.out.println(this.getClass().getSimpleName() + " => A");
				count--;
				Test.obj.notify();

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
