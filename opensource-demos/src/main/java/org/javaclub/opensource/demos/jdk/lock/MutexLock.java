/*
 * @(#)MutexLock.java	2011-9-8
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jdk.lock;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * MutexLock 通过文件实现的互斥锁.
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: MutexLock.java 2011-9-8 下午02:09:07 Exp $
 */
public abstract class MutexLock {
	
	static final File mutexIdentifyFile;
	static {
		// 初始化一个与多线程相关的文件
		mutexIdentifyFile = null;
	}

	public static boolean isLocked() {
		boolean locked = false;
		try {
			// 打开一个用于实现互斥锁的文件。
			RandomAccessFile fis = new RandomAccessFile(mutexIdentifyFile, "rw");

			// 获得文件通道
			FileChannel lockfc = fis.getChannel();

			// 获得文件的独占锁，该方法不产生堵塞，立刻返回
			FileLock flock = lockfc.tryLock();

			// 如果为空，则表明已经有应用占有该锁
			if (flock != null) {
				locked = true;
			}

		} catch (Exception e) { }
		return locked;
	}
}
