/*
 * @(#)BaiduHi.java	2011-10-10
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.baidublog;

import java.util.Date;

/**
 * Baidu博客文章
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: BaiduHi.java 2011-10-10 下午08:12:24 Exp $
 */
public class BaiduHi {
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 分类
	 */
	private String categories;
	/**
	 * 发布日期
	 */
	private Date dateCreated;

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public String getCategories() {
		return categories;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String description) {
		this.content = description;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public BaiduHi(String title, String description, String categories,
			Date dateCreated) {
		this.title = title;
		this.content = description;
		this.categories = categories;
		this.dateCreated = dateCreated;
	}

	public BaiduHi() {
	}

}
