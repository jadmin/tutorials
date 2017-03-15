/*
 * @(#)DeleteDocument.java	2011-5-30
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
 * desc
 * 
 * @author <a href="mailto:hongyuan.czq@taobao.com">Gerald Chen</a>
 * @version $Id: DeleteDocument.java 37 2011-06-10 04:52:21Z gerald.chen.hz@gmail.com $
 */
public class DeleteDocument {

	public static final String DB_HOST = "192.168.35.101";

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

		// remove number = 1
		DBObject doc = collection.findOne(); // get first document
		collection.remove(doc);

		// remove number = 2
		BasicDBObject document = new BasicDBObject();
		document.put("number", 2);
		collection.remove(document);

		// remove number = 3
		collection.remove(new BasicDBObject().append("number", 3));

		// remove number > 9 , means delete number = 10
		BasicDBObject query = new BasicDBObject();
		query.put("number", new BasicDBObject("$gt", 9));
		collection.remove(query);

		// remove number = 4 and 5
		BasicDBObject query2 = new BasicDBObject();
		List<Integer> list = new ArrayList<Integer>();
		list.add(4);
		list.add(5);
		query2.put("number", new BasicDBObject("$in", list));
		collection.remove(query2);

		// remove all documents
		// DBCursor cursor = collection.find();
		// while (cursor.hasNext()) {
		// collection.remove(cursor.next());
		// }

		// remove all documents , no query means delete all
		// collection.remove(new BasicDBObject());

		// print out the document
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}

	}

}
