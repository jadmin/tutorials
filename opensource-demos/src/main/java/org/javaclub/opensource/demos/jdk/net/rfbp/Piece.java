/*
 * @(#)Piece.java	2011-9-7
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jdk.net.rfbp;


/**
 * Piece
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Piece.java 2011-9-7 下午02:23:09 Exp $
 */
public class Piece {

	private long start;  // 起始点的指针位置
    private long pos;    // 当前完成度所在的指针位置
    private long end;    // 终结点的指针位置
    
    static final int MAX_MB = 1;
    
    public Piece(long start, long pos, long end) {
        this.start = start;
        this.pos = pos;
        this.end = end;
    }

    public synchronized long getEnd() {
        return end;
    }

    public synchronized void setEnd(long end) {
        this.end = end;
    }

    public synchronized long getPos() {
        return pos;
    }

    public synchronized void setPos(long pos) {
        this.pos = pos;
    }

    public synchronized long getStart() {
        return start;
    }

    public synchronized void setStart(long start) {
        this.start = start;
    }
    
    /**
     * Split a new piece if possible.
     * 
     * @return <code>null</code> if this piece can't be splited already.
     */
    public synchronized Piece split() {
        // 如果当前区块还有大于1MB的数据未下载完，则允许切割出分块
        long leftSize = end - pos;
        if (leftSize > (1024 * 1024 * MAX_MB)) {
            long s; // 起点
            long e; // 终点
            s = (long) (pos + Math.ceil(leftSize / 2));
            e = end;
            end = s - 1;
            Piece piece = new Piece(s, s, e);
            return piece;
        }
        return null;
    }

	public String toString() {
		return "Piece [start=" + start + ", pos=" + pos + ", end=" + end + "]";
	}
    
    
}
