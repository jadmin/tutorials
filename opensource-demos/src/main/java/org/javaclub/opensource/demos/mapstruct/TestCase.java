/*
 * @(#)TestCase.java	2018年9月30日
 *
 * Copyright (c) 2018. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.mapstruct;

import org.javaclub.opensource.demos.mapstruct.bo.ItemCat;
import org.javaclub.opensource.demos.mapstruct.convert.CatMapper;
import org.javaclub.opensource.demos.mapstruct.vo.SimpleCatVO;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import com.github.javaclub.toolbox.core.Numbers;
import com.github.javaclub.toolbox.core.Strings;

/**
 * TestCase
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: TestCase.java 2018年9月30日 11:01:45 Exp $
 */
public class TestCase {
	
	static CatMapper catMapperConvert = Mappers.getMapper(CatMapper.class);

	@Test
	public void testOne() {
		ItemCat ic = new ItemCat(Numbers.random(60000), 
								Strings.fixed(16), 1, 0, "cp:1;ipt:28");
		
		SimpleCatVO scv = catMapperConvert.toCatVO(ic);
		System.out.println(scv);
	}
}
