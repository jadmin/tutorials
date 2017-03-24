/*
 * @(#)Producer.java	2017-03-24
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread.synchlock.lock;

import java.util.concurrent.locks.Lock;

/**
 * Producer
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Producer.java 2017-03-24 2017-03-24 13:27:28 Exp $
 */
public class Producer implements Runnable {

	private Lock lock;

	public Producer(Lock lock) {
		this.lock = lock;
	}

	@Override
	public void run() {
		 int count = 10;
         while (count > 0) {
              try {
                   lock.lock();
                  count --;
                  System.out.println(this.getClass().getSimpleName() + " => A");
             } finally {
                   lock.unlock();
                   try {
                        Thread. sleep(90L);
                  } catch (InterruptedException e) {
                         // TODO Auto-generated catch block
                        e.printStackTrace();
                  }
             }
        }

	}

}
