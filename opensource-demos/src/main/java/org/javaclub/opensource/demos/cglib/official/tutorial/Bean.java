/*
 * @(#)Bean.java	2011-8-31
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.cglib.official.tutorial;

import java.beans.PropertyChangeListener;

/**
 * Bean
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Bean.java 2011-8-31 下午07:51:49 Exp $
 */
public abstract class Bean implements java.io.Serializable {

	private static final long serialVersionUID = -3478781414470480145L;
	
	String sampleProperty;

	public String getSampleProperty() {
		return sampleProperty;
	}

	public void setSampleProperty(String value) {
		this.sampleProperty = value;
	}
	
	public abstract void addPropertyChangeListener(PropertyChangeListener listener);

	public abstract void removePropertyChangeListener(PropertyChangeListener listener);

	public String toString() {
		return "sampleProperty is " + sampleProperty;
	}

}
