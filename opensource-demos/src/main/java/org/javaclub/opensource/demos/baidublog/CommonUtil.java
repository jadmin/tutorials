/*
 * @(#)CommonUtil.java	2011-10-11
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.baidublog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * CommonUtil
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: CommonUtil.java 2011-10-11 下午12:34:54 Exp $
 */
public abstract class CommonUtil {
	
	protected static final Log LOG = LogFactory.getLog(BaidublogCollector.class);

	/**
	 * Reads this input stream and returns contents as a byte[]
	 */
	public static byte[] readAsByteArray(InputStream inStream) throws IOException {
		int size = 1024;
		byte[] ba = new byte[size];
		int readSoFar = 0;

		while (true) {
			int nRead = inStream.read(ba, readSoFar, size - readSoFar);
			if (nRead == -1)
				break;
			readSoFar += nRead;
			if (readSoFar == size) {
				int newSize = size * 2;
				byte[] newBa = new byte[newSize];
				System.arraycopy(ba, 0, newBa, 0, size);
				ba = newBa;
				size = newSize;
			}
		}

		byte[] newBa = new byte[readSoFar];
		System.arraycopy(ba, 0, newBa, 0, readSoFar);
		return newBa;
	}
	
	/**
	 * Returns the contents of this file as a String
	 */
	public static String readAsString(File file, String charset) throws IOException {
		String encoding = "UTF-8";
		if (null != charset && charset.length() != 0) {
			encoding = charset;
		}
		BufferedReader r = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), encoding));
		StringBuffer b = new StringBuffer();
		while (true) {
			int ch = r.read();
			if (ch == -1)
				break;
			b.append((char) ch);
		}
		r.close();
		return b.toString();
	}
	
	public static void writeText(String filePath, String txt, boolean append) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(filePath, append), true);
			pw.println(txt);
		} catch (Exception e) {
			if(LOG.isWarnEnabled()) {
				LOG.warn("error occured while writing file.", e);
			}
		} finally {
			if(null != pw) {
				pw.close();
			}
		}
	}
	
	public static String time(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}
}
