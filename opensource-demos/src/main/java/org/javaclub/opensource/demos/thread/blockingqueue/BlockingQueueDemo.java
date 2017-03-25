/*
 * @(#)BlockingQueueDemo.java	2017-03-26
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * BlockingQueueDemo
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: BlockingQueueDemo.java 2017-03-26 2017-03-26 00:18:05 Exp $
 */
public class BlockingQueueDemo {

	public static void main(String[] args) {
		BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(1);

		Thread producerThread = new Thread(new ProducerQueue(queue));
		Thread consumerThread = new Thread(new ConsumerQueue(queue));

		producerThread.start();
		consumerThread.start();

	}

}

class ProducerQueue implements Runnable {

	private BlockingQueue<Integer> queue;

	public ProducerQueue(BlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	public void run() {
		for (int product = 1; product <= 10; product++) {
			try {
				// wait for a random time
				Thread.sleep((int) Math.random() * 3000);
				queue.put(product);
				System.out.println("put integer => " + product);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class ConsumerQueue implements Runnable {

	private BlockingQueue<Integer> queue;

	public ConsumerQueue(BlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	public void run() {
		for (int i = 1; i <= 10; i++) {
			try {
				// wait for a random time
				Thread.sleep((int) (Math.random() * 3000));
				Integer ii = queue.take();
				System.out.println("get integer => " + ii);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
