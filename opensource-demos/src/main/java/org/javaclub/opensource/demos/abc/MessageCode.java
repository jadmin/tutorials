/*
 * @(#)MessageCode.java	2017年4月19日
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.abc;

import java.util.HashMap;
import java.util.Map;

/**
 * MessageCode
 * <p>
 * 提示信息&特征码定义
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: MessageCode.java 2017年4月19日 16:25:36 Exp $
 */
public enum MessageCode {
	
	FATAL(-999, "系统异常，请稍后再试"),
	
	/**
	 * 成功 
	 */
	DEFAULT_SUCCESS(1, "成功"),
	
	
	/**
	 * 入口参数数据有误
	 */
	PARAMS_ERROR(1000, "入参数据格式错误"),
	
	/**
	 * 入参对象不可为空 
	 */
	PARAMS_OBJ_IS_NULL(1001, "入参对象不可为空"),
	
	/**
	 * 数值对象不可为空且必须为正数
	 */
	PARAMS_NUMBER_SHOULD_POSITIVE(1002, "数值对象不可为空且必须为正数"),
	
	/**
	 * 布尔参数数据必须为true 
	 */
	PARAMS_BOOL_SHOULD_TRUE(1003, "布尔参数数据必须为true"),
	
	/**
	 * String参数数据不能为空
	 */
	PARAMS_STRING_SHOULD_NOT_EMPTY(1004, "String参数数据不能为空"),
	
    ;

	private static Map<Integer, String> mapValues;
	
	/**
	 * 唯一特征码标识
	 */
	private int 	code;
	
	/**
	 * 提示信息 
	 */
	private String 	message;
	
	private MessageCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
    public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public static Map<Integer, String> map() {
		if(null == mapValues) {
			mapValues = new HashMap<Integer, String>();
			for (MessageCode mc : MessageCode.values()) {
				mapValues.put(mc.getCode(), mc.getMessage());
			}
		}
		
		return mapValues;
	}
	
}
