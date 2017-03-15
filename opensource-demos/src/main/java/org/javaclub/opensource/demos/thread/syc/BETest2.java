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
public class BETest2 {

	public static void main(String[] args) {
		new Thread(new PrintCharThread('B')).start();
		new Thread(new PrintCharThread('E')).start();
	}
}

class PrintCharThread implements Runnable {

	private char ch;
	@SuppressWarnings("unused")
	private static Object lock = new Object();

	public PrintCharThread(char ch) {
		super();
		this.ch = ch;
	}

	public void run() {
		while (true) {
			try {
				if (ch == 'B') {
					this.notify();
					System.out.print('B');
					this.wait();
				} else if (ch == 'E') {
					this.notify();
					System.out.print('E');
					this.wait();
				} else {
					// 
				}
				// 暂停2秒，以便更清楚的查看打印效果
				Thread.sleep(2000);
			} catch (Exception e) {
			}
		}
	}
}
