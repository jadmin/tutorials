/*
 * @(#)BETest.java	2010-3-21
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.thread.syc;

/**
 * 主线程启动两个子线程，并输出BEBEBE.....的字符串。其中的两个子线程分别连续输出B和E
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: BETest.java 2010-3-21 下午11:44:16$
 */
public class BETest {
	class ThreadB implements Runnable {
		public void run() {
			while (true) {
				try {
					print("B");
				} catch (Exception e) {
				}
			}
		}
	}

	class ThreadE implements Runnable {
		public void run() {
			while (true) {
				try {
					print("E");
				} catch (Exception e) {
				}
			}
		}
	}

	private synchronized void print(String s) {
		try {
			if (s.equals("B")) {
				this.notify();
				System.out.print("B");
				this.wait();
			} else if (s.equals("E")) {
				this.notify();
				System.out.print("E");
				this.wait();
			}
			Thread.sleep(2000);
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		BETest test = new BETest();
		new Thread(test.new ThreadB()).start();
		new Thread(test.new ThreadE()).start();
	}
}
