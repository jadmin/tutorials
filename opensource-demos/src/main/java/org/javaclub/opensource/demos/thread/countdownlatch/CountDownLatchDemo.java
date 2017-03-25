/*
 * @(#)CountDownLatchDemo.java	2017-03-25
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountDownLatchDemo
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: CountDownLatchDemo.java 2017-03-25 2017-03-25 13:08:43 Exp $
 */
public class CountDownLatchDemo {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		final CountDownLatch countDownLatch = new CountDownLatch(3);

		executor.execute(new Runnable() {
			public void run() {
				try {
					System.out.println("订机票");
				} finally {
					countDownLatch.countDown();
				}
			}
		});
		executor.execute(new Runnable() {
			public void run() {
				try {
					System.out.println("订巴士");
				} finally {
					countDownLatch.countDown();
				}
			}
		});
		executor.execute(new Runnable() {
			public void run() {
				try {
					System.out.println("订酒店");
				} finally {
					countDownLatch.countDown();
				}
			}
		});

		try {
			countDownLatch.await();
			System.out.println("可以出发了");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
		}

	}

}
