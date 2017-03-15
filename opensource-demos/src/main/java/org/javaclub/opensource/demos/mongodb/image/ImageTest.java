/*
 * @(#)ImageTest.java	2011-5-30
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.mongodb.image;

import java.io.File;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * Java MongoDB : Save image example
 *
 * @author <a href="mailto:hongyuan.czq@taobao.com">Gerald Chen</a>
 * @version $Id: ImageTest.java 37 2011-06-10 04:52:21Z gerald.chen.hz@gmail.com $
 */
public class ImageTest {
	
	/** 数据库连接IP */
	public static final String DB_HOST = "192.168.35.101";

	/** 数据库连接端口 */
	public static final int DB_PORT = 27017;

	public static void main(String[] args) throws Exception {
		
		Mongo mongo = new Mongo(DB_HOST, DB_PORT);
		DB db = mongo.getDB("test_db");
		DBCollection collection = db.getCollection("test_collection");
		System.out.println(collection);

		String newFileName = "mkyong-java-image";

		File imageFile = new File("c:\\JavaWebHosting.png");

		// create a "photo" namespace
		GridFS gfsPhoto = new GridFS(db, "photo");

		// get image file from local drive
		GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);

		// set a new filename for identify purpose
		gfsFile.setFilename(newFileName);

		// save the image file into mongoDB
		gfsFile.save();

		// print the result
		DBCursor cursor = gfsPhoto.getFileList();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}

		// get image file by it's filename
		GridFSDBFile imageForOutput = gfsPhoto.findOne(newFileName);

		// save it into a new image file
		imageForOutput.writeTo("c:\\JavaWebHostingNew.png");

		// remove the image file from mongoDB
		gfsPhoto.remove(gfsPhoto.findOne(newFileName));
	}

}
