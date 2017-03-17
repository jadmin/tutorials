/*
 * @(#)JormTest.java	2011-7-20
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.jorm.demos;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.javaclub.jorm.Jorm;
import com.github.javaclub.jorm.Session;
import com.github.javaclub.jorm.demos.entity.Book;
import com.github.javaclub.jorm.demos.entity.Category;
import com.github.javaclub.jorm.jdbc.collection.PersistentCollection;
import com.github.javaclub.toolbox.core.Numbers;
import com.github.javaclub.toolbox.core.Strings;
import com.github.javaclub.toolbox.util.DateUtil;

/**
 * JormTest
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: JormTest.java 2011-7-20 15:12:56 Exp $
 */
public class ManyToOneTest {

	static Session session;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		session = Jorm.getSession();
	}
	
	@Test
	public void testGetOne() {
		createCategory();
		
		session.clean(Book.class);
		Book book = null;
		for(int i = 0; i < 1000; i++) {
			book = new Book(Strings.fixed(10));
			book.setCategoryId(Numbers.random(28));
			book.setAuthor(Strings.fixed(3));
			book.setIsbn(Strings.fixed(3) + "-" + Strings.fixed(2) + "-" + Strings.fixed(6));
			book.setPubTime(DateUtil.randomDate("1999-01-01", "2008-01-01"));
			book.setCreateTime(DateUtil.randomDate("1999-01-01", "2008-01-01"));
			session.save(book);
			System.out.println(book.getCategory());
		}
	}

	@Test
	public void testGetBook() throws Exception {
		this.createCategory();
		
		Book book = new Book(Strings.fixed(10));
		book.setCategoryId(Numbers.random(28));
		book.setAuthor(Strings.fixed(3));
		book.setIsbn(Strings.fixed(3) + "-" + Strings.fixed(2) + "-" + Strings.fixed(6));
		book.setPubTime(DateUtil.randomDate("1999-01-01", "2008-01-01"));
		book.setCreateTime(DateUtil.randomDate("1999-01-01", "2008-01-01"));
		String id = (String) session.save(book);
		
		Book loadbook = session.read(Book.class, id);
		System.out.println(loadbook);
		System.out.println("category => " + loadbook.getCategory());
	}
	
	@Test
	public void testGetMany0() {
		Category category = null;
		category = session.read(Category.class, 1);;
		List<Book> list = category.getBooks();
		PersistentCollection pc = (PersistentCollection) list;
		System.out.println("totalBooks=" + pc.count());
		while(pc.hasNext()) {
			Book bk = (Book) pc.next();
			System.out.println(bk);
			System.out.println("cate => " + bk.getCategory());
		}
	}
	
	@Test
	public void testGetMany() {
		/*Category category = null;
		for(int i = 0; i < 28; i++) {
			category = session.read(Category.class, i + 1);;
			List<Book> list = category.getBooks(1, 20);
			for(int j = 0; j < list.size(); j++) {
				System.out.println((j + 1) + " -> " + (Book) list.get(j));
			}
		}*/
	}
	
	protected void createCategory() {
		session.clean(Category.class);
		Category category = null;
		for(int i = 0; i < 28; i++) {
			category = new Category(Strings.fixed(8));
			category.setOrder(i);
			category.setCreateTime(DateUtil.randomDate("1999-01-01", "2011-01-01"));
			session.save(category);
		}
	}
}
