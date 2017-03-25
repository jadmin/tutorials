/*
 * @(#)PrimeNumberDemo.java	2017-03-26
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread.callablefuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * PrimeNumberDemo
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: PrimeNumberDemo.java 2017-03-26 2017-03-26 00:24:23 Exp $
 */
public class PrimeNumberDemo {

	public static void main(String[] args) {
		Callable<int[]> primeCallable = new PrimeCallable(1000);
		FutureTask<int[]> primeTask = new FutureTask<int[]>(primeCallable);

		Thread t = new Thread(primeTask);
		t.start();

		try {
			// 假設現在做其它事情
			Thread.sleep(5000);

			// 回來看看質數找好了嗎
			if (primeTask.isDone()) {
				int[] primes = primeTask.get();
				for (int prime : primes) {
					System.out.print(prime + " ");
				}
				System.out.println();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

}

class PrimeCallable implements Callable<int[]> {
	private int max;

	public PrimeCallable(int max) {
		this.max = max;
	}

	public int[] call() throws Exception {
		int[] prime = new int[max + 1];

		List<Integer> list = new ArrayList<Integer>();

		for (int i = 2; i <= max; i++)
			prime[i] = 1;

		for (int i = 2; i * i <= max; i++) { // 這邊可以改進
			if (prime[i] == 1) {
				for (int j = 2 * i; j <= max; j++) {
					if (j % i == 0)
						prime[j] = 0;
				}
			}
		}

		for (int i = 2; i < max; i++) {
			if (prime[i] == 1) {
				list.add(i);
			}
		}

		int[] p = new int[list.size()];
		for (int i = 0; i < p.length; i++) {
			p[i] = list.get(i).intValue();
		}

		return p;
	}
}
