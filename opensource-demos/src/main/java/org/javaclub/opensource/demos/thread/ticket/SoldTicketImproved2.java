/*
 * @(#)SoldTicketImproved2.java	2017-03-23
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread.ticket;

/**
 * SoldTicketImproved2 使用同步代码块
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: SoldTicketImproved2.java 2017-03-23 2017-03-23 22:48:27 Exp $
 */
public class SoldTicketImproved2 implements Runnable {

	private int ticket = 10;

	public SoldTicketImproved2() {
	}

	@Override
	public void run() {
		for (int i = 0; i < 20; i++) {
			synchronized (this) {
				if (this.ticket > 0) {
					// 休眠1s秒中，为了使效果更明显，否则可能出不了效果
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + "号窗口卖出：" + this.ticket-- + "号票");
				}
			}

		}
	}

	public static void main(String args[]) {
		SoldTicketImproved2 demo = new SoldTicketImproved2();
		// 基于火车票创建三个窗口
		new Thread(demo, "a").start();
		new Thread(demo, "b").start();
		new Thread(demo, "c").start();
	}

}
