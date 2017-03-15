/*
 * @(#)BaidublogCollector.java	2011-10-10
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.baidublog;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * CsdnblogPoster
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: BaidublogCollector.java 2011-10-10 下午09:37:19 Exp $
 */
public class BaidublogCollector {
	
	protected static final Log LOG = LogFactory.getLog(BaidublogCollector.class);
	
	static final String BAIDU_BLOG = "http://hi.baidu.com/";
	static final String TPL_FILE = "qq_note.html";
	
	private String blogid;
	private String path;
	
	public BaidublogCollector() {
		super();
	}

	public BaidublogCollector(String blogid, String path) {
		super();
		this.blogid = blogid;
		this.path = path;
	}
	
	public final String getPath() {
		return path;
	}

	public final void setPath(String path) {
		this.path = path;
	}

	public final String getBlogid() {
		return blogid;
	}

	public final void setBlogid(String blogid) {
		this.blogid = blogid;
	}
	
	public final String getPostUrl() {
		return (BAIDU_BLOG + this.getBlogid());
	}
	
	public void save(BaiduHi hi) throws IOException {
		String txt = CommonUtil.readAsString(new File(getPath() + TPL_FILE), "UTF-8");
		txt = txt.replace("${title}", hi.getTitle());
		txt = txt.replace("${src_link}", BAIDU_BLOG + getBlogid() + "/blog");
		txt = txt.replace("${blogid}", this.getBlogid());
		txt = txt.replace("${catagory}", hi.getCategories());
		txt = txt.replace("${create_time}", CommonUtil.time(hi.getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
		txt = txt.replace("${content}", hi.getContent());
		
		CommonUtil.writeText(getPath() + hi.getTitle() + ".html", txt, false);
	}

	public static void main(String[] args) {
		BaidublogCollector collector = new BaidublogCollector("jadmin", "D:/tmp/baidu_blog/");
		Blogcrawler crawler = new Blogcrawler(collector.getBlogid());
		Stack<String> urls = crawler.getAllPostLink(collector.getPostUrl());
		BaiduHi hi = null;
		while (!urls.isEmpty()) {
			hi = crawler.getBaiduHi(urls.pop());
			File file = new File(collector.getPath() + hi.getTitle() + ".html");
			if(null == hi || file.exists()) {
				continue;
			}
			try {
				collector.save(hi);
				try {
					Thread.sleep(6 * 1000L);
				} catch (InterruptedException e) {
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			log(hi.getTitle());
			log(hi.getContent());
			log(hi.getCategories());
			log(CommonUtil.time(hi.getDateCreated(), "yyyy-MM-dd HH:mm:ss"));
		}
	}
	
	private static void log(String line) {
		System.out.println(line);
		System.out.println();
	}

}
