/*
 * @(#)Foo.java	2017-03-28
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.java8.beans;

import java.util.Objects;

/**
 * Foo
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Foo.java 2017-03-28 2017-03-28 13:12:30 Exp $
 */
public class Foo {

	private Bar bar;
	
	private Baz baz;

	public Foo(Bar bar, Baz baz) {
		this.bar = Objects.requireNonNull(bar, "bar not be null");
		this.baz = Objects.requireNonNull(baz, "bar not be null");
	}
	
}
