/*
 * @(#)UpdateDocument.java	2011-5-30
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.mongodb.document;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * Java MongoDB : Update document
 *
 * @author <a href="mailto:hongyuan.czq@taobao.com">Gerald Chen</a>
 * @version $Id: UpdateDocument.java 37 2011-06-10 04:52:21Z gerald.chen.hz@gmail.com $
 */
public class UpdateDocument {
	
	/** 数据库连接IP */
	public static final String DB_HOST = "192.168.35.101";

	/** 数据库连接端口 */
	public static final int DB_PORT = 27017;

	public static void main(String[] args) throws UnknownHostException, MongoException {

		Mongo mongo = new Mongo(DB_HOST, DB_PORT);
		DB db = mongo.getDB("yourdb");
 
		// get a single collection
		DBCollection collection = db.getCollection("dummyColl");
 
		System.out.println("Testing 1...");
		insertDummyDocuments(collection);
		//find hosting = hostB, and update it with new document
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("hosting", "hostB");
		newDocument.put("type", "shared host");
		newDocument.put("clients", 111);
 
		collection.update(new BasicDBObject().append("hosting", "hostB"), newDocument);
 
		printAllDocuments(collection);
		removeAllDocuments(collection);
 
		System.out.println("Testing 2...");
		insertDummyDocuments(collection);
		//find hosting = hostB and increase its "clients" value by 99
		BasicDBObject newDocument2 = new BasicDBObject().append("$inc", 
				new BasicDBObject().append("clients", 99));
 
		collection.update(new BasicDBObject().append("hosting", "hostB"), newDocument2);
 
		printAllDocuments(collection);
		removeAllDocuments(collection);
 
		System.out.println("Testing 3...");
		insertDummyDocuments(collection);
		//find hosting = hostA and update type to from "vps" to "dedicated server"
		BasicDBObject newDocument3 = new BasicDBObject().append("$set", 
				new BasicDBObject().append("type", "dedicated server"));
 
		collection.update(new BasicDBObject().append("hosting", "hostA"), newDocument3);
		printAllDocuments(collection);
		removeAllDocuments(collection);
 
		System.out.println("Testing 4...");
		insertDummyDocuments(collection);
		//find type = vps , update all matched documents , clients value to 888
		BasicDBObject updateQuery = new BasicDBObject().append("$set", 
				new BasicDBObject().append("clients", "888"));
 
		//both method are same
		//collection.updateMulti(new BasicDBObject().append("type", "vps"), updateQuery);
 
		collection.update(
                        new BasicDBObject().append("type", "vps"), updateQuery, false, true);
 
		printAllDocuments(collection);
		removeAllDocuments(collection);

	}
	
	public static void printAllDocuments(DBCollection collection){
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
	}
 
	public static void removeAllDocuments(DBCollection collection){
		collection.remove(new BasicDBObject());
	}
 
	public static void insertDummyDocuments(DBCollection collection){
		BasicDBObject document = new BasicDBObject();
		document.put("hosting", "hostA");
		document.put("type", "vps");
		document.put("clients", 1000);
 
		BasicDBObject document2 = new BasicDBObject();
		document2.put("hosting", "hostB");
		document2.put("type", "dedicated server");
		document2.put("clients", 100);
 
		BasicDBObject document3 = new BasicDBObject();
		document3.put("hosting", "hostC");
		document3.put("type", "vps");
		document3.put("clients", 900);
 
		collection.insert(document);
		collection.insert(document2);
		collection.insert(document3);
	}


}
