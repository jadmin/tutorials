/*
 * @(#)FilesConverter.java	2011-9-28
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.yk;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FilesConverter {
	
	public static void main(String[] args) throws IOException {
		String dir = "D:/workspace/php/class";
		
		doConvert(dir, "GBK", "UTF-8");
	}
	
	static String LS = System.getProperty("line.separator", "\r\n");

	/**
	 * 将目录(包括其子目录)下的所有文件从一种编码转换到另外一种编码
	 * 
	 * @param dir 要转换的目录
	 * @throws IOException 
	 */
	public static void doConvert(String dir, String from , String to) throws IOException {
		File directory = new File(dir);
		if ((!directory.exists()) || (!directory.isDirectory())) {
			throw new IllegalArgumentException("The input => " + dir
					+ " must a directory.");
		}
		File[] rootFiles = directory.listFiles();
		String filepath = null;
		for (int i = 0; i < rootFiles.length; i++) {
			File file = rootFiles[i];
			if (file.isDirectory()) {
				doConvert(file.getAbsolutePath(), from, to);
			} else {
				if("jpg".equalsIgnoreCase(getExt(file)) 
					|| "gif".equalsIgnoreCase(getExt(file)) 
					|| "mdb".equalsIgnoreCase(getExt(file))) {
					continue;
				}
				
				filepath = file.getAbsolutePath();
				FileInputStream fis = new FileInputStream(file);
				DataInputStream in = new DataInputStream(fis);
				BufferedReader d = new BufferedReader(new InputStreamReader(in, from));
				String line = null;
				StringBuffer content = new StringBuffer();
				while ((line = d.readLine()) != null) {
					content.append(line + LS);
				}
				d.close();
				in.close();
				fis.close();
				// 确保源文件被删除掉
				for (int j = 0; j < 3; j++) {
					file.delete();
				}
				
				Writer ow = new OutputStreamWriter(new FileOutputStream(filepath), to);
				ow.write(content.toString());
				ow.close();
				
			}
		}

	}

	public static String getExt(File file) {
		if (!file.exists()) {
			throw new IllegalArgumentException("The file [" + file
					+ "] doesn't exist...");
		}
		String name = file.getName();
		int index = name.lastIndexOf(".");
		if (index < 0) {
			return null;
		}
		return name.substring(index + 1);
	}
}
