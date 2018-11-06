/*
 * @(#)Apple.java	2018年11月6日
 *
 * Copyright (c) 2018. All Rights Reserved.
 *
 */

package com.github.javaclub.java8.beans;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;

/**
 * Apple
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Apple.java 2018年11月6日 23:12:25 Exp $
 */
@Data
@ToString
public class Apple {

	private Integer id;
	private String name;
	private BigDecimal money;
	private Integer num;

	public Apple(Integer id, String name, BigDecimal money, Integer num) {
		this.id = id;
		this.name = name;
		this.money = money;
		this.num = num;
	}

}
