/*
 * @(#)HelloWorldTest.java	2011-5-26
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.mongodb.hello;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

/**
 * MongoDB学习之HelloWorld
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: HelloWorldTest.java 37 2011-06-10 04:52:21Z gerald.chen.hz@gmail.com $
 */
public class HelloWorldTest {

	/** 数据库连接IP */
	public static final String DB_HOST = "192.168.35.101";

	/** 数据库连接端口 */
	public static final int DB_PORT = 27017;

	public static void main(String[] args) throws Exception {
		// connect to mongoDB, ip and port number
		Mongo mongo = new Mongo(DB_HOST, DB_PORT);

		// get database from MongoDB,
		// if database doesn't exists, mongoDB will create it automatically
		DB db = mongo.getDB("test_db");

		// Get collection from MongoDB, database named "yourDB"
		// if collection doesn't exists, mongoDB will create it automatically
		DBCollection collection = db.getCollection("test_collection");

		// create a document to store key and value
		BasicDBObject document = new BasicDBObject();
		document.put("id", 1001);
		document.put("message", "hello world mongoDB in Java");

		// save it into collection named "yourCollection"
		collection.insert(document);

		// search query
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("id", 1001);

		// query it
		DBCursor cursor = collection.find(searchQuery);

		// loop over the cursor and display the retrieved result
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
		System.out.println("Done");
	}
}
