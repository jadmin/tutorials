/*
 * @(#)BizObjects.java	2017年4月19日
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.abc;

import java.math.BigDecimal;

import org.javaclub.opensource.demos.exception.BizException;

/**
 * BizObjects
 * <p>
 * 主要做一些业务参数校验
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: BizObjects.java 2017年4月19日 10:09:18 Exp $
 */
public final class BizObjects {

	private BizObjects() {
        throw new AssertionError("No BizObjects instances can build for you!");
    }
	
	/**
	 * 必须非null
	 */
	public static <T> T requireNotNull(T obj, MessageCode mc) {
        if (null == obj) {
            throw new BizException(mc.getCode(), mc.getMessage());
        }
        return obj;
    }
	
	/**
	 * 字符串必须非空
	 */
	public static String requireNotEmpty(String str, MessageCode mc) {
		if(null == str || 0 >= str.trim().length()) {
			throw new BizException(mc.getCode(), mc.getMessage());
		}
		return str;
	}
	
	/**
	 * 数值类型非空且大于0
	 */
	public static Number requireNotNullGtZero(Number number, MessageCode mc) {
        if (null == number || 0 >= number.intValue()) {
            throw new BizException(mc.getCode(), mc.getMessage());
        }
        return number;
    }
	
	/**
	 * 必须为true
	 */
	public static void requireTrue(Boolean flag, MessageCode mc) {
        if (null == flag || !flag) {
            throw new BizException(mc.getCode(), mc.getMessage());
        }
    }
	
	/**
	 * 若null了，说点啥？
	 */
	public static void ifNull(Object obj, MessageCode mc) {
		if(null == obj) {
			throw new BizException(mc.getCode(), mc.getMessage());
		}
	}
	
	/**
	 * 若false了，说点啥？
	 */
	public static void ifFalse(Boolean bool, MessageCode mc) {
		if(!bool) {
			throw new BizException(mc.getCode(), mc.getMessage());
		}
	}
	
	/**
	 * 必须非null
	 */
	public static <T> T requireNotNull(T obj, String message) {
        if (null == obj) {
            throw new BizException(MessageCode.PARAMS_OBJ_IS_NULL.getCode(), message);
        }
        return obj;
    }
	
	/**
	 * 字符串必须非空
	 */
	public static String requireNotEmpty(String str, String message) {
		if(null == str || 0 >= str.trim().length()) {
			throw new BizException(MessageCode.PARAMS_STRING_SHOULD_NOT_EMPTY.getCode(), message);
		}
		return str;
	}
	
	/**
	 * 数值类型非空且大于0
	 */
	public static Number requireNotNullGtZero(Number number, String message) {
        if (null == number || new BigDecimal(0).compareTo(new BigDecimal(number.toString())) >= 0) {
            throw new BizException(MessageCode.PARAMS_NUMBER_SHOULD_POSITIVE.getCode(), message);
        }
        return number;
    }
	
	/**
	 * 必须为true
	 */
	public static void requireTrue(Boolean flag, String message) {
        if (null == flag || !flag) {
            throw new BizException(MessageCode.PARAMS_BOOL_SHOULD_TRUE.getCode(), message);
        }
    }
	
	public static <T> T present(T obj, T defaults) {
		return null == obj ? defaults : obj;
	}
	
	public static RefNode create() {
		return new RefNode();
	}
	
	static class RefNode {
		
		public RefNode() { 
		}
		
		public <T> T present(T value) {
			return value;
		}
		
		public RefNode ifTrue(boolean flag, RuntimeException e) {
			if(flag) {
				throw e;
			}
			return this;
		}
		
		public RefNode ifFalse(boolean flag, RuntimeException e) {
			if(!flag) {
				throw e;
			}
			return this;
		}
	}
	
	public static void main(String[] args) {
		int val = 188;
		int count = BizObjects.create()
							.ifFalse(val > 100, new BizException("异常错误信息"))
							.present(100);
		System.out.println(count);
	}
}
