/*
 * @(#)ImageFetcher.java	2010-5-8
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package org.javaclub.opensource.demos.httpclient.v3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A utility class for fetching resource from url(s).
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: UrlFetcher.java 37 2011-06-10 04:52:21Z gerald.chen.hz@gmail.com $
 */
public class UrlFetcher {
	/** Logger for this class */
	protected static final Log LOG = LogFactory.getLog(UrlFetcher.class);
	
	/**
	 * 抓取网络上的图片等资源文件
	 *
	 * @param absoluteUrl 网络资源的绝对url地址
	 * @param dest 存放图片的目录
	 * @throws JawaException 
	 */
	public static File fetch(String absoluteUrl, String dest, boolean keepName) throws Exception {
		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
		// timeout ---> one minute
		client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)"));
		client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);

		// Create a method instance.
		GetMethod method = new GetMethod(absoluteUrl);
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
		method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

		OutputStream output = null;
		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				LOG.error("fetch(String, String, boolean) - Method failed: " + method.getStatusLine(), null); //$NON-NLS-1$
			}
			// Read the response body.
			byte[] responseBody = method.getResponseBody();
			String ext = absoluteUrl.substring(absoluteUrl.lastIndexOf(".") + 1);
			String file = dest + File.separator + UUID.randomUUID().toString().replace("-", "") + "." + ext;
			if(keepName) {
				file = dest + File.separator + absoluteUrl.substring(absoluteUrl.lastIndexOf("/") + 1);
			}
			output = new FileOutputStream(new File(file));
			output.write(responseBody);
			output.flush();
			return new File(file);
		} catch (Exception e) {
			LOG.error("fetch(String, String, boolean)", e); //$NON-NLS-1$
			throw new Exception(e);
		} finally {
			// Release the connection.
			method.releaseConnection();
			if(null != output) {
				output.close();
			}
		}
	}

	public static void multiFetch(String[] absoluteUrls, String dest, boolean keepName) {
		// Create an HttpClient with the MultiThreadedHttpConnectionManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
		HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)"));
		httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        
        // create a thread for each URI
        GetThread[] threads = new GetThread[absoluteUrls.length];
        for (int i = 0; i < threads.length; i++) {
            GetMethod get = new GetMethod(absoluteUrls[i]);
            get.setFollowRedirects(true);
            threads[i] = new GetThread(httpClient, get, absoluteUrls[i], dest, keepName, i + 1);
        }
        
        // start the threads
        for (int j = 0; j < threads.length; j++) {
            threads[j].start();
        }
	}
	
	/**
     * A thread that performs a GET.
     */
    static class GetThread extends Thread {
        
        private HttpClient httpClient;
        private GetMethod method;
        private String url;
        private String dest;
        private boolean keepName;
        private int id;
        
        public GetThread(HttpClient httpClient, GetMethod method, String url, String dest, boolean keepName, int id) {
            this.httpClient = httpClient;
            this.method = method;
            this.url = url;
            this.dest = dest;
            this.keepName = keepName;
            this.id = id;
        }
        
        /**
         * Executes the GetMethod and prints some satus information.
         */
        public void run() {
        	OutputStream output = null;
            try {
            	System.out.println(id + " - about to get something from " + method.getURI());
				
                // execute the method
                httpClient.executeMethod(method);
                System.out.println(id + " - get executed");
				
                // get the response body as an array of bytes
                byte[] bytes = method.getResponseBody();
                System.out.println(id + " - " + bytes.length + " bytes read");
                
				String ext = url.substring(url.lastIndexOf("."));
				String file = dest + File.separator + UUID.randomUUID().toString().replace("-", "") + ext;
				if(keepName) {
					file = dest + File.separator + url.substring(url.lastIndexOf("/") + 1);
				}
				output = new FileOutputStream(new File(file));
				output.write(bytes);
				output.flush();
            } catch (Exception e) {
            	if(LOG.isWarnEnabled()) {
            		LOG.warn("Thread - " + id + " - error: " + e);
            	}
            } finally {
                // always release the connection after we're done 
                method.releaseConnection();
                if(null != output) {
    				try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
    			}
                System.out.println(id + " - connection released");
            }
        }
       
    }

	public static void main(String[] args) throws Exception {
		String dest = "D:/temp/digfile";
		String image = "http://hc.apache.org/httpcomponents-client/tutorial/html/fundamentals.html";
		File file = fetch(image, dest, true);
		System.out.println(file.getAbsolutePath());
		
		String[] urls = new String[] {
				"http://hiphotos.baidu.com/wuyaxue/pic/item/6f064a1681c23f5821a4e995.jpg",
				"http://image5.poco.cn/mypoco/myphoto/20080722/09/7892110200807220858324211877189405_000_640.jpg",
				"http://image5.poco.cn/mypoco/myphoto/20080722/09/7892110200807220858324211877189405_001_640.jpg",
				"http://hiphotos.baidu.com/wuyaxue/pic/item/e7d424f5abb5e837bd31093f.jpg",
				"http://hiphotos.baidu.com/wuyaxue/pic/item/1501cacebc398c1193457eb0.jpg",
				"http://hiphotos.baidu.com/475679303/pic/item/8f44ec22a7f1d169925807bb.jpg",
				"http://image5.poco.cn/mypoco/myphoto/20080722/09/7892110200807220858324211877189405_002_640.jpg",
				"http://hiphotos.baidu.com/wuyaxue/pic/item/2acba6c3cf3c6d47b219a87a.jpg",
				"http://hiphotos.baidu.com/wjx78606303/pic/item/eb2529c68720fef7d0006057.jpg",
				"http://image.club.sohu.com/pic/3c/fb/baguab017e73a5f21114c.jpg",
				"http://file.we54.com/pic2/20080429/tj/7/5.jpg",
				"http://file.we54.com/pic2/20080429/tj/7/9.jpg",
				"http://hiphotos.baidu.com/wuyaxue/pic/item/3639be45f167f234cefca3bb.jpg",
				"http://hiphotos.baidu.com/ydcdong/pic/item/1dc79fdafa4ce3cab6fd48f9.jpg"
			};
		dest = "D:/temp/digfile";
		UrlFetcher.multiFetch(urls, dest, true);
		
		
	}
}
