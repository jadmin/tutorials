/*
 * @(#)Book.java	2011-7-9
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.jorm.demos.entity;

import java.util.Date;

import com.github.javaclub.jorm.annotation.Column;
import com.github.javaclub.jorm.annotation.Entity;
import com.github.javaclub.jorm.annotation.Id;
import com.github.javaclub.jorm.annotation.ManyToOne;
import com.github.javaclub.jorm.annotation.PK;
import com.github.javaclub.jorm.annotation.Table;
import com.github.javaclub.jorm.annotation.constant.GenerationType;


/**
 * 书籍
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Book.java 105 2011-07-13 04:02:32Z gerald.chen.hz@gmail.com $
 */
@Entity(table="t_books")
@PK("id")
public class Book {

	@Id(GenerationType.IDENTITY)
	@Column("book_id")
	private String id;

	@Column("book_name")
	private String name;

	@Column("category_id")
	private Integer categoryId;
	
	@ManyToOne(selField="id", ownerField="categoryId")
	private Category category;

	@Column("isbn_no")
	private String isbn;

	@Column("pub_time")
	private Date pubTime;

	@Column("create_time")
	private Date createTime;

	@Column("book_author")
	private String author;
	
	public Book() {
		super();
	}

	public Book(String name) {
		super();
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Date getPubTime() {
		return pubTime;
	}

	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String toString() {
		return "Book [author=" + author + ", categoryId=" + categoryId
				+ ", createTime=" + createTime + ", id=" + id + ", isbn="
				+ isbn + ", name=" + name + ", pubTime=" + pubTime + "]";
	}

}
