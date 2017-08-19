/*
 * @(#)BizExceptionOne.java	2017年4月19日
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.exception;

import org.javaclub.opensource.demos.abc.MessageCode;

/**
 * BizExceptionOne
 * <p>
 * 通用业务异常，不知道抛啥异常均可以抛出此异常
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: BizExceptionOne.java 2017年4月19日 上午10:16:31 Exp $
 */
public class BizException extends RuntimeException {

	private static final long serialVersionUID = 5465203973769259142L;
	
	/**
	 * 唯一标识码（快速定位问题用）
	 */
	private int code;

	public BizException() {
		super();
	}

	public BizException(int code, String msg) {
		super(msg);
		this.code = code;
	}
	
	public BizException(MessageCode mc) {
		super(mc.getMessage());
		this.code = mc.getCode();
	}

	public BizException(String msg) {
		super(msg);
	}

	public BizException(Throwable t) {
		super(t);
	}

	public BizException(String msg, Throwable t) {
		super(msg, t);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
