/*
 * @(#)Category.java	2011-7-9
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.jorm.demos.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.javaclub.jorm.annotation.Column;
import com.github.javaclub.jorm.annotation.Entity;
import com.github.javaclub.jorm.annotation.Id;
import com.github.javaclub.jorm.annotation.OneToMany;
import com.github.javaclub.jorm.annotation.PK;
import com.github.javaclub.jorm.annotation.Table;

/**
 * 目录
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Category.java 105 2011-07-13 04:02:32Z gerald.chen.hz@gmail.com $
 */
@Entity(table="t_book_category")
@PK(value="id")
public class Category {

	@Id
	private int id;
	
	@Column("category_name")
	private String name;
	
	@Column("r_order")
	private int order;
	
	@Column("create_time")
	private Date createTime;
	
	@OneToMany(type = Book.class, selField="categoryId", ownerField="id", cascade=false)
	private List<Book> books = new ArrayList<Book>();
	
	public Category() {
		super();
	}

	public Category(String name) {
		super();
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public String toString() {
		return "Category [createTime=" + createTime + ", id=" + id + ", name="
				+ name + ", order=" + order + "]";
	}
	
}
