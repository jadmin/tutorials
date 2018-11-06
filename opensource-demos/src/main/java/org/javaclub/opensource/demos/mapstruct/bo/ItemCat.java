/*
 * @(#)ItemCat.java	2018年9月30日
 *
 * Copyright (c) 2018. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.mapstruct.bo;

/**
 * ItemCat
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ItemCat.java 2018年9月30日 11:04:49 Exp $
 */
public class ItemCat {

	private Integer catId;
	
	private String catName;
	
	private Integer level;
	
	private Integer parentCatId;
	
	private String property;
	
	public ItemCat(Integer catId, String catName, 
			Integer level, Integer parentCatId, String property) {
		super();
		this.catId = catId;
		this.catName = catName;
		this.level = level;
		this.parentCatId = parentCatId;
		this.property = property;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getParentCatId() {
		return parentCatId;
	}

	public void setParentCatId(Integer parentCatId) {
		this.parentCatId = parentCatId;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

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
}
