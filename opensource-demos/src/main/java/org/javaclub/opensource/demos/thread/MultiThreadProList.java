/*
 * @(#)MultiThreadProList.java	2017年6月19日
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * MultiThreadProList 多线程同时处理一个List
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: MultiThreadProList.java 2017年6月19日 11:44:40 Exp $
 */
public class MultiThreadProList {

	static int threadCounts = 10;// 使用的线程数
	static long sum = 0;

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ExecutorService exec = Executors.newFixedThreadPool(threadCounts);
		List<Callable<Long>> callList = new ArrayList<Callable<Long>>();

		List<Integer> list = new ArrayList<Integer>();

		for (int j = 0; j <= 1000000; j++) {
			list.add(j);
		}

		int len = list.size() / threadCounts;// 平均分割List
		// List中的数量没有线程数多（很少存在）
		if (len == 0) {
			threadCounts = list.size();// 采用一个线程处理List中的一个元素
			len = list.size() / threadCounts;// 重新平均分割List
		}
		for (int i = 0; i < threadCounts; i++) {
			final List<Integer> subList;
			if (i == threadCounts - 1) {
				subList = list.subList(i * len, list.size());
			} else {
				subList = list.subList(i * len, len * (i + 1) > list.size() ? list.size() : len * (i + 1));
			}
			// 采用匿名内部类实现
			callList.add(new Callable<Long>() {
				public Long call() throws Exception {
					long subSum = 0L;
					for (Integer i : subList) {
						subSum += i;
					}
					System.out
							.println("分配给线程：" + Thread.currentThread().getName() + "那一部分List的整数和为：\tSubSum:" + subSum);
					return subSum;
				}
			});
		}
		List<Future<Long>> futureList = exec.invokeAll(callList);
		for (Future<Long> future : futureList) {
			sum += future.get();
		}
		exec.shutdown();
		System.out.println(sum);
	}
}
