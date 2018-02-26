/*
 * @(#)Utils.java	2018年2月23日
 *
 * Copyright (c) 2018. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.etc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.util.Assert;

/**
 * Utils 通用工具类(Strings/IO等)
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Utils.java 2018年2月23日 22:01:51 Exp $
 */
public class Utils {

	public static final int BUFFER_SIZE = 4096;

	public static final String EMPTY_STRING = "";
	public static final String DEFAULT_CHARSET = "UTF-8";

	private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	private Utils() {
		// forbidden build
	}

	// ===== Strings start =====

	public static String noneNull(String str) {
		return (null == str ? EMPTY_STRING : str.trim());
	}

	public static String noneNull(String str, String defaults) {
		return (null == str ? defaults : str.trim());
	}

	public static String anyNoneNull(String... arrays) {
		if (null == arrays || 0 >= arrays.length) {
			return EMPTY_STRING;
		}
		for (String string : arrays) {
			if (isNotBlank(string)) {
				return string.trim();
			}
		}

		return EMPTY_STRING;
	}

	public static String noneBlank(String str, String defaults) {
		return (isBlank(str) ? defaults : str.trim());
	}

	public static String anyToString(Object o, boolean noneNull) {
		if (null == o && !noneNull) {
			return null;
		}

		return (null == o) ? EMPTY_STRING : o.toString();
	}

	public static boolean isEmpty(String input) {
		return null == input || 0 == input.length();
	}

	public static boolean isNotEmpty(String input) {
		return null != input && input.length() > 0;
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean isNotBlank(String... arrays) {
		for (String string : arrays) {
			if (isBlank(string)) {
				return false;
			}
		}
		return true;
	}

	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}

	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasText(String str) {
		return hasText((CharSequence) str);
	}

	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}

	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(str, false);
	}

	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (str == null || str.length() == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str.length());
		if (capitalize) {
			buf.append(Character.toUpperCase(str.charAt(0)));
		} else {
			buf.append(Character.toLowerCase(str.charAt(0)));
		}
		buf.append(str.substring(1));
		return buf.toString();
	}

	/**
	 * <pre>
	 * Utils.split(null)       = null
	 * Utils.split("")         = []
	 * Utils.split("abc def")  = ["abc", "def"]
	 * Utils.split("abc  def") = ["abc", "def"]
	 * Utils.split(" abc ")    = ["abc"]
	 * </pre>
	 *
	 * @param str
	 *            the String to parse, may be null
	 * @return an array of parsed Strings, <code>null</code> if null String
	 *         input
	 */
	public static String[] split(String str) {
		return split(str, null, -1);
	}

	/**
	 * <pre>
	 * StringUtils.split(null, *)         = null
	 * StringUtils.split("", *)           = []
	 * StringUtils.split("a.b.c", '.')    = ["a", "b", "c"]
	 * StringUtils.split("a..b.c", '.')   = ["a", "b", "c"]
	 * StringUtils.split("a:b:c", '.')    = ["a:b:c"]
	 * StringUtils.split("a b c", ' ')    = ["a", "b", "c"]
	 * </pre>
	 */
	public static String[] split(String str, char separatorChar) {
		return splitWorker(str, separatorChar, false);
	}

	/**
	 * <pre>
	 * StringUtils.split(null, *)         = null
	 * StringUtils.split("", *)           = []
	 * StringUtils.split("abc def", null) = ["abc", "def"]
	 * StringUtils.split("abc def", " ")  = ["abc", "def"]
	 * StringUtils.split("abc  def", " ") = ["abc", "def"]
	 * StringUtils.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
	 * </pre>
	 */
	public static String[] split(String str, String separatorChars) {
		return splitWorker(str, separatorChars, -1, false);
	}

	public static String[] split(String str, String separatorChars, int max) {
		return splitWorker(str, separatorChars, max, false);
	}

	/**
	 * 将目标字符串以指定字符分隔后，并每个分隔的元素去除首尾空格
	 *
	 * @param str
	 *            被处理字符串
	 * @param separatorChars
	 *            分隔字符
	 * @return
	 */
	public static String[] splitAndTrim(String str, String separatorChars) {
		List<String> list = new ArrayList<String>();
		String[] strArray = splitWorker(str, separatorChars, -1, false);
		if (null != strArray && strArray.length > 0) {
			for (String e : strArray) {
				if (null == e)
					continue;
				list.add(e.trim());
			}
		}
		return list.toArray(new String[0]);
	}

	private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
		// Performance tuned for 2.0 (JDK1.4)

		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return new String[0];
		}
		List<String> list = new ArrayList<String>();
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		while (i < len) {
			if (str.charAt(i) == separatorChar) {
				if (match || preserveAllTokens) {
					list.add(str.substring(start, i));
					match = false;
					lastMatch = true;
				}
				start = ++i;
				continue;
			}
			lastMatch = false;
			match = true;
			i++;
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
		// Performance tuned for 2.0 (JDK1.4)
		// Direct code is quicker than StringTokenizer.
		// Also, StringTokenizer uses isSpace() not isWhitespace()

		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return new String[0];
		}
		List<String> list = new ArrayList<String>();
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null) {
			// Null separator means use whitespace
			while (i < len) {
				if (Character.isWhitespace(str.charAt(i))) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else if (separatorChars.length() == 1) {
			// Optimise 1 character case
			char sep = separatorChars.charAt(0);
			while (i < len) {
				if (str.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else {
			// standard case
			while (i < len) {
				if (separatorChars.indexOf(str.charAt(i)) >= 0) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * 计算md5
	 */
	public static String getMd5(final String text) {
		return md5(text.getBytes());
	}

	/**
	 * Make MD5 diaguest.
	 */
	public static String md5(byte[] data) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buf = md.digest(data);
			return toHexString(buf);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 Algorithm not supported", e);
		}
	}

	public static String toHexString(byte[] bytes) {
		int length = bytes.length;
		StringBuffer sb = new StringBuffer(length * 2);
		int x = 0;
		int n1 = 0, n2 = 0;
		for (int i = 0; i < length; i++) {
			if (bytes[i] >= 0)
				x = bytes[i];
			else
				x = 256 + bytes[i];
			n1 = x >> 4;
			n2 = x & 0x0f;
			sb = sb.append(HEX[n1]);
			sb = sb.append(HEX[n2]);
		}
		return sb.toString();
	}

	// ===== Strings end =======

	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system
			// class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = Utils.class.getClassLoader();
		}
		return cl;
	}

	// ======== IO start =========

	public static File getClasspathFile(String classpath) {
		URL url = getDefaultClassLoader().getResource(classpath);
		if (url == null) {
			throw new IllegalStateException("Could not find classpath properties resource: " + classpath);
		}
		if (url.getProtocol().equals("file") == false) {
			throw new IllegalArgumentException("URL could not be converted to a File: " + url);
		}
		return toFile(url);
	}

	/**
	 * 加载classpath资源(亦可读取jar中内容)
	 *
	 * @param resource classpath资源路径，如：META-INF/MANIFEST.MF
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static InputStream getClasspathStream(String resource) {
		return getDefaultClassLoader().getSystemResourceAsStream(resource);
	}

	/**
	 * Returns the contents of this InputStream as a String, using the specified
	 * charset.
	 *
	 * @param input The InputStream
	 * @param charset charset encoding
	 * @return the contents of this InputStream, string format
	 * @throws IOException
	 */
	public static String readAsString(InputStream input, String charset) throws IOException {
		Assert.notNull(input);
		String encoding = DEFAULT_CHARSET;
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
					filename = filename.substring(0, pos) + ch + filename.substring(pos + 3);
				}
			}
			return new File(filename);
		}
	}

	public static String getJarVersion(String groupId, String artifactId) {
		Properties pom = null;
		try {
			pom = getJarProperties(groupId, artifactId);
		} catch (Exception e) {
		}

		return null == pom ? null : pom.getProperty("version");
	}

	public static Properties getJarProperties(String groupId, String artifactId) throws IOException {

		Properties properties = new Properties();

		String mainfestFile = "META-INF/MANIFEST.MF";
		String mavenPomFile = String.format("META-INF/maven/%s/%s/pom.properties", groupId, artifactId);

		InputStream pomResource = getClasspathStream(mavenPomFile);
		if (null != pomResource) {
			// #Generated by Maven
			// #Wed Jul 22 13:09:50 CST 2015
			// version=1.0.5-SNAPSHOT
			// groupId=com.shandiangou
			// artifactId=shangou-commons

			byte[] pomData = copyToByteArray(pomResource);
			properties.load(new ByteArrayInputStream(pomData));

			BufferedReader br = new BufferedReader(new StringReader(new String(pomData, "utf-8")));
			List<String> lines = new ArrayList<String>();
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				lines.add(line);
			}
			// try to get package time
			if (lines.size() >= 2 && lines.get(1).startsWith("#")) {
				String time = lines.get(1).substring(1);
				properties.put("packageTime", time);
			}
		}

		InputStream mainfestResource = getClasspathStream(mainfestFile);
		if (null != mainfestResource) {
			// Manifest-Version: 1.0
			// Archiver-Version: Plexus Archiver
			// Built-By: hengyunabc
			// Created-By: Apache Maven 3.3.1
			// Build-Jdk: 1.8.0_45

			byte[] mainfestData = copyToByteArray(mainfestResource);
			BufferedReader br = new BufferedReader(new StringReader(new String(mainfestData)));
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				String[] split = split(line, ':');
				if (split != null && split.length == 2) {
					properties.put(split[0].trim(), split[1].trim());
				}
			}
		}

		return properties;
	}

	public static byte[] copyToByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
		copy(in, out);
		return out.toByteArray();
	}

	public static int copy(InputStream in, OutputStream out) throws IOException {
		try {
			int byteCount = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		} finally {
			closeQuietly(in);
			closeQuietly(out);
		}
	}

	public static void closeQuietly(InputStream in) {
		try {
			if (null != in) {
				in.close();
			}
		} catch (Exception e) {
		}
	}

	public static void closeQuietly(OutputStream out) {
		try {
			if (null != out) {
				out.close();
			}
		} catch (Exception e) {
		}
	}

	public static void closeQuietly(Connection con) {
		try {
			if (null != con) {
				con.close();
			}
		} catch (Exception e) {
		}
	}

	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
	}

	public static void close(Connection conn) {
		try {
			if ((conn != null) && (!conn.isClosed())) {
				conn.close();
			}
		} catch (SQLException e) {
		}
	}

	// ======== IO end ===========

	public static void main(String[] args) {
		String mavenPomFile = String.format("META-INF/maven/%s/%s/pom.properties", "groupId", "artifactId");
		System.out.println(mavenPomFile);
	}
}
