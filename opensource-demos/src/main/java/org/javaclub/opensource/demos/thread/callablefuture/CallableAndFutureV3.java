/*
 * @(#)CallableAndFutureV3.java	2017-03-26
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread.callablefuture;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CallableAndFutureV3
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: CallableAndFutureV3.java 2017-03-26 2017-03-26 00:42:32 Exp $
 */
public class CallableAndFutureV3 {

	public static void main(String[] args) {
		ExecutorService threadPool = Executors.newCachedThreadPool();
		CompletionService<Integer> cs = new ExecutorCompletionService<Integer>(threadPool);
		for (int i = 1; i < 5; i++) {
			final int taskID = i;
			cs.submit(new Callable<Integer>() {
				public Integer call() throws Exception {
					return taskID;
				}
			});
		}
		// 可能做一些事情
		for (int i = 1; i < 5; i++) {
			try {
				System.out.println(cs.take().get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

}
