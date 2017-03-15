/*
 * @(#)UniqueTimer.java	2012-3-8
 *
 * Copyright (c) 2012. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.unique;

import java.util.concurrent.atomic.AtomicLong;

/**
 * UniqueTimer
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: UniqueTimer.java 2012-3-8 10:17:54 Exp $
 */
public class UniqueTimer {

	private AtomicLong timeMillis = new AtomicLong(System.currentTimeMillis());

	public long getCurrentTime() {

		return timeMillis.incrementAndGet();
	}
	
	public static void main(String[] args) {
		UniqueTimer unique = new UniqueTimer();
		
		for (int i = 0; i < 100000; i++) {
			System.out.println(unique.getCurrentTime());
		}
	}
}
