/*
 * @(#)QueryDocument.java	2011-5-30
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.mongodb.document;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * Java MongoDB : Query document
 *
 * @author <a href="mailto:hongyuan.czq@taobao.com">Gerald Chen</a>
 * @version $Id: QueryDocument.java 37 2011-06-10 04:52:21Z gerald.chen.hz@gmail.com $
 */
public class QueryDocument {
	
	/** 数据库连接IP */
	public static final String DB_HOST = "192.168.35.101";

	/** 数据库连接端口 */
	public static final int DB_PORT = 27017;

	public static void main(String[] args) throws UnknownHostException, MongoException {

		Mongo mongo = new Mongo(DB_HOST, DB_PORT);
		DB db = mongo.getDB("test_db");

		// get a single collection
		DBCollection collection = db.getCollection("test_collection");

		// insert number 1 to 10 for testing
		for (int i = 1; i <= 10; i++) {
			collection.insert(new BasicDBObject().append("number", i));
		}

		// get first document
		DBObject dbObject = collection.findOne();
		System.out.println(dbObject);

		// get all available documents
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}

		// get document, where number = 5
		BasicDBObject query = new BasicDBObject();
		query.put("number", 5);
		DBCursor cursor2 = collection.find(query);
		while (cursor2.hasNext()) {
			System.out.println(cursor2.next());
		}

		// get document, where number = 9 and number = 10
		BasicDBObject query2 = new BasicDBObject();
		List<Integer> list = new ArrayList<Integer>();
		list.add(9);
		list.add(10);
		query2.put("number", new BasicDBObject("$in", list));
		DBCursor cursor3 = collection.find(query2);
		while (cursor3.hasNext()) {
			System.out.println(cursor3.next());
		}

		// get document, where number > 5
		BasicDBObject query3 = new BasicDBObject();
		query3.put("number", new BasicDBObject("$gt", 5));
		DBCursor cursor4 = collection.find(query3);
		while (cursor4.hasNext()) {
			System.out.println(cursor4.next());
		}

		// get document, where 5 < number < 8
		BasicDBObject query4 = new BasicDBObject();
		query4.put("number", new BasicDBObject("$gt", 5).append("$lt", 8));
		DBCursor cursor5 = collection.find(query4);
		while (cursor5.hasNext()) {
			System.out.println(cursor5.next());
		}

		// get document, where number != 8
		BasicDBObject query5 = new BasicDBObject();
		query5.put("number", new BasicDBObject("$ne", 8));
		DBCursor cursor6 = collection.find(query5);
		while (cursor6.hasNext()) {
			System.out.println(cursor6.next());
		}

	}

}
