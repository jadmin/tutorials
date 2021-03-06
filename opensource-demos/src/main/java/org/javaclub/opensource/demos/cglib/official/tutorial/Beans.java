/*
 * @(#)Beans.java	2011-8-31
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.cglib.official.tutorial;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Beans
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Beans.java 2011-8-31 下午07:53:57 Exp $
 */
public class Beans implements MethodInterceptor {

	private PropertyChangeSupport propertySupport;

	public void addPropertyChangeListener(PropertyChangeListener listener) {

		propertySupport.addPropertyChangeListener(listener);

	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(listener);
	}

	@SuppressWarnings("unchecked")
	public static Object newInstance(Class clazz) {
		try {
			Beans interceptor = new Beans();
			Enhancer e = new Enhancer();
			e.setSuperclass(clazz);
			e.setCallback(interceptor);
			Object bean = e.create();
			interceptor.propertySupport = new PropertyChangeSupport(bean);
			return bean;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Error(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	static final Class C[] = new Class[0];
	static final Object emptyArgs[] = new Object[0];

	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		Object retValFromSuper = null;
		try {
			if (!Modifier.isAbstract(method.getModifiers())) {
				retValFromSuper = proxy.invokeSuper(obj, args);
			}
		} finally {
			String name = method.getName();
			if (name.equals("addPropertyChangeListener")) {
				addPropertyChangeListener((PropertyChangeListener) args[0]);
			} else if (name.equals("removePropertyChangeListener")) {
				removePropertyChangeListener((PropertyChangeListener) args[0]);
			}
			if (name.startsWith("set") && args.length == 1
					&& method.getReturnType() == Void.TYPE) {

				char propName[] = name.substring("set".length()).toCharArray();

				propName[0] = Character.toLowerCase(propName[0]);
				propertySupport.firePropertyChange(new String(propName), null,
						args[0]);

			}
		}
		return retValFromSuper;
	}

	public static void main(String args[]) {

		Bean bean = (Bean) newInstance(Bean.class);

		bean.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println(evt);
			}
		});

		bean.setSampleProperty("TEST");

	}

}
