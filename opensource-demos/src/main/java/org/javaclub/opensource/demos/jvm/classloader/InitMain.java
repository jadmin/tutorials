/*
 * @(#)InitMain.java	2017-03-25
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.classloader;

/**
 * 子类的初始化过程和主动引用：
 * 子类初始化，先初始化父类
 *   input: java -XX:+TraceClassLoading 加载信息
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: InitMain.java 2017-03-25 2017-03-25 00:24:04 Exp $
 */
public class InitMain {
    public static void main(String[] args) {
        new Child();// new关键字初始化 注释开启和未开启作比较
        System.out.println("======");
        System.out.println(Child.v); // 此时Child已经被加载，但未被初始化
    }
}

class Parent {
    static {
        System.out.println("Parent init");
    }
    public static int v = 100;
}
class Child extends Parent {
    static {
        System.out.println("Child  init");
    }
}
