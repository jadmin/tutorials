/*
 * @(#)DocumentTest.java	2011-5-30
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.mongodb.document;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

/**
 * Java MongoDB : Insert a Document
 *
 * @author <a href="mailto:hongyuan.czq@taobao.com">Gerald Chen</a>
 * @version $Id: DocumentTest.java 37 2011-06-10 04:52:21Z gerald.chen.hz@gmail.com $
 */
public class DocumentTest {

	/** 数据库连接IP */
	public static final String DB_HOST = "192.168.35.101";

	/** 数据库连接端口 */
	public static final int DB_PORT = 27017;
	
	public static void main(String[] args) throws UnknownHostException, MongoException {
		Mongo mongo = new Mongo(DB_HOST, DB_PORT);
		DB db = mongo.getDB("test_db");

		// get a single collection
		DBCollection collection = db.getCollection("test_collection");

		// BasicDBObject example
		System.out.println("BasicDBObject example...");
		BasicDBObject document = new BasicDBObject();
		document.put("database", "mkyongDB");
		document.put("table", "hosting");

		BasicDBObject documentDetail = new BasicDBObject();
		documentDetail.put("records", "99");
		documentDetail.put("index", "vps_index1");
		documentDetail.put("active", "true");
		document.put("detail", documentDetail);

		collection.insert(document);

		DBCursor cursorDoc = collection.find();
		while (cursorDoc.hasNext()) {
			System.out.println(cursorDoc.next());
		}
		collection.remove(new BasicDBObject());

		// BasicDBObjectBuilder example
		System.out.println("BasicDBObjectBuilder example...");
		BasicDBObjectBuilder documentBuilder = BasicDBObjectBuilder.start()
			.add("database", "mkyongDB")
                            .add("table", "hosting");

		BasicDBObjectBuilder documentBuilderDetail = BasicDBObjectBuilder.start()
                            .add("records", "99")
                            .add("index", "vps_index1")
			.add("active", "true");

		documentBuilder.add("detail", documentBuilderDetail.get());

		collection.insert(documentBuilder.get());

		DBCursor cursorDocBuilder = collection.find();
		while (cursorDocBuilder.hasNext()) {
			System.out.println(cursorDocBuilder.next());
		}
		collection.remove(new BasicDBObject());

		// Map example
		System.out.println("Map example...");
		Map<String, Object> documentMap = new HashMap<String, Object>();
		documentMap.put("database", "mkyongDB");
		documentMap.put("table", "hosting");

		Map<String, Object> documentMapDetail = new HashMap<String, Object>();
		documentMapDetail.put("records", "99");
		documentMapDetail.put("index", "vps_index1");
		documentMapDetail.put("active", "true");

		documentMap.put("detail", documentMapDetail);

		collection.insert(new BasicDBObject(documentMap));

		DBCursor cursorDocMap = collection.find();
		while (cursorDocMap.hasNext()) {
			System.out.println(cursorDocMap.next());
		}

		collection.remove(new BasicDBObject());

		// JSON parse example
		System.out.println("JSON parse example...");

		String json = "{'database' : 'mkyongDB','table' : 'hosting'," +
		"'detail' : {'records' : 99, 'index' : 'vps_index1', 'active' : 'true'}}}";

		DBObject dbObject = (DBObject) JSON.parse(json);

		collection.insert(dbObject);

		DBCursor cursorDocJSON = collection.find();
		while (cursorDocJSON.hasNext()) {
			System.out.println(cursorDocJSON.next());
		}

		collection.remove(new BasicDBObject());

	}

}
