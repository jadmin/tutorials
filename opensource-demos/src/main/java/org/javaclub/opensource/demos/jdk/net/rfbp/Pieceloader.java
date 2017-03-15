/*
 * @(#)Pieceloader.java	2011-9-7
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jdk.net.rfbp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 断点续传主线程,负责部分文件的抓取.
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Pieceloader.java 2011-9-7 上午10:03:46 Exp $
 */
public class Pieceloader extends Thread {
	
	static final Log LOG = LogFactory.getLog(Pieceloader.class);
	
	/** 下载主线程 */
	private RFBP rfbp;
	
	/** 任务列表 */
	private List<Piece> tasks;
	
	private byte[] buff = new byte[1024 * 8];   // 缓冲区的大小
    private RandomAccessFile out;
    private BufferedInputStream in;
    
    /** 当前线程的读取字节数 */
    private long read;
    
    /** 当前线程在读取数据时总花费的时间 */
    private long cost;
	
	public Pieceloader(RFBP rfbp, List<Piece> tasks) {
        this.rfbp = rfbp;
        this.tasks = tasks;
    }

	public void run() {
		if(rfbp.isDone()) {
        	rfbp.finished();
        	return;
        }
		
        while (!rfbp.isDone()) {
            
            // 暂停任务
            synchronized (this) {
                if (rfbp.isPaused()) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            
            // 中断停止
            if (Thread.interrupted() || rfbp.isStopped()) {
                return;
            }
            
            // 等待获取任务
            Piece piece;
            synchronized (tasks) {
                while (tasks.isEmpty()) {
                    if (rfbp.isDone()) return;
                    try {
                        tasks.wait();
                    } catch (InterruptedException ie) { }
                }
                piece = tasks.remove(0);
                rfbp.removeFreeLoader(this);
            }
            
            try {
                URL u = new URL(rfbp.getURL());
                HttpURLConnection uc = (HttpURLConnection) u.openConnection();
                uc.setConnectTimeout(60000);
                uc.setReadTimeout(60000);
                // 设置断点续传位置
                uc.setAllowUserInteraction(true);
                uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
                uc.setRequestProperty("Range", "bytes=" + piece.getPos() + "-" + piece.getEnd());
                in = new BufferedInputStream(uc.getInputStream());

                out = new RandomAccessFile(rfbp.getFileProcess(), "rw");
                out.seek(piece.getPos()); // 设置指针位置

                long start; long end; int len = 0;
                while (piece.getPos() < piece.getEnd()) {
                    start = System.currentTimeMillis();
                    len = in.read(buff, 0, buff.length);
                    if (len == -1) break;
                    out.write(buff, 0, len);
                    end = System.currentTimeMillis();
                    cost += end - start;    // 累计时间使用
                    
                    long newPos = piece.getPos() + len;
                    
                    // 如果该区段已经完成,如果该线程负责的区域已经完成，或出界
                    if (newPos > piece.getEnd()) {
                        piece.setPos(piece.getEnd());   
                        long offset = newPos - piece.getEnd();
                        long trueReads = (len - offset + 1);
                        rfbp.growReadBytes(trueReads);    // 修正偏移量
                        rfbp.setOffsetTotal(rfbp.getOffsetTotal() + trueReads);
                        read += trueReads;
                    } else {
                    	rfbp.growReadBytes(len);
                        piece.setPos(piece.getPos() + len);
                        read += len;
                    }
                    // 如果存在空闲的任务线程，则切割出新的区域至任务队列中。由空闲
                    // 的线程辅助下载
                    if (rfbp.hasFreeLoader()) {
                        Piece newPiece = piece.split();
                        if (newPiece != null) {
                        	if(LOG.isDebugEnabled()) {
                        		LOG.debug("存在空闲等待线程，切割出新分片：" + newPiece.toString());
                        	}
                            synchronized (tasks) {
                            	rfbp.addTask(newPiece);
                            	rfbp.setRepairCount(rfbp.getRepairCount() + 1); // 增加切割次数
                                tasks.notifyAll();  // 唤醒等待任务中的空闲线程
                            }
                        }
                        
                    }
                    // 暂停任务
                    synchronized (this) {
                        if (rfbp.isPaused()) {
                            try {
                                this.wait();
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                    
                    // 中断停止
                    if (Thread.interrupted() || rfbp.isStopped()) {
                        in.close();
                        out.close();
                        return;
                    }
                }
                out.close();
                in.close(); 
                rfbp.addFreeLoader(this);
                if (rfbp.isDone()) {
                	rfbp.finished();
                }
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
        
    }

	/** 获取当前线程已经下载的字节数 */
    public long getReadBytes() {
        return this.read;
    }
    
    /** 获取当前线程读取数据时总花费的时间 */
    public long getCostTime() {
        return this.cost;
    }
    
    /** 获取当前线程的下载速度 */
    public float getSpeed() {
        if (cost <= 0.001) return 0f;
        return ((read / 1024f) / (cost / 1000f));
    }
    
    /** 继续下载任务 */
    public synchronized void continued() {
        this.notifyAll();
    }
}
