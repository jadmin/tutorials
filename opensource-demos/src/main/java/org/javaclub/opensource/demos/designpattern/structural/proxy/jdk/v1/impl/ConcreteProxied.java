/*
 * @(#)ConcreteProxied.java	May 21, 2010
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.designpattern.structural.proxy.jdk.v1.impl;

import org.javaclub.opensource.demos.designpattern.structural.proxy.jdk.v1.Proxied;

/**
 * 具体的代理对象实现类
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ConcreteProxied.java 42 2011-06-10 06:15:51Z gerald.chen.hz@gmail.com $
 */
public class ConcreteProxied implements Proxied {

	@Override
	public void doSomething() {
		try {   
            Thread.sleep(100);   
        } catch (InterruptedException e) {   
            System.err.println("Error : InterruptedException");   
        }   
        System.out.println(this.getClass().getSimpleName()   
                + " >> doSomething .");   
	}

	@Override
	public void doSomethingElse(String string) {
		try {   
            Thread.sleep(150);   
        } catch (InterruptedException e) {   
            System.err.println("Error : InterruptedException");   
        }   
        System.out.println(this.getClass().getSimpleName()   
                + " >> doSomethingElse , argument = " + string + ".");   

	}

}
