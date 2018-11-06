/*
 * @(#)CatMapper.java	2018年9月30日
 *
 * Copyright (c) 2018. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.mapstruct.convert;

import java.util.List;

import org.javaclub.opensource.demos.mapstruct.bo.ItemCat;
import org.javaclub.opensource.demos.mapstruct.vo.SimpleCatVO;
import org.mapstruct.Mapper;

/**
 * CatMapper
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: CatMapper.java 2018年9月30日 11:06:45 Exp $
 */
@Mapper
public interface CatMapper {
	
	SimpleCatVO toCatVO(ItemCat ic);
	
	List<SimpleCatVO> toCatList(List<ItemCat> list);
}
