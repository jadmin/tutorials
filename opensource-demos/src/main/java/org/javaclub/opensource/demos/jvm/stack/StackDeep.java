/*
 * @(#)StackDeep.java	2017-03-25
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.stack;

/**
 * StackDeep
 * 
 * 堆溢出的理解
 *  input: // -Xss128K
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: StackDeep.java 2017-03-25 2017-03-25 11:11:17 Exp $
 */
public class StackDeep {

	private static int count = 0;
    public static void recursion(){
        count++;
        recursion();
    }

    public static void recursion(long a,long b,long c){
        long e=1,f=2,g=3,h=4,j=5,k=6,l=7,q=8,w=10,r=9;
        count++;
        recursion(a,b,c);
    }

    public static void main(String[] args) {
        try {
            // recursion(0L,0L,0L);
            recursion();
        } catch (Throwable e){
            System.out.println("counts = " + count);
            e.printStackTrace();
        }
    }
}
