/*
 * @(#)IOUtil.java	2009-12-14
 *
 * Copyright (c) 2009 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.etc;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;

/**
 * A utility class for IO operation.
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: IOUtil.java 1641 2012-03-22 14:01:24Z gerald.chen.hz@gmail.com $
 */
public abstract class IOUtil {

	public static void close(Reader in) {
		if(in != null) {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
	}
	
	public static void close(Writer out) {
		if(out != null) {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}
	
	public static void close(InputStream input) {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
			}
		}
	}

	public static void close(OutputStream output) {
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	public static void close(FileChannel channel) {
		if (channel != null) {
			try {
				channel.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
     * Compare the contents of two Streams to determine if they are equal or
     * not.
     * <p>
     * This method buffers the input internally using
     * <code>BufferedInputStream</code> if they are not already buffered.
     *
     * @param input1  the first stream
     * @param input2  the second stream
     * @return true if the content of the streams are equal or they both don't
     * exist, false otherwise
     * @throws NullPointerException if either input is null
     * @throws IOException if an I/O error occurs
     */
    public static boolean contentEquals(InputStream input1, InputStream input2)
            throws IOException {
        if (!(input1 instanceof BufferedInputStream)) {
            input1 = new BufferedInputStream(input1);
        }
        if (!(input2 instanceof BufferedInputStream)) {
            input2 = new BufferedInputStream(input2);
        }

        int ch = input1.read();
        while (-1 != ch) {
            int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }

        int ch2 = input2.read();
        return (ch2 == -1);
    }
}
