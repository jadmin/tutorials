/*
 * @(#)SimpleTest.java	2011-6-8
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.mongodb;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

/**
 * desc
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: SimpleTest.java 37 2011-06-10 04:52:21Z gerald.chen.hz@gmail.com $
 */
public class SimpleTest {
	
	/** 数据库连接IP */
	public static final String DB_HOST = "192.168.35.101";

	/** 数据库连接端口 */
	public static final int DB_PORT = 27017;

	public static void main(String[] args) throws UnknownHostException, MongoException {
		Mongo mg = new Mongo(DB_HOST, DB_PORT);
        //查询所有的Database
        for (String name : mg.getDatabaseNames()) {
            System.out.println("dbName: " + name);
        }
        
        DB db = mg.getDB("test_db");
        //查询所有的聚集集合
        for (String name : db.getCollectionNames()) {
            System.out.println("collectionName: " + name);
        }
        
        DBCollection users = db.getCollection("users");
        
        //查询所有的数据
        DBCursor cur = users.find();
        while (cur.hasNext()) {
            System.out.println(cur.next());
        }
        System.out.println(cur.count());
        System.out.println(cur.getCursorId());
        System.out.println(JSON.serialize(cur));
    }

}
