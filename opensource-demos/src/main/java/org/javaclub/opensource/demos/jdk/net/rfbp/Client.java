/*
 * @(#)Client.java	2011-9-7
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jdk.net.rfbp;

import org.junit.Test;


/**
 * Client
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Client.java 2011-9-7 上午10:09:02 Exp $
 */
public class Client {

	public void fetchFile(String url, String path, String name) {
		CfgBean bean = new CfgBean(url, path, name);
		try {
			RFBP fileFetch = new RFBP(bean).setIntensify(false);
			fileFetch.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bean = null;
		}
	}

	public static void main(String[] args) throws Exception {
		//String url = "http://localhost/eclipse-jee-maven.zip";
		//String url = "http://www.safe-120.com:80/sites/aoluo/WebRoot.zip";
		//String url = "ftp://y1:y1@ftp6.66ys.cn:198/[66影视www.66ys.cn]英雄本色DVD国语中字CD2.rmvb";
		String url = "http://read.pudn.com/downloads168/ebook/772168/J2EE.pdf";
		String path = "D:/tmp/fetch";
		String name = "J2EE.pdf";
		
		Client client = new Client();
		client.fetchFile(url, path, name);
		
	}
	
	@Test
	public void test_download_qq() throws Exception {
		String url = "http://www.blogjava.net/Files/huliqing/JLoading_src.rar";
		String path = "D:/tmp/fetch";
		String name = "JLoading_src.rar";
		new RFBP(url, path, name).setIntensify(true).start();
		Thread.sleep(100000 * 1000);
	}
	
	@Test
	public void test_dl_webqq() throws Exception {
		String url = "http://dl_dir.qq.com/qqfile/web/webqq/WebQQ_1.2.46.400.exe";
		String path = "D:/tmp/fetch";
		String name = "WebQQ_1.2.46.400.exe";
		new RFBP(url, path, name).setIntensify(true).start();
		Thread.sleep(100000 * 1000);
	}
	
}






