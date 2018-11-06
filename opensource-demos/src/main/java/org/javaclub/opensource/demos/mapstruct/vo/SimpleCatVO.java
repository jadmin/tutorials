/*
 * @(#)SimpleCatVO.java	2018年9月30日
 *
 * Copyright (c) 2018. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.mapstruct.vo;

/**
 * SimpleCatVO
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: SimpleCatVO.java 2018年9月30日 上午11:03:02 Exp $
 */
public class SimpleCatVO {

	private Integer catId;
	
	private String catName;

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	@Override
	public String toString() {
		return "SimpleCatVO [catId=" + catId + ", catName=" + catName + "]";
	}
	
	
	
}
