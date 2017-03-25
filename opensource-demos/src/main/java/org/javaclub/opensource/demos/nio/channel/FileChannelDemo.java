/*
 * @(#)FileChannelDemo.java	2017-03-25
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.nio.channel;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.github.javaclub.toolbox.util.FileUtil;

/**
 * FileChannelDemo 
 * 
 * 从文件channel读取数据到缓冲区
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: FileChannelDemo.java 2017-03-25 2017-03-25 16:53:59 Exp $
 */
public class FileChannelDemo {

	public static void main(String[] args) throws Exception {
		
		RandomAccessFile aFile = new RandomAccessFile(FileUtil.getClasspathFile("file/test.txt").getAbsolutePath(), "rw");
		FileChannel inChannel = aFile.getChannel();

		/* 分配buffer */
		ByteBuffer buf = ByteBuffer.allocate(2);
		/* 读入到buffer */
		int bytesRead = inChannel.read(buf);
		while (bytesRead != -1) {
			/* 设置读 */
			buf.flip();
			/* 开始读取 */
			while (buf.hasRemaining()) {
				System.out.print((char) buf.get());
			}
			buf.clear();
			bytesRead = inChannel.read(buf);
		}

		aFile.close();
	}

}
