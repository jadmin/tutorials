/*
 * @(#)PostDemo.java	2011-10-14
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.httpclient.v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.junit.Test;

/**
 * PostDemo
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: PostDemo.java 2011-10-14 下午09:04:53 Exp $
 */
public class PostDemo {

	@Test
	public void sendmail() {
		String url = "http://javaclub.sourceforge.net/mail.php";
		/*
		 * 在使用Httpclient的过程中，线上的酒店出现过一个问题，就是当访问量增大的时候，
		 * 会发现本地的连接等待时间急剧增加，例如从400ms增加到 78000ms，之前一直以为是航信系统问题，
		 * 后面经过检查才发现，原来是本地httpclient设置时，最大连接数采用了默认设置的原因，而默认的最大连接数只有2个，
		 * 所以当有大量连接需要建立时，大多数连接只有等待。后面将连接数设置修改成32个之后，这个响应时间就基本上很少出现很大的时候。 
		 */
		HttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = httpConnectionManager.getParams();
        params.setConnectionTimeout(60000);
        params.setSoTimeout(20000);
        params.setDefaultMaxConnectionsPerHost(2);//very important!!
        params.setMaxTotalConnections(256);//very important!!
        
        HttpClient client = new HttpClient(httpConnectionManager);
        // 设置编码
        client.getParams().setContentCharset("UTF-8");
        client.getParams().setHttpElementCharset("UTF-8");
        
        // 设置请求头
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)"));
		client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
		
		// Create a method instance.
		PostMethod method = new PostMethod(url);
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
		method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
		
		// 设置post参数
		Part[] parts = {
				new StringPart("add", "1"),
				new StringPart("username", "whpu"), 
				new StringPart("emails", "jsoft@126.com,etxp@qq.com,jstudio@qq.com"),
				new StringPart("subject", "测试HttpClient请求发送邮件"),
				new StringPart("message", "我的邮件测试内容~~~！")
		};
		method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));

		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}

			// Read the response body.
			byte[] responseBody = method.getResponseBody();

			// Deal with the response.
			// Use caution: ensure correct character encoding and is not binary
			// data
			System.out.println(new String(responseBody));

		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
	}
}
