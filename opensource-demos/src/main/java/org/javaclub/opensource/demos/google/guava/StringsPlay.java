/*
 * @(#)StringsPlay.java	2017-03-23
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.google.guava;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import com.google.common.base.Strings;



/**
 * StringsPlay
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: StringsPlay.java 2017-03-23 2017-03-23 00:47:00 Exp $
 */
public class StringsPlay {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testPadding() {
		
		assertEquals("test", Strings.emptyToNull("test"));  
		assertEquals(" ", Strings.emptyToNull(" "));  
		assertNull(Strings.emptyToNull(""));  
		assertNull(Strings.emptyToNull(null));  
		   
		assertFalse(Strings.isNullOrEmpty("test"));  
		assertFalse(Strings.isNullOrEmpty(" "));  
		assertTrue(Strings.isNullOrEmpty(""));  
		assertTrue(Strings.isNullOrEmpty(null));  
		   
		assertEquals("test", Strings.nullToEmpty("test"));  
		assertEquals(" ", Strings.nullToEmpty(" "));  
		assertEquals("", Strings.nullToEmpty(""));  
		assertEquals("", Strings.nullToEmpty(null));  
		   
		assertEquals("Ralph_____", Strings.padEnd("Ralph", 10, '_'));  
		assertEquals("Bob_______", Strings.padEnd("Bob", 10, '_'));  
		   
		assertEquals("_____Ralph", Strings.padStart("Ralph", 10, '_'));  
		assertEquals("_______Bob", Strings.padStart("Bob", 10, '_'));  
		  
		assertEquals("xyxyxyxyxy", Strings.repeat("xy", 5));
		
		// 最长公共子串匹配
		assertEquals("2455", Strings.commonPrefix("24556", "245589"));
		assertEquals("hello", Strings.commonSuffix("ihello", "4325hello"));
		
	}

}
