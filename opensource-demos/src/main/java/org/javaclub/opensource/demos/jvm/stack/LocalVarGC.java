/*
 * @(#)LocalVarGC.java	2017-03-25
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.stack;

/**
 * LocalVarGC
 * 
 * 打印GC信息
 *  input: -XX:+PrintGC
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: LocalVarGC.java 2017-03-25 2017-03-25 11:11:03 Exp $
 */
public class LocalVarGC {

	public void localVarGc1(){
        byte[] bytes = new byte[ 6 * 1024 * 1024];
        System.gc();
    }

    public void localVarGc2(){
        byte[] bytes = new byte[ 6 * 1024 * 1024];
        bytes = null;
        System.gc();
    }

    public void localVarGc3(){
        {
            byte[] bytes = new byte[ 6 * 1024 * 1024];
        }
        System.gc();
    }

    public void localVarGc4(){
        {
            byte[] bytes = new byte[ 6 * 1024 * 1024];
        }
        int a = 4;
        System.gc();
    }

    public void localVarGc5(){
        localVarGc1();
        System.gc();
    }

    public static void main(String[] args) {
        LocalVarGC ins = new LocalVarGC();
//      ins.localVarGc1();
//      ins.localVarGc2();
//      ins.localVarGc3();
//      ins.localVarGc4();
        ins.localVarGc5();
    }
}
