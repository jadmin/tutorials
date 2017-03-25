/*
 * @(#)CallableAndFutureV4.java	2017-03-26
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.thread.callablefuture;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * CallableAndFutureV4
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: CallableAndFutureV4.java 2017-03-26 2017-03-26 00:45:34 Exp $
 */
public class CallableAndFutureV4 {

	public static void main(String[] args) throws Exception {
		ExecutorService threadPool = Executors.newSingleThreadExecutor();
        Future<String> future = threadPool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(200);
                return "Hello Honey";
            }
        });//提交返回的结果
        System.out.println("等待结果");
        System.out.println("拿到结果:" + future.get(1, TimeUnit.SECONDS));
        //future可以添加参数，此处超过一秒没有取到，我就不取了
 
        ExecutorService threadPool2 = Executors.newFixedThreadPool(10);
 
 
        //提交一批量的结果，然后，立刻获得先获得的结果，同时捕获。应用需要查找
        CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(threadPool2);
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            completionService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    Thread.sleep(new Random().nextInt(5000));
                    return finalI;
                }
            });
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(completionService.take().get());
        }
	}
}
