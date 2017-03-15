/*
 * @(#)RFBP.java	2011-9-7
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jdk.net.rfbp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 断点续传主线程,负责整个文件的抓取.
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: RFBP.java 2011-9-7 上午09:58:17 Exp $
 */
public class RFBP extends Thread {
	
	static final Log LOG = LogFactory.getLog(RFBP.class);
	
	/** 要下载的文件信息 */
	CfgBean cfg = null;
	
	int threads = 8;
	
    /** 空闲的任务线程 */
    private Vector<Pieceloader> freeLoaders = new Vector<Pieceloader>();
	
    /** 所有执行线程,包括执行的与空闲的 */
    private List<Pieceloader> allLoaders = new ArrayList<Pieceloader>();
    
    private List<Pieceloador> allLoadors = new ArrayList<Pieceloador>();
    
    /** 待完成的任务列表 */
    private Vector<Piece> todoTasks = new Vector<Piece>();
    
    /** 当前的所有任务列表，包括正在处理中的与待完成的任务列表 */
    private Vector<Piece> allTasks = new Vector<Piece>();
	
	/** 文件总长度  */
	long fileLength;
	
	/** 已经读取的长度 */
	long readLength;
	
	/** 是否第一次取文件 */
	boolean isFirst = true;
	
	boolean initialized = false;
	
	/** 文件下载的临时信息 */
	File tmp;
	
	/** 正在下载中的文件名(默认比原文件名多带一个".tmp") */
	private File fileProcess;
	
	DataOutputStream output; // 输出到文件的输出流
	
	/** 是否使用加强版 */
	private boolean intensify = false;
	
	/** 下载是否已经完成 */
	boolean finished = false;
	
	// 运行状态信息
    private int state = 0;
    private final static int STATE_NONE = 0;
    private final static int STATE_READY = 1;
    private final static int STATE_LOADING = 2;
    private final static int STATE_PAUSED = 3;
    private final static int STATE_STOPPED = 4;
    
    protected RFBP() {
    	
    }
    
    public RFBP(String url) throws IOException {
    	this(url, System.getProperty("user.home"), null);
    }
    
    public RFBP(String url, String path, String name) throws IOException {
    	this(new CfgBean(url, path, name));
    }

	public RFBP(CfgBean bean) throws IOException {
		try {
			cfg = bean;
			ensureReady();
		} finally {
			bean = null;
		}
		tmp = new File(cfg.getPath() + File.separator + cfg.getName() + ".tmp.cfg");
		fileLength = this.getFileLength(this.getURL());
		fileProcess = new File(cfg.getPath() + File.separator + cfg.getName() + ".tmp");
		if (tmp.exists()) {
			isFirst = false;
			readBreakPoint();
			state = STATE_READY;
			if(LOG.isDebugEnabled()) {
				LOG.debug("文件总长度：" + fileLength + "，上次下载已经读取长度：" + readLength);
				LOG.debug("从断点处继续下载" + this.getURL());
			}
		} else {
			if(fileLength == -1) {
				throw new IOException("Invalid file size => " + fileLength);
			} else if(fileLength == -2) {
				throw new IOException("Failed to access the url file => " + cfg.getFileURL());
			}
			if((fileLength / 1024 / 1024) > 100) {
				threads = 20;
			}
			if(LOG.isDebugEnabled()) {
				LOG.debug("下载" + this.getURL() + "  文件大小" + fileLength + "  启动线程数" + threads);
			}
		}
	}

	public boolean isIntensify() {
		return intensify;
	}

	public RFBP setIntensify(boolean intensify) {
		this.intensify = intensify;
		return this;
	}

	public void run() {
		if(isStopped() || isLoading()) {
			return;
		} else {
			// 如果还从未下载过数据，则完全初始化
			if (!initialized) {
				initialized();
			}
			// 开始下载
			state = STATE_LOADING;
			loading();
		}
	}
	
	
	/** 
	 * 首次下载文件，需要初始化相关信息 
	 * 
	 */
    protected void initialized() {
        // 对文件进行分块
        // 创建分块，并创建相应的负责下载的线程
        long pieceSize = (long) Math.ceil((double) fileLength / (double) threads);
        long pStart = 0; long pEnd = 0;
        allTasks.clear();
        todoTasks.clear();
        for (int i = 0; i < threads; i++) {
            if (i == 0) {
                pStart = pieceSize * i;
            }
            if (i == threads - 1) {
                pEnd = fileLength;
            } else {
                pEnd = pStart + pieceSize;
            }
            Piece piece = new Piece(pStart, pStart, pEnd);
            allTasks.add(piece);
            todoTasks.add(piece);
            pStart = pEnd + 1;
        }
        initialized = true;
        isFirst = true;
        state = STATE_READY;
    }

	protected void ensureReady() {
		if(null == cfg.getFileURL() || cfg.getFileURL().length() == 0) {
			throw new RuntimeException("The fileURL is null.");
		}
		if(cfg.getPath() == null) {
			cfg.setPath(System.getProperty("user.home"));
		}
		String ext = cfg.getFileURL().substring(cfg.getFileURL().lastIndexOf("."));
		if(cfg.getName() == null) {
			cfg.setName(UUID.randomUUID().toString().replace("-", "") + ext);
		}
		if(new File(cfg.getPath() + File.separator + cfg.getName()).exists()) {
			File file = constructFile(cfg.getName(), cfg.getPath());
			cfg.setName(file.getName());
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
	
	/**
	 * 启动子线程,开始分片下载
	 *
	 */
	protected void loading() {
        // 初始化下载线程(先清除，以确保没有不可用的线程)
        allLoaders.clear();
        allLoadors.clear();
        // 初始化定时器,每3秒保存一次状态
        timer = new Timer();
        timer.schedule(new BreakPointTimerTask(), 0, 1000 * 3);
        for (int i = 0; i < threads; i++) {
        	if(this.isIntensify()) {
        		Pieceloader pl = new Pieceloader(this, todoTasks);
                pl.start();
                allLoaders.add(pl);
        	} else {
        		if(i < todoTasks.size()) {
        			Pieceloador pl = new Pieceloador(this, todoTasks.get(i));
                    pl.start();
                    allLoadors.add(pl);
        		}
        	}
        }        
    }

	/** 状态未知 */
    public boolean isNone() {
        return state == STATE_NONE ? true : false;
    }
    
    /** 是否运行中的 */
    public boolean isLoading() {
        return state == STATE_LOADING ? true : false;
    }
    
    /** 是否准备就绪的 */
    public boolean isReady() {
        return state == STATE_READY ? true : false;
    }
    
    /** 是否暂停中的 */
    public boolean isPaused() {
        return state == STATE_PAUSED ? true : false;
    }
    
    /** 是否已经停止的 */
    public boolean isStopped() {
        return state == STATE_STOPPED ? true : false;
    }
    
    /** 判断文件是否已经全部下载完 */
    public synchronized boolean isDone() {
        return (readLength >= fileLength);
    }
    
    /** 处理已经完成的任务 */
	public synchronized void finished() {
		if (!finished) { // 确保此方法只被调用一次
			if(null != timer) {
				timer.cancel();
			}
			
			fileProcess.renameTo(new File(cfg.getPath() + File.separator
					+ cfg.getName()));
			this.state = STATE_STOPPED;
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
			}
			deleteTempFile();
			finished = true;
			if(LOG.isInfoEnabled()) {
				LOG.info("文件下载成功");
				LOG.info(this.getURL() + "\t" + this.getSpeed() + "kb/s");
			}
			System.out.println(this.getURL() + "\t" + this.getSpeed() + "kb/s");
		}
	}
    
    // ------------------------------------------------------------ 任务处理
    
    /** 将一个新的任务区域(待完成的)添加到列表中，todoTasks, allTasks */
    public synchronized void addTask(Piece piece) {
        this.allTasks.add(piece);
        this.todoTasks.add(piece);
        try {
			this.saveBreakPoint();
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("Error occured while adding a piece to task.", e);
			throw new RuntimeException("Error occured while adding a piece to task.", e);
		}
    }
    
    /**
     * 增加已读的字节数,下载线程每次读写完一段数据后都会调用该方法
     * @param readBytes
     */
    public synchronized void growReadBytes(long bytes) {
        readLength += bytes;
    }
    
    /** 是否有空的任务线程 */
    public synchronized boolean hasFreeLoader() {
        return freeLoaders.size() > 0;
    }
    
    /** 添加一个空闲的任务线程 */
    public synchronized void addFreeLoader(Pieceloader pl) {
        freeLoaders.add(pl);
        try {
			this.saveBreakPoint();
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("Error occured while adding a free Pieceloader.", e);
			throw new RuntimeException("Error occured while adding a free Pieceloader.", e);
		}
    }
    
    /** 移除一个空闲线程 */
    public synchronized void removeFreeLoader(Pieceloader pl) {
        freeLoaders.remove(pl);
    }
    
    
    /** 获取任务的URL下载源地址 */
    public String getURL() {
        return cfg.getFileURL();
    }
    
    /** 获取下载中的文件对象 */
    public File getFileProcess() {
        return this.fileProcess;
    }
    
    /** 获取下载速度 (KB/s) */
    public synchronized float getSpeed() {
        float sp = 0f;
        if(this.isIntensify()) {
        	for (Pieceloader pl : allLoaders) {
                sp += pl.getSpeed();
            }
        } else {
        	for (Pieceloador pl : allLoadors) {
                sp += pl.getSpeed();
            }
        }
        
        return sp;
    }

	/**
	 * 获得目标文件长度
	 *
	 * @return 文件长度
	 */
	public long getFileLength(String fileURL) throws IOException {
		long count = -1;
		try {
			URL url = new URL(fileURL);
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setConnectTimeout(60000);
			httpConnection.setReadTimeout(60000);
			httpConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
			int responseCode = httpConnection.getResponseCode();
			if (responseCode >= 400) {
				processErrorCode(responseCode);
				return -2; // -2 represent access is error
			}
			String header;
			for (int i = 1;; i++) {
				header = httpConnection.getHeaderFieldKey(i);
				if(null == header) {
					continue;
				}
				if (header.equals("Content-Length")) {
					count = Long.parseLong(httpConnection.getHeaderField(header));
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		return count;
	}

	/**
	 * 保存断点信息，即：保存下载信息（文件指针位置）
	 * @throws IOException 
	 *
	 */
	protected synchronized void saveBreakPoint() throws IOException {
		try {
			output = new DataOutputStream(new FileOutputStream(tmp));
			output.writeInt(allTasks.size());
			for (Piece p : allTasks) {
				output.writeLong(p.getStart());
				output.writeLong(p.getEnd());
				output.writeLong(p.getPos());
			}
			output.close();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 读取断点信息，即：读取保存的下载信息（文件指针位置）
	 *
	 */
	protected void readBreakPoint() throws IOException {
		DataInputStream input = null;
		try {
			input = new DataInputStream(new FileInputStream(tmp));
			int threadsCount = input.readInt();
			long startPos; long endPos; long readPos;
			Piece p;
			for (int i = 0; i < threadsCount; i++) {
				startPos = input.readLong();
				endPos = input.readLong();
				readPos = input.readLong();
				readLength += readPos - startPos;
				// 初始化任务
				p = new Piece(startPos, readPos, endPos);
				if(LOG.isDebugEnabled()) {
					LOG.debug(i + " => " + p.toString());
				}
				allTasks.add(p);
				if(readPos < endPos) {
					// 有没读取过的数据才加入待处理任务
					todoTasks.add(p);
				}
			}
			readLength += threadsCount;
			initialized = true;
			input.close();
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			try {
				if(null != input) {
					input.close();
				}
			} catch (IOException e) {}
		}
	}
	
	/**
	 * 文件下载完成后，删除临时文件
	 *
	 */
	protected void deleteTempFile() {
		if(tmp.exists()) {
			for (int i = 0; i < 3; i++) {
				tmp.delete();
			}
		}
		if(fileProcess.exists()) {
			for (int i = 0; i < 3; i++) {
				fileProcess.delete();
			}
		}
	}
	
	private void processErrorCode(int code) {
		System.err.println("Error Code : " + code);
	}
	
	// ------------------------------------------------------------ 偏移修正
    private int repairCount;    // 修正次数
    private long offsetTotal;   // 偏移量

    public synchronized long getOffsetTotal() {
        return offsetTotal;
    }

    public synchronized void setOffsetTotal(long offsetTotal) {
        this.offsetTotal = offsetTotal;
    }

    public synchronized int getRepairCount() {
        return repairCount;
    }

    public synchronized void setRepairCount(int repairCount) {
        this.repairCount = repairCount;
    }
	
	/**
     * 任务定时器，每隔一段时间，这个定时器会把当前下载任务的各个分片(Piece)的状态
     * 信息保存到磁盘文件中，以避免程序出现意外或突然蹦溃而造成的无法断点续传的功能
     */
    private Timer timer;
    
    private class BreakPointTimerTask extends TimerTask {
        public void run() {
            try {
                if (isDone()) {
                	this.cancel();
                }
                saveBreakPoint();
            } catch (Exception ex) {
            	throw new RuntimeException(ex);
            }
        }
    }
}
