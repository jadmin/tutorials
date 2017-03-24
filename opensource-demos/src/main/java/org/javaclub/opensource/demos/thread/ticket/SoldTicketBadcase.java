/*
 * @(#)SoldTicketBadcase.java	2017-03-23
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread.ticket;

/**
 * SoldTicketBadcase 多线程售票的一个失败例子
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: SoldTicketBadcase.java 2017-03-23 2017-03-23 22:41:23 Exp $
 */
public class SoldTicketBadcase implements Runnable {

	private int ticket = 10; // 共有10张票

	public SoldTicketBadcase(){       
    }

	@Override
	public void run() {
		for (int i = 0; i < 20; i++) {
			if (this.ticket > 0) {
				try { // 休眠1s秒中，为了使效果更明显，否则可能出不了边界资源竞争的效果
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + "号窗口卖出：" + this.ticket-- + "号票");
			}

		}
	}

	public static void main(String args[]) {
		SoldTicketBadcase badcase = new SoldTicketBadcase();
		// 基于火车票创建三个窗口
		new Thread(badcase, "a").start();
		new Thread(badcase, "b").start();
		new Thread(badcase, "c").start();
	}

}

/*
 * 运行输出结果：
 * 
 * 
c号窗口卖出：10号票
a号窗口卖出：8号票
b号窗口卖出：9号票
c号窗口卖出：7号票
b号窗口卖出：6号票
a号窗口卖出：5号票
c号窗口卖出：4号票
b号窗口卖出：3号票
a号窗口卖出：2号票
c号窗口卖出：1号票
a号窗口卖出：0号票
b号窗口卖出：-1号票

 * 
 */
