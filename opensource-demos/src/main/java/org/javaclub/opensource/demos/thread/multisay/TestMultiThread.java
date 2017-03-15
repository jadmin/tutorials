/*
 * @(#)TestMultiThread.java	2009-9-3
 *
 * Copyright (c) 2009 by jadmin. All Rights Reserved.
 */

package org.javaclub.opensource.demos.thread.multisay;

/**
 * desc
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: TestMultiThread.java 37 2011-06-10 04:52:21Z gerald.chen.hz@gmail.com $
 */
public class TestMultiThread {

	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getName() + " 线程运行开始!");
        new MitiSay("A").start();
        new MitiSay("B").start();
        System.out.println(Thread.currentThread().getName() + " 线程运行结束!");
	}
}

class MitiSay extends Thread {
    public MitiSay(String threadName) {
        super(threadName);
    }
 
    public void run() {
        System.out.println(getName() + " 线程运行开始!");
        for (int i = 0; i < 10; i++) {
            System.out.println(i + " " + getName());
            try {
                sleep((int) Math.random() * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(getName() + " 线程运行结束!");
    }
}
