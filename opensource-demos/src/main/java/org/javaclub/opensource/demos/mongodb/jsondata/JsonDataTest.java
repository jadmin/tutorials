/*
 * @(#)JsonDataTest.java	2011-5-30
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.mongodb.jsondata;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

/**
 * Java MongoDB : Convert JSON data to DBObject.
 *
 * @author <a href="mailto:hongyuan.czq@taobao.com">Gerald Chen</a>
 * @version $Id: JsonDataTest.java 37 2011-06-10 04:52:21Z gerald.chen.hz@gmail.com $
 */
public class JsonDataTest {
	
	/** 数据库连接IP */
	public static final String DB_HOST = "192.168.35.101";

	/** 数据库连接端口 */
	public static final int DB_PORT = 27017;

	public static void main(String[] args) throws UnknownHostException, MongoException {

		Mongo mongo = new Mongo(DB_HOST, DB_PORT);
		DB db = mongo.getDB("test_db");
		DBCollection collection = db.getCollection("test_collection");

		// convert JSON to DBObject directly
		DBObject dbObject = (DBObject) JSON
				.parse("{'name':'mkyong', 'age':30}");

		collection.insert(dbObject);

		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}

	}

}
