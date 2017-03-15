/*
 * @(#)ByteUtil.java	2011-6-1
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.etc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * 实现序列化对象和byte字节数组的相互转化
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ByteUtil.java 1639 2012-03-20 14:02:30Z gerald.chen.hz@gmail.com $
 */
public abstract class ByteUtil {

	private static final int MAX_LENGTH = 1024;

	public static byte[] object2ByteArray(Serializable obj) {
		ByteArrayOutputStream bos = null;
		ObjectOutputStream out = null;

		try {
			bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			out.flush();

			byte[] results = bos.toByteArray();
			int srcLength = results.length;
			int length = srcLength;
			byte flags = 0x00;
			if (srcLength > MAX_LENGTH) {
				results = compress(results);
				length = results.length;
				flags = 0x01;
			}
			byte[] body = new byte[length + 5];
			body[0] = flags;
			System.arraycopy(toBytes(srcLength), 0, body, 1, 4);
			System.arraycopy(results, 0, body, 5, length);
			return body;
		} catch (IOException e) {
			throw new RuntimeException("serialize object failed.");
		} finally {
			close(out);
			close(bos);
		}
	}

	public static Serializable byteArray2Object(byte[] body) {
		if (null == body) {
			return null;
		}
		if (body.length < 5) {
			return null;
		}

		ByteArrayInputStream bis = null;
		ObjectInputStream in = null;
		try {
			int length = body.length - 5;
			byte flags = body[0];
			int c4 = body[4] & 0xff;
			int c3 = body[3] & 0xff;
			int c2 = body[2] & 0xff;
			int c1 = body[1] & 0xff;
			int size = ((c4 << 24) + (c3 << 16) + (c2 << 8) + (c1 << 0));
			byte[] results = new byte[length];
			System.arraycopy(body, 5, results, 0, length);
			if (flags == 0x01) {
				results = uncompress(results, size);
			}
			bis = new ByteArrayInputStream(results);
			in = new ObjectInputStream(bis);
			return (Serializable) in.readObject();
		} catch (IOException e) {
			throw new RuntimeException("deserialize object failed.");
		} catch (DataFormatException e) {
			throw new RuntimeException("serialize object failed.");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"deserialize object failed. Class not found exception.");
		} finally {
			close(in);
			close(bis);
		}
	}
	
	public static int toInt( byte[] bytes ) {
		int result = 0;
		for (int i=0; i<4; i++) {
			result = ( result << 8 ) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}
	
	/**
	 * 将整型数据转换为字节数组
	 *
	 * @param i 整型数据
	 * @return
	 */
	public static byte[] toBytes(int i) {
		byte[] b = new byte[4];

		b[0] = (byte) (i & 0xff);
		b[1] = (byte) ((i & 0xff00) >> 8);
		b[2] = (byte) ((i & 0xff0000) >> 16);
		b[3] = (byte) ((i & 0xff000000) >> 24);
		return b;
	}
	
	public static byte[] getBytes(String str, String charset) {
		try {
			return str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private static byte[] compress(byte[] input) throws IOException {
		Deflater compressor = new Deflater();
		compressor.setLevel(Deflater.BEST_COMPRESSION);
		compressor.setInput(input);
		compressor.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
		byte[] buf = new byte[8192];
		while (!compressor.finished()) {
			int count = compressor.deflate(buf);
			bos.write(buf, 0, count);
		}
		compressor.end();
		return bos.toByteArray();
	}

	private static byte[] uncompress(byte[] input, int uncompr_len)
			throws IOException, DataFormatException {
		Inflater decompressor = new Inflater();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompr_len);
		byte[] buf = new byte[uncompr_len];
		decompressor.setInput(input);
		while (!decompressor.finished()) {
			int count = decompressor.inflate(buf);
			if (count <= 0) {
				break;
			}
			bos.write(buf, 0, count);
		}
		decompressor.end();
		return bos.toByteArray();
	}

	private static void close(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (Exception e) {
			}
		}
	}

	private static void close(InputStream bis) {
		if (bis != null) {
			try {
				bis.close();
			} catch (Exception e) {
			}
		}
	}

	public static void main(String[] args) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("k1", "v1");
		params.put("k2", "v2");
		
		byte[] bytes = ByteUtil.object2ByteArray((Serializable) params);
		System.out.println(bytes.length);

		System.out.println(ByteUtil.byteArray2Object(bytes));
	}

}
