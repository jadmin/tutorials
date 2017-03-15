/*
 * @(#)TimeoutException.java	2012-2-20
 *
 * Copyright (c) 2012. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.timeout;

/**
 * TimeoutException, Signals that the task timed out.
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: TimeoutException.java 1546 2012-02-20 14:22:53Z gerald.chen.hz@gmail.com $
 */
public class TimeoutException extends Exception {

	/** desc */
	private static final long serialVersionUID = -190413500090578882L;

	public TimeoutException() {
		super();
	}
	
	public TimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public TimeoutException(String message) {
		super(message);
	}

	public TimeoutException(Throwable cause) {
		super(cause);
	}

}
