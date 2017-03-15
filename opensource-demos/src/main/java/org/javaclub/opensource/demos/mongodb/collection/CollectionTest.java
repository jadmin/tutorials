/*
 * @(#)CollectionTest.java	2011-5-30
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.mongodb.collection;

import java.net.UnknownHostException;
import java.util.Set;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * Get collection from MongoDB
 *
 * @author <a href="mailto:hongyuan.czq@taobao.com">Gerald Chen</a>
 * @version $Id: CollectionTest.java 37 2011-06-10 04:52:21Z gerald.chen.hz@gmail.com $
 */
public class CollectionTest {

	public static final String DB_HOST = "192.168.35.101";

	public static final int DB_PORT = 27017;
	
	public static void main(String[] args) throws UnknownHostException, MongoException {
		Mongo mongo = new Mongo(DB_HOST, DB_PORT);
		DB db = mongo.getDB("test_db");

		// get list of collections
		Set<String> collections = db.getCollectionNames();

		for (String collectionName : collections) {
			System.out.println(collectionName);
		}

		// get a single collection
		DBCollection collection = db.getCollection("test_collection");
		System.out.println(collection.toString());


	}

}
