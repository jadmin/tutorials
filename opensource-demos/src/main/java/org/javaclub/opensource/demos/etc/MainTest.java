/*
 * @(#)MainTest.java	2012-3-22
 *
 * Copyright (c) 2012 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.etc;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * desc
 *
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: MainTest.java 2012-3-22 21:48:52$
 */
public class MainTest {
	
	private static Map<String, String> paramMap = new HashMap<String, String>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		paramMap.put("${username}", "王海");
		paramMap.put("${kkk}", " 哈哈 ");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testReplace() throws Exception {
		File file = new File("E:/workspace/java/opensource/javaclub/opensource-demos/src/main/resources/file/sms.tpl.txt");
		String text = FileUtil.readAsString(file, "UTF-8");
		System.out.println(text);
		text = getReplacedText(text, paramMap);
		System.out.println(text);
	}
	
	protected String getReplacedText(final String text, Map<String, String> paraMap) {
		String result = text;
		Map.Entry<String, String> entry = null;
		for (Map.Entry<String, String> e : paraMap.entrySet()) {
			entry = e;
			result = result.replace((String) entry.getKey(), (String) entry.getValue());
		}
		return result;
	}
}
