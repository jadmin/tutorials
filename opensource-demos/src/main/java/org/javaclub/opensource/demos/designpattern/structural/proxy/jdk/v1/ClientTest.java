/*
 * @(#)ClientTest.java	May 21, 2010
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.designpattern.structural.proxy.jdk.v1;

import java.lang.reflect.Proxy;

import org.javaclub.opensource.demos.designpattern.structural.proxy.jdk.v1.impl.ConcreteProxied;

/**
 * 客户端调用例子
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ClientTest.java 46 2011-06-10 06:32:00Z gerald.chen.hz@gmail.com $
 */
public class ClientTest {

	public static void main(String[] args) {
		Proxied proxied = new ConcreteProxied();   
        proxied.doSomething();   
        proxied.doSomethingElse("only a String");   
  
        // 生成一个代理实例，这个代理实现了 Proxied 接口   
        // 对这个代理（proxy）的方法的调用 会 重定向到 TimeingInvocationHandler 的 invoke 方法   
        Proxied proxy = (Proxied) Proxy.newProxyInstance(Proxied.class  
                .getClassLoader(), // 类加载器   
                new Class[] { Proxied.class }, // 代理要实现的接口   
                new TimeingInvocationHandler(proxied) // 调用处理器   
                );   
        proxy.doSomething();   
        proxy.doSomethingElse("only a String");   
	}

}
