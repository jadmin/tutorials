/*
 * @(#)LockCode.java	2010-3-22
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.thread.thunder;

/**
 * 迅雷笔试题： 有三个线程ID分别是A、B、C,请有多线编程实现，在屏幕上循环打印10次ABCABC…
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: LockCode.java 2010-3-22 上午12:58:10$
 */
public class ThreadLoopPrint {

	public static void main(String[] args) {
		LockCode lockCode = LockCode.newInstance();// 公共锁码
		Thread ta = new Thread(new PrintRunnable('A', lockCode));
		Thread tb = new Thread(new PrintRunnable('B', lockCode));
		Thread tc = new Thread(new PrintRunnable('C', lockCode));
		ta.start();
		tb.start();
		tc.start();
	}
}

/**
 * 完成打印工作的线程类
 */
class PrintRunnable implements Runnable {
	/** 需要打印的字符 */
	private char character = '?';
	/** 公共锁码 */
	private LockCode lockCode = null;

	PrintRunnable(char c, LockCode l) {
		this.character = c;
		this.lockCode = l;
	}

	/**
	 * 线程执行
	 */
	public void run() {
		int loopCount = 1;
		while (loopCount <= 10) {
			synchronized (lockCode) {// 线程同步操作锁码
				try {
					// 如果当前运行的线程并不等于当前锁码的码值，则改线程等待
					// 比如当前运行线程是A，但是码值为B，则A线程等待。
					while (lockCode.getCode() != this.character)
						lockCode.wait();
					// 码值匹配成功，打印字符
					System.out.print(this.character);
					// 循环10次记数
					loopCount++;
					// 设置码值，让下一个线程可以运行
					lockCode.setCode();
					// 让其他所有等待线程激活
					lockCode.notifyAll();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

/**
 * 锁码：公共数据区 码值：码值为A，表示应该由A线程来执行，B,C线程等待 码值为B,C同理。
 */
class LockCode {
	/** 当前锁码码值，初始码值为A，表示最初由A线程运行 */
	private char code = 'A';

	/** 单例模式 */
	private LockCode() {
	}

	public static LockCode newInstance() {
		return new LockCode();
	}

	/**
	 * 循环设置锁码 每一次调用，锁码按照A-B-C-A-...-的顺序循环往复
	 */
	public void setCode() {
		this.code = (char) (this.code + 1);
		if (this.code == 'D')
			this.code = 'A';
	}

	/**
	 * 得到锁码
	 */
	public char getCode() {
		return this.code;
	}
}
