/*
 * @(#)JavaMethodAreaOOM.java	2017-03-30
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.memory;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * JavaMethodAreaOOM
 * 
 * VM Args: -XX:PermSize=10M -XX:MaxPermSize=10M
 * 
 * 限制方法区大小，借助cglib使方法区出现内存溢出异常
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: JavaMethodAreaOOM.java 2017-03-30 2017-03-30 12:59:49 Exp $
 */
public class JavaMethodAreaOOM {

	public static void main(String[] args) {

		while(true) {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(OOMObject.class);
			enhancer.setUseCache(false);
			enhancer.setCallback(new MethodInterceptor() {
				
				@Override
				public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
					return proxy.invokeSuper(obj, args);
				}
			});
			enhancer.create();
		}
	}
	
	static class OOMObject {
		
	}

}
