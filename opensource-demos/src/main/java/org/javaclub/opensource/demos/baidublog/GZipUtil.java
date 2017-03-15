package org.javaclub.opensource.demos.baidublog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Do gzip compression and uncompression.
 * 
 * @author gerald
 */
public final class GZipUtil {
	
	static final int MAX_SIZE = 1024 * 1024 * 8; // 8MB

	/**
	 * Do a gzip operation.
	 */
	public static byte[] gzip(byte[] data) {
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(MAX_SIZE);
		GZIPOutputStream output = null;
		try {
			output = new GZIPOutputStream(byteOutput);
			output.write(data);
		} catch (IOException e) {
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
				}
			}
		}
		return byteOutput.toByteArray();
	}

	/**
	 * Do a g-unzip operation.
	 */
	public static byte[] gunzip(byte[] data) {
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(MAX_SIZE);
		GZIPInputStream input = null;
		try {
			input = new GZIPInputStream(new ByteArrayInputStream(data));
			byte[] buffer = new byte[1024];
			int n = 0;
    		while (-1 != (n = input.read(buffer))) {
    			byteOutput.write(buffer, 0, n);
    		}
			/*for (;;) {
				n = input.read(buffer);
				if (n <= 0)
					break;
				byteOutput.write(buffer, 0, n);
			}*/
		} catch (IOException e) {
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException ioe) {
				}
			}
		}
		return byteOutput.toByteArray();
	}

}
