/*
 * @(#)FileTool.java  2009-2-19
 *
 * Copyright (c) 2009 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.etc;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 文件操作处理辅助工具.
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 */
public class FileUtil {

	/** 拷贝文件时，一次读取流的字节数8KB */
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 8;

	private static final String DEFAULT_ENCODING = "UTF-8";

	/** 获取系统环境的换行符号，在Windows下是"\r\n"，linux下是"\n" */
	public static final String LS = System.getProperty("line.separator");

	/** File.separator */
	public static final String FS = File.separator;
	
	/** The first filename suffix index */
	public static long fileCode = 0;

	protected static final Log LOG = LogFactory.getLog(FileUtil.class);

	private FileUtil() {

	}

	/**
	 * 拷贝文件,并得到拷贝文件的byte大小<br>
	 * 如果文件比较大，请使用copyLarge方法，注意在调用处将流对象关闭
	 * 
	 * @param input
	 * @param output
	 * @return 被拷贝文件的byte大小，如果文件超过2G,将返回-1
	 * @throws IOException
	 * @time 2008-12-4 上午09:53:00
	 */
	public static int copyFile(InputStream input, OutputStream output)
			throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	/**
	 * 拷贝超大文件，注意在调用处将流对象关闭
	 * 
	 * @param input
	 * @param output
	 * @return 拷贝文件的byte数
	 * @throws IOException
	 * @time 2008-12-4 上午09:58:20
	 */
	public static long copyLarge(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * Copy file, using as flowling:
	 * 
	 * <pre>
	 * OutputStream out = new FileOutputStream(&quot;D:/kk.zip&quot;);
	 * String path = &quot;C:/test.zip&quot;;
	 * try {
	 * 	FileHelper.copyFile(path, out);
	 * } catch (IOException ioe) {
	 * 	// do something
	 * } finally {
	 * 	FileHelper.close(out);
	 * }
	 * </pre>
	 * 
	 * @param src
	 * @param out
	 * @throws IOException
	 */
	public static void copyFile(String src, OutputStream out) throws IOException {
		FileInputStream input = new FileInputStream(src);
		copyLarge(input, out);
		IOUtil.close(input);
	}

	/**
	 * Copy file from one path to another.
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFile(String src, String dest) throws IOException {
		FileInputStream input = new FileInputStream(src);
		FileOutputStream out = new FileOutputStream(dest);
		copyLarge(input, out);
		IOUtil.close(out);
		IOUtil.close(input);
	}

	/**
	 * Copy one file to another.
	 * 
	 * @param src
	 * @param dst
	 * @throws IOException
	 */
	public static void copyFile(File src, File dst) throws IOException {
		Assert.isTrue(src != null && (src.isFile()), "Source File must denote a file.");
		Assert.isTrue(dst != null && (dst.isFile()),
				"Destinate File must denote a file.");
		FileInputStream input = new FileInputStream(src);
		FileOutputStream out = new FileOutputStream(dst);
		copyLarge(input, out);
		IOUtil.close(out);
		IOUtil.close(input);
	}

	/**
	 * Recursively copy the contents of the <code>src</code> file/directory to
	 * the <code>dest</code> file/directory.
	 * 
	 * @param src the source directory
	 * @param dest the destination directory
	 * @throws IOException in the case of I/O errors
	 */
	public static void copyDir(File src, File dest) throws IOException {
		Assert.isTrue(src != null && (src.isDirectory() || src.isFile()),
				"Source File must denote a directory or file");
		Assert.notNull(dest, "Destination File must not be null");
		doCopyDir(src, dest);
	}

	/**
	 * Actually copy the contents of the <code>src</code> file/directory to the
	 * <code>dest</code> file/directory.
	 * 
	 * @param src the source directory
	 * @param dest the destination directory
	 * @throws IOException in the case of I/O errors
	 */
	private static void doCopyDir(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			dest.mkdir();
			File[] entries = src.listFiles();
			if (entries == null) {
				throw new IOException("Could not list files in directory: " + src);
			}
			for (int i = 0; i < entries.length; i++) {
				File file = entries[i];
				doCopyDir(file, new File(dest, file.getName()));
			}
		} else if (src.isFile()) {
			try {
				dest.createNewFile();
			} catch (IOException ex) {
				IOException ioex = new IOException("Failed to create file: " + dest);
				ioex.initCause(ex);
				throw ioex;
			}
			copyFile(src, dest);
		} else {
			// Special File handle: neither a file not a directory.
			// Simply skip it when contained in nested directory...
		}
	}

	/**
	 * Copy file from one to another, which can be also a single file or a
	 * directory.
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copy(File src, File dest) throws IOException {
		Assert.isTrue(src != null && (src.isDirectory() || src.isFile()),
				"Source File must denote a directory or file.");
		Assert.notNull(dest, "Destination File must not be null.");
		if (src.isFile()) {
			if(dest.exists() && dest.isDirectory()) {
				// 如果目标文件存在，而且是目录
				File target = new File(dest, src.getName());
				if(!target.exists()) {
					try {
						target.createNewFile();
					} catch (IOException e) {
						IOException ioe = new IOException("Failed to create file: " + target);
						ioe.initCause(e);
						throw ioe;
					}
				}
				copyFile(src, target);
			} else {
				try {
					dest.createNewFile();
				} catch (IOException ex) {
					IOException ioex = new IOException("Failed to create file: " + dest);
					ioex.initCause(ex);
					throw ioex;
				}
				copyFile(src, dest);
			}
		} else if (src.isDirectory()) {
			copyDir(src, dest);
		} else {
			// do nothing
		}
	}

	/**
	 * Copy the contents of the given byte array to the given output File.
	 * 
	 * @param in the byte array to copy from
	 * @param out the file to copy to
	 * @throws IOException in case of I/O errors
	 */
	public static void copy(byte[] in, File out) throws IOException {
		Assert.notNull(in, "No input byte array specified");
		Assert.notNull(out, "No output File specified");
		ByteArrayInputStream inStream = null;
		OutputStream outStream = null;
		try {
			inStream = new ByteArrayInputStream(in);
			outStream = new BufferedOutputStream(new FileOutputStream(out));
			copyLarge(inStream, outStream);
		} finally {
			IOUtil.close(outStream);
			IOUtil.close(inStream);
		}
	}

	/**
	 * Copy the contents of the given input File into a new byte array.
	 * 
	 * @param in the file to copy from
	 * @return the new byte array that has been copied to
	 * @throws IOException in case of I/O errors
	 */
	public static byte[] copyToByteArray(File in) throws IOException {
		Assert.notNull(in, "No input File specified");
		return copyToByteArray(new BufferedInputStream(new FileInputStream(in)));
	}

	/**
	 * Copy the contents of the given InputStream into a new byte array. Closes
	 * the stream when done.
	 * 
	 * @param in the stream to copy from
	 * @return the new byte array that has been copied to
	 * @throws IOException in case of I/O errors
	 */
	public static byte[] copyToByteArray(InputStream in) throws IOException {
		byte[] bytes = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
		try {
			copyLarge(in, out);
			bytes = out.toByteArray();
		} finally {
			IOUtil.close(out);
		}
		return bytes;
	}

	/**
	 * Copy the contents of the given Reader to the given Writer. Closes both
	 * when done.
	 * 
	 * @param in the Reader to copy from
	 * @param out the Writer to copy to
	 * @return the number of characters copied
	 * @throws IOException in case of I/O errors
	 */
	public static int copy(Reader in, Writer out) throws IOException {
		Assert.notNull(in, "No Reader specified");
		Assert.notNull(out, "No Writer specified");
		int byteCount = 0;
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		int bytesRead = -1;
		while ((bytesRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
			byteCount += bytesRead;
		}
		out.flush();
		return byteCount;
	}

	/**
	 * Copy the contents of the given String to the given output Writer. Closes
	 * the write when done.
	 * 
	 * @param in the String to copy from
	 * @param out the Writer to copy to
	 * @throws IOException in case of I/O errors
	 */
	public static void copy(String in, Writer out) throws IOException {
		Assert.notNull(in, "No input String specified");
		Assert.notNull(out, "No Writer specified");
		out.write(in);
	}

	/**
	 * Copy the contents of the given Reader into a String. Closes the reader
	 * when done.
	 * 
	 * @param in the reader to copy from
	 * @return the String that has been copied to
	 * @throws IOException in case of I/O errors
	 */
	public static String copyToString(Reader in) throws IOException {
		String result = null;
		StringWriter out = new StringWriter();
		try {
			copy(in, out);
			result = out.toString();
		} finally {
			IOUtil.close(out);
		}
		return result;
	}

	/**
	 * 如果没有path所指定的文件，创建并返回
	 * 
	 * @param path
	 * @return
	 * @throws IOException 
	 * @time 2008-10-24 下午03:45:00
	 */
	public static File createFile(String path) throws IOException {
		File file = new File(path);
		return createFile(file);
	}

	/**
	 * 创建一个文件，父文件夹并不存在的时候也可以创建。
	 * 
	 * @param file
	 * @throws IOException 
	 */
	public static File createFile(File file) throws IOException {
		if (file.exists()) {
			return file;
		}
		File fileParent = file.getParentFile();
		if (!fileParent.exists()) {
			createDir(fileParent);
		}
		file.createNewFile();
		return file;
	}

	/**
	 * 根据指定的路径创建文件目录，在父目录不存在的情况下也可以创建
	 *
	 * @param path
	 * @return
	 */
	public static File createDir(String path) {
		if(!StringUtils.hasLength(path)) {
			throw new IllegalArgumentException("the parameter is null or empty.");
		}
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			return file;
		} else {
			return createDir(file);
		}
	}

	/**
	 * 创建一个文件夹，在父目录不存在的情况下也可以创建
	 *
	 * @param file 要创建的目标文件夹
	 * @return 创建好的文件夹文件对象，创建失败则返回<code>null</code>
	 */
	public static File createDir(File file) {
		File fileParent = file.getAbsoluteFile().getParentFile();
		if (!fileParent.exists()) {
			createDir(fileParent);
		}
		return file.mkdir() ? file : null;
	}
	
	/**
	 * 返回满足过滤条件的文件的全路径.
	 *
	 * @param file 目标目录(文件夹)
	 * @param filter 过滤条件
	 * @return
	 */
	public static String[] list(File file, FilenameFilter filter) {
		String names[] = file.list();
		if ((names == null) || (filter == null)) {
		    return names;
		}
		List<String> v = new ArrayList<String>();
		for (int i = 0 ; i < names.length ; i++) {
		    if (filter.accept(file, names[i])) {
		    	v.add(StringUtils.cleanPath(file.getAbsolutePath() + File.separator + names[i]));
		    }
		}
		return (String[])(v.toArray(new String[0]));
	}
	
	public static File[] listTree(File directory, FileFilter fileFilter) {
		Assert.isTrue(directory.exists() && directory.isDirectory(),
				"the parameter [directory] must be a directory file.");
		List<File> list = new ArrayList<File>();
		_listTree(list, directory, fileFilter);
		return (File[]) list.toArray(new File[0]);
	}

	private static void _listTree(List<File> list, File directory, FileFilter fileFilter) {
		File[] rootFilterdFiles = directory.listFiles(fileFilter);
		list.addAll(Arrays.asList(rootFilterdFiles));

		File[] rootFiles = directory.listFiles();
		for (int i = 0; i < rootFiles.length; i++) {
			File file = rootFiles[i];
			if (file.isDirectory()) {
				_listTree(list, file, fileFilter);
			}
		}
	}

	/**
	 * Convert from a <code>URL</code> to a <code>File</code>.
	 * <p>
	 * From version 1.1 this method will decode the URL. Syntax such as
	 * <code>file:///my%20docs/file.txt</code> will be correctly decoded to
	 * <code>/my docs/file.txt</code>.
	 * 
	 * @param url the file URL to convert, <code>null</code> returns
	 *            <code>null</code>
	 * @return the equivalent <code>File</code> object, or <code>null</code> if
	 *         the URL's protocol is not <code>file</code>
	 * @throws IllegalArgumentException if the file is incorrectly encoded
	 */
	public static File toFile(URL url) {
		if (url == null || !url.getProtocol().equals("file")) {
			return null;
		} else {
			String filename = url.getFile().replace('/', File.separatorChar);
			int pos = 0;
			while ((pos = filename.indexOf('%', pos)) >= 0) {
				if (pos + 2 < filename.length()) {
					String hexStr = filename.substring(pos + 1, pos + 3);
					char ch = (char) Integer.parseInt(hexStr, 16);
					filename = filename.substring(0, pos) + ch
							+ filename.substring(pos + 3);
				}
			}
			return new File(filename);
		}
	}

	/**
	 * Converts each of an array of <code>URL</code> to a <code>File</code>.
	 * <p>
	 * Returns an array of the same size as the input. If the input is
	 * <code>null</code>, an empty array is returned. If the input contains
	 * <code>null</code>, the output array contains <code>null</code> at the
	 * same index.
	 * <p>
	 * This method will decode the URL. Syntax such as
	 * <code>file:///my%20docs/file.txt</code> will be correctly decoded to
	 * <code>/my docs/file.txt</code>.
	 * 
	 * @param urls the file URLs to convert, <code>null</code> returns empty
	 *            array
	 * @return a non-<code>null</code> array of Files matching the input, with a
	 *         <code>null</code> item if there was a <code>null</code> at that
	 *         index in the input array
	 * @throws IllegalArgumentException if any file is not a URL file
	 * @throws IllegalArgumentException if any file is incorrectly encoded
	 * @since Commons IO 1.1
	 */
	public static File[] toFiles(URL[] urls) {
		if (urls == null || urls.length == 0) {
			return new File[0];
		}
		File[] files = new File[urls.length];
		for (int i = 0; i < urls.length; i++) {
			URL url = urls[i];
			if (url != null) {
				if (url.getProtocol().equals("file") == false) {
					throw new IllegalArgumentException(
							"URL could not be converted to a File: " + url);
				}
				files[i] = toFile(url);
			}
		}
		return files;
	}

	/**
	 * Converts each of an array of <code>File</code> to a <code>URL</code>.
	 * <p>
	 * Returns an array of the same size as the input.
	 * 
	 * @param files the files to convert
	 * @return an array of URLs matching the input
	 * @throws IOException if a file cannot be converted
	 */
	public static URL[] toURLs(File[] files) throws IOException {
		URL[] urls = new URL[files.length];

		for (int i = 0; i < urls.length; i++) {
			urls[i] = files[i].toURL();
		}

		return urls;
	}

	/**
	 * Returns the contents of this file as a String
	 */
	public static String readAsString(File file, String charset) throws IOException {
		String encoding = DEFAULT_ENCODING;
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
	
	/**
	 * Returns the contents of this InputStream as a String, using the specified charset "UTF-8".
	 *
	 * @param input The InputStream
	 * @return he contents of this InputStream, string format
	 * @throws IOException
	 */
	public static String readAsString(InputStream input) throws IOException {
		return readAsString(input, DEFAULT_ENCODING);
	}
	
	/**
	 * Returns the contents of this InputStream as a String, using the specified charset.
	 *
	 * @param input The InputStream
	 * @param charset charset encoding
	 * @return the contents of this InputStream, string format
	 * @throws IOException
	 */
	public static String readAsString(InputStream input, String charset) throws IOException {
		Assert.notNull(input);
		String encoding = DEFAULT_ENCODING;
		if (null != charset && charset.length() != 0) {
			encoding = charset;
		}
		BufferedReader r = new BufferedReader(new InputStreamReader(input, encoding));
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

	/**
	 * Returns the contents of this file as a byte[] the file must not be too
	 * large
	 * 
	 * @param file the file to read
	 */
	public static byte[] readAsByteArray(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		byte[] ret = FileUtil.readAsByteArray(in);
		in.close();
		return ret;
	}

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
	
	public static boolean writeText(String fileName, String content) {
		return writeText(fileName, content, "UTF-8");
	}

	public static boolean writeText(String fileName, String content, String encoding) {
		try {
			writeByte(fileName, content.getBytes(encoding));
		} catch (Exception e) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("error occured while writing content to file [" + fileName
						+ "].");
			}
			return false;
		}
		return true;
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
			IOUtil.close(pw);
		}
	}
	
	public static void writeText(File file, String txt, boolean append) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(file, append), true);
			pw.println(txt);
		} catch (Exception e) {
			if(LOG.isWarnEnabled()) {
				LOG.warn("error occured while writing file.", e);
			}
		} finally {
			IOUtil.close(pw);
		}
	}
	
	/**
	 * desc
	 *
	 * @param file
	 * @param encoding
	 * @param content
	 * @param isNewline
	 * @throws IOException
	 * @deprecated 效率太慢  using {@link#writeTxt(String, String, boolean)}
	 */
	public static void appendText(File file, String encoding, String content,
			boolean isNewline) throws IOException {
		String originText = readAsString(file, encoding);
		if (isNewline) {
			originText = originText + LS;
		}
		originText = originText + content;
		writeText(file.getAbsolutePath(), originText, encoding);
	}

	public static boolean writeByte(String fileName, byte b[]) {
		try {
			BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(
					fileName));
			fos.write(b);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean writeByte(File f, byte b[]) {
		try {
			BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(f));
			fos.write(b);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 将目录(包括其子目录)下的所有文件从一种编码转换到另外一种编码
	 * 
	 * @param dir 要转换的目录
	 * @throws IOException 
	 */
	public static void convert(String dir, String fromEncoding, String toEncoding) throws IOException {
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
				convert(file.getAbsolutePath(), fromEncoding, toEncoding);
			} else {
				filepath = file.getAbsolutePath();
				FileInputStream fis = new FileInputStream(file);
				DataInputStream in = new DataInputStream(fis);
				BufferedReader d = new BufferedReader(new InputStreamReader(in, fromEncoding));
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
				
				Writer ow = new OutputStreamWriter(new FileOutputStream(filepath), toEncoding);
				ow.write(content.toString());
				ow.close();
			}
		}
	}

	/**
	 * 根据正则式，从压缩文件中获取文件
	 * 
	 * @param zip 压缩文件
	 * @param regex 正则式，用来匹配文件名
	 * @return 数组
	 */
	public static ZipEntry[] findEntryInZip(ZipFile zip, String regex) {
		List<ZipEntry> list = new LinkedList<ZipEntry>();
		Enumeration<? extends ZipEntry> en = zip.entries();
		while (en.hasMoreElements()) {
			ZipEntry ze = en.nextElement();
			if (null == regex || ze.getName().matches(regex))
				list.add(ze);
		}
		return list.toArray(new ZipEntry[list.size()]);
	}

	/**
	 * Delete file, which can be a single file or a directory.
	 * 
	 * @param file the file to delete.
	 */
	public static void delete(File file) {
		Assert.notNull(file, "File must not be null.");
		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			deleteFile(file);
		} else if (file.isDirectory()) {
			deleteDir(file);
		} else {
			// do nothing
		}
	}

	/**
	 * Delete a single file, not a directory.
	 * 
	 * @param file the file to delete
	 * @return <code>true</code> if the <code>File</code> was deleted, otherwise
	 *         <code>false</code>.
	 */
	public static boolean deleteFile(File file) {
		if (!file.exists()) {
			return true;
		}
		if (!file.isFile()) {
			return false;
		}
		for (int i = 0; i < 3; i++) {
			if (file.delete())
				return true;
		}
		return false;
	}

	/**
	 * Delete a single file, not a directory.
	 * 
	 * @param name the filename of the file to delete.
	 * @return <code>true</code> if the <code>File</code> was deleted, otherwise
	 *         <code>false</code>.
	 */
	public static final boolean deleteFile(String name) {
		File file = new File(name);
		return deleteFile(file);
	}

	/**
	 * Delete the supplied {@link File} - for directories, recursively delete
	 * any nested directories or files as well.
	 * 
	 * @param root the root <code>File</code> to delete
	 * @return <code>true</code> if the <code>File</code> was deleted, otherwise
	 *         <code>false</code>
	 */
	public static boolean deleteDir(File root) {
		if (root != null && root.exists()) {
			if (root.isDirectory()) {
				File[] children = root.listFiles();
				if (children != null) {
					for (int i = 0; i < children.length; i++) {
						deleteDir(children[i]);
					}
				}
			}
			return root.delete();
		}
		return false;
	}

	public static void deleteTree(File directory, FileFilter fileFilter) {
		if (directory == null || !directory.exists() || !directory.isDirectory()) {
			return;
		}
		File[] rootFilterdFiles = directory.listFiles(fileFilter);
		for (File file : rootFilterdFiles) {
			delete(file);
		}

		File[] rootFiles = directory.listFiles();
		List<File> all = Arrays.asList(rootFiles);
		all.removeAll(Arrays.asList(rootFilterdFiles));
		for (int i = 0; i < all.size(); i++) {
			File file = all.get(i);
			if (file.isDirectory()) {
				deleteTree(file, fileFilter);
			}
		}
	}

	public static String getClassPath() {
		return System.getProperties().getProperty("java.class.path", "");
	}

	public static boolean compareBytes(byte buf1[], byte buf2[], int count) {
		for (int i = 0; i < count; i++) {
			if (buf1[i] != buf2[i]) {
				return false;
			}
		}
		return true;
	}

	public static boolean compareFiles(String path1, String path2) {
		long total = new File(path1).length();
		if (total != new File(path2).length()) {
			return false;
		}

		FileInputStream input1 = null;
		FileInputStream input2 = null;
		try {
			input1 = new FileInputStream(path1);
			input2 = new FileInputStream(path2);
			byte buffer1[] = new byte[32768];
			byte buffer2[] = new byte[32768];
			long totalRead;
			int bytesRead;
			for (totalRead = 0L; (bytesRead = input1.read(buffer1, 0, buffer1.length)) > 0; totalRead += bytesRead) {
				if (input2.read(buffer2, 0, buffer2.length) != bytesRead) {
					return false;
				}
				if (!compareBytes(buffer1, buffer2, bytesRead)) {
					return false;
				}
			}
			boolean flag = (total == totalRead);
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("compare files failed.", e);
		} finally {
			IOUtil.close(input1);
			IOUtil.close(input2);
		}
		return false;
	}

	public static boolean compareFiles(File file1, File file2) {
		long total = file1.length();
		if (total != file2.length()) {
			return false;
		}

		FileInputStream input1 = null;
		FileInputStream input2 = null;
		try {
			input1 = new FileInputStream(file1);
			input2 = new FileInputStream(file2);
			byte buffer1[] = new byte[32768];
			byte buffer2[] = new byte[32768];
			long totalRead;
			int bytesRead;
			for (totalRead = 0L; (bytesRead = input1.read(buffer1, 0, buffer1.length)) > 0; totalRead += bytesRead) {
				if (input2.read(buffer2, 0, buffer2.length) != bytesRead) {
					return false;
				}
				if (!compareBytes(buffer1, buffer2, bytesRead)) {
					return false;
				}
			}
			boolean flag = (total == totalRead);
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("compare files failed.", e);
		} finally {
			IOUtil.close(input1);
			IOUtil.close(input2);
		}
		return false;
	}

	/**
	 * get file's filename without extension
	 * 
	 * @param file
	 * @return
	 */
	public static String getName(File file) {
		if (!file.exists()) {
			throw new IllegalArgumentException("The file [" + file + "] doesn't exist.");
		}
		String name = file.getName();
		int index = name.lastIndexOf(".");
		if (index < 0) {
			return name;
		}
		return name.substring(0, index);
	}

	/**
	 * get file's extension
	 * 
	 * @param file
	 * @return
	 */
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

	public static File getClassesFile(String fileName) {
		String path = getClassPath();
		for (StringTokenizer tokenizer = new StringTokenizer(path, File.pathSeparator); tokenizer
				.hasMoreTokens();) {
			path = tokenizer.nextToken();
			File file = new File(String.valueOf(String.valueOf((new StringBuffer(String
					.valueOf(String.valueOf(path)))).append(File.separator).append(
					fileName))));
			if (file.exists()) {
				return file;
			}
		}
		return null;
	}

	/**
	 * 根据一个java对象获取该对象的class文件
	 * 
	 * @param object
	 * @return
	 */
	public static File getClassesFile(Object object) {
		String name = object.getClass().getName().replace('.', File.separatorChar);
		name = String.valueOf(String.valueOf(name)).concat(".class");
		return getClassesFile(name);
	}

	/**
	 * 根据一个java对象获取该对象的class文件所在目录的路径
	 * 
	 * @param object
	 * @return
	 */
	public static String getClassesDirectory(Object object) {
		return getClassesFile(object).getParent();
	}
	
	/**
	 * 根据一段文本模拟出一个输入流对象
	 * 
	 * @param cs 文本
	 * @return 输出流对象InputStream
	 */
	public static InputStream stream(CharSequence cs) {
		return stream(cs, DEFAULT_ENCODING);
	}
	
	/**
	 * 根据一段文本模拟出一个输入流对象
	 * 
	 * @param cs 文本
	 * @return 输出流对象InputStream
	 */
	public static InputStream stream(CharSequence cs, String charset) {
		try {
			return new ByteArrayInputStream(cs.toString().getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("字符转码出错.", e);
		}
	}
	
	public static boolean isImage(InputStream input) {
		try {
			BufferedImage image = ImageIO.read(input);
			return image != null;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isImage(File file) {
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			return isImage(input);
		} catch (Exception e) {
			return false;
		} finally {
			IOUtil.close(input);
		}
	}
	
	/**
	 * 以filename为基准,在某个目录下构建一个与已存在文件不重名的文件<br>
	 * 方法返回时，此文件尚未被创建
	 *
	 * @param filename 基准文件名
	 * @param destpath 指定的某个目录
	 * @return
	 */
	public static File constructFile(String filename, String destpath) {
		File file = new File(destpath + File.separator + filename);
		if(!file.exists()) {
			return file;
		}
		String fname = filename;
		int idx = fname.lastIndexOf(".");
		int seqIdx = -1;
		if(idx > -1) {
			String ext = fname.substring(fname.lastIndexOf("."));
			seqIdx = fname.lastIndexOf("_#");
			if(seqIdx > -1) {
				int oldSeq = Integer.valueOf(fname.substring(fname.lastIndexOf("_#") + 2, fname.lastIndexOf(".")));
				fname = fname.substring(0, fname.lastIndexOf("_#")) + "_#" + String.valueOf(oldSeq + 1) + ext;
			} else {
				fname = fname.substring(0, fname.lastIndexOf(".")) + "_#1" + ext;
			}
		} else {
			seqIdx = fname.lastIndexOf("_#");
			if(seqIdx > -1) {
				int oldSeq = Integer.valueOf(fname.substring(fname.lastIndexOf("_#") + 2));
				fname = fname.substring(0, fname.lastIndexOf("_#")) + "_#" + (oldSeq + 1);
			} else {
				fname = fname + "_#1";
			}
			
		}
		return constructFile(fname, destpath);
	}
	
	public static void tree(File directory) {
		doWriteTree(directory, 1);
	}
	
	public static void tree(Class<?> clazz) {
		String name = clazz.getName().replace('.', File.separatorChar);
		name = String.valueOf(String.valueOf(name)).concat(".class");
		File dir = getClassesFile(name).getParentFile();
		doWriteTree(dir, 1);
	}
	
	private static void doWriteTree(File dir, int level) {
		if(level == 1 && dir.isDirectory()) {
			System.out.println(".");
		}
		String preStr = "";// 缩进量
		for (int i = 0; i < level; i++) {
			if (i == level - 1)
				preStr = preStr + "|-- ";
			else
				preStr = preStr + "|   ";// 级别 - 代表这个目下下地子文件夹
		}
		File[] childs = dir.listFiles();// 返回一个抽象路径名数组，这些路径名表示此抽象路径名所表示目录中地文件
		if(null != childs) {
			for (int i = 0; i < childs.length; i++) {
				System.out.println(preStr + childs[i].getName());// 打印子文件地名字
				if (childs[i].isDirectory()) { // 测试此抽象路径名表示地文件能否是一个目录
					doWriteTree(childs[i], level + 1);
				}// 假如子目录下还有子目录，递归子目录调用此方法
			}
		}
	}
	


	public static void main(String[] args) throws IOException {
		File file = new File("C:/Documents and Settings/jadmin/桌面/新建 文本文档.txt");
		System.out.println(FileUtil.isImage(file));
		
		System.out.println(file.getParent());
		
		FileUtil.createFile(file);
	}
}
