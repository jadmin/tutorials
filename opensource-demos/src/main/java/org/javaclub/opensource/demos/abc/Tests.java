/*
 * @(#)Tests.java	2017-03-28
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.abc;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

/**
 * Tests
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Tests.java 2017-03-28 2017-03-28 13:50:26 Exp $
 */
public class Tests {

	@Test
	public void testArray() {
		String[] strArray = {"21232", "4325435", "hello"};
		System.out.println(strArray + "\n" + Arrays.asList(strArray));
	}

}
