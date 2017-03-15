/*
 * @(#)CfgBean.java	2011-9-7
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jdk.net.rfbp;

/**
 * 描述被抓取文件的信息，如文件保存的目录、名字、抓取文件的 URL等.
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: CfgBean.java 2011-9-7 上午10:07:58 Exp $
 */
public class CfgBean {
	
	/** File's URL */
	private String fileURL;
	
	/** Saved File's Path */
	private String path = System.getProperty("user.home");
	
	/** Saved File's Name */
	private String name;
	
	protected CfgBean() {
		
	}
	
	public CfgBean(String fileURL, String path, String name) {
		this.fileURL = fileURL;
		this.path = path;
		this.name = name;
	}

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String value) {
		this.fileURL = value;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String value) {
		this.path = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

}
