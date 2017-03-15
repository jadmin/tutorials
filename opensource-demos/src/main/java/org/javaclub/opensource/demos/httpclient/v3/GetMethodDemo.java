/*
 * @(#)HttpClientTutorial.java	2010-5-8
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.httpclient.v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * A httpclient GetMethod demo.
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: GetMethodDemo.java 37 2011-06-10 04:52:21Z gerald.chen.hz@gmail.com $
 */
public class GetMethodDemo {

	private static String url = "http://www.apache.org/";

	public static void main(String[] args) {
		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
		// timeout ---> one minute
		client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
		List<Header> headers = new ArrayList<Header>();  
        headers.add(new Header("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)"));  
        client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);

		// Create a method instance.
		GetMethod method = new GetMethod(url);
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
		method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

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
