package org.javaclub.opensource.demos.thread;

public class QueueTest {
	public static void main(String[] args) {
		Queue q = new Queue();
		Producer p = new Producer(q);
		Consumer c = new Consumer(q);
		p.start();
		c.start();
	}
}

class Producer extends Thread {
	Queue q;

	Producer(Queue q) {
		this.q = q;
	}

	public void run() {
		for (int i = 0; i < 10; i++) {
			q.put(i);
			System.out.println("Producer put " + i);
		}
	}
}

class Consumer extends Thread {
	Queue q;

	Consumer(Queue q) {
		this.q = q;
	}

	public void run() {
		while (true) {
			System.out.println("Consumer get " + q.get());
		}
	}
}

class Queue {
	int value;
	boolean bFull = false;

	public synchronized void put(int i) {
		if (!bFull) {
			value = i;
			bFull = true;
			notify();
		}
		try {
			wait();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public synchronized int get() {
		if (!bFull) {
			try {
				wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		bFull = false;
		notify();
		return value;
	}
}