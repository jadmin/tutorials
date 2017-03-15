/*
 * @(#)TimeingInvocationHandler.java	May 21, 2010
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.designpattern.structural.proxy.jdk.v1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * desc
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: TimeingInvocationHandler.java 42 2011-06-10 06:15:51Z gerald.chen.hz@gmail.com $
 */
public class TimeingInvocationHandler implements InvocationHandler {
	
	private Object proxied;   
    public TimeingInvocationHandler(Object proxied){   
        this.proxied = proxied;   
    } 

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println(method.getDeclaringClass().getName());   
        long currentTimeMillis = System.currentTimeMillis();   
        Object ret = method.invoke(proxied, args);   
        System.out.println(this.getClass().getSimpleName()+" >> wastes time : "  
                +(System.currentTimeMillis() - currentTimeMillis)+"ms");   
        return ret;
	}
}
