/*
 * @(#)Semaphore.java	2018年2月26日
 *
 * Copyright (c) 2018. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread;

/**
 * Semaphore
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Semaphore.java 2018年2月26日 11:41:45 Exp $
 */
public class Semaphore {

	protected long permits;

	public Semaphore(long initialPermits) {
		this.permits = initialPermits;
	}

	public void acquire() throws InterruptedException {
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
		synchronized (this) {
			try {
				while (this.permits <= 0L) {
					wait();
				}
				this.permits -= 1L;
			} catch (InterruptedException ex) {
				notify();
				throw ex;
			}
		}
	}

	public boolean attempt(long msecs) throws InterruptedException {
		if (Thread.interrupted()) {
			throw new InterruptedException();
		}
		synchronized (this) {
			if (this.permits > 0L) {
				this.permits -= 1L;
				return true;
			}
			if (msecs <= 0L) {
				return false;
			}
			try {
				long startTime = System.currentTimeMillis();
				long waitTime = msecs;
				do {
					wait(waitTime);
					if (this.permits > 0L) {
						this.permits -= 1L;
						return true;
					}
					waitTime = msecs - (System.currentTimeMillis() - startTime);
				} while (waitTime > 0L);
				return false;
			} catch (InterruptedException ex) {
				notify();
				throw ex;
			}
		}
	}

	public synchronized void release() {
		this.permits += 1L;
		notify();
	}

	public synchronized void release(long n) {
		if (n < 0L) {
			throw new IllegalArgumentException("Negative argument");
		}
		this.permits += n;
		for (long i = 0L; i < n; i += 1L) {
			notify();
		}
	}

	public synchronized long permits() {
		return this.permits;
	}
}
