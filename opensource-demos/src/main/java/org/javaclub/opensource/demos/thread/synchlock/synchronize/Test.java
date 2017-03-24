/*
 * @(#)Test.java	2017-03-24
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread.synchlock.synchronize;

/**
 * 两个线程依次打印"A""B",总共打印10次。
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Test.java 2017-03-24 2017-03-24 13:26:50 Exp $
 */
public class Test {

	public static final Object obj = new Object();
    
    public static void main(String[] args) {
          
           new Thread( new Producer()).start();
           new Thread( new Consumer()).start();
          
    }
}
