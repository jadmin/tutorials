/*
 * @(#)JvmXmxArgs.java	2017-03-25
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.heap;

/**
 * JvmXmxArgs
 * 
 * 打印堆内存
 *  input: java -Xmx33m org.javaclub.opensource.demos.jvm.heap.JvmXmxArgs a b
 *  打印始终小于 33M , 因为GC在不同区域采用不同回收算法，可用内存的减少为了其使用空间换时间的策略。
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: JvmXmxArgs.java 2017-03-25 2017-03-25 11:02:49 Exp $
 */
public class JvmXmxArgs {
	
	public static void main(String[] args) {
        for (String arg : args) {
            System.out.println("参数为" + arg);
        }
        // 堆内存
        System.out.println("-Xmx:" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M");
    }

}
