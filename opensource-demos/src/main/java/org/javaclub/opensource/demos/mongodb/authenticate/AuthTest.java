/*
 * @(#)AuthTest.java	2011-5-27
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.mongodb.authenticate;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * Java + MongoDB in Secure Mode
 *
 * @author <a href="mailto:hongyuan.czq@taobao.com">Gerald Chen</a>
 * @version $Id: AuthTest.java 37 2011-06-10 04:52:21Z gerald.chen.hz@gmail.com $
 */
public class AuthTest {
	
	/** 数据库连接IP */
	public static final String DB_HOST = "192.168.35.101";

	/** 数据库连接端口 */
	public static final int DB_PORT = 27017;
	
	public static void main(String[] args) throws Exception, MongoException {
		Mongo mongo = new Mongo(DB_HOST, DB_PORT);
		DB db = mongo.getDB("test_db");

		boolean auth = db.authenticate("gerald", "123456".toCharArray());
		System.out.println(auth);

		DBCollection collection = db.getCollection("test_collection");
		System.out.println(collection.getFullName());

	}

}
