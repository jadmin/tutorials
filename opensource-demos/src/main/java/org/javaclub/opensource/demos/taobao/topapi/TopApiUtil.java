/*
 * @(#)TopApiUtil.java	2012-3-12
 *
 * Copyright (c) 2012. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.taobao.topapi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * TopApiUtil Top API 辅助工具
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: TopApiUtil.java 2012-3-12 14:51:35 Exp $
 */
public abstract class TopApiUtil {
	
	private static final String CONTAINER_URL = "http://container.api.taobao.com/container?appkey=${appkey}";
	private static final String SESSION_PARAM_KEY = "top_session";
	private static final String LOGIN_URL = "http://login.taobao.com/member/login.jhtml";

	/**
	 * 
	 * 新的md5签名，首尾放secret。
	 * 
	 * @param secret 分配给您的APP_SECRET
	 */
	public static String md5Signature(TreeMap<String, String> params, String secret) {
		String result = null;
		StringBuffer orgin = getBeforeSign(params, new StringBuffer(secret));
		if (orgin == null) return result;
		orgin.append(secret);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = byte2hex(md.digest(orgin.toString().getBytes("utf-8")));
		} catch (Exception e) {
			throw new java.lang.RuntimeException("sign error !");
		}
		return result;
	}

	/** 连接到TOP服务器并获取数据 */
	public static String getResult(String urlStr, String content) {
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.connect();
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.write(content.getBytes("utf-8"));
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}



	/**
	 * 
	 * 二行制转字符串
	 */
	private static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs.append("0").append(stmp);
			} else {
				hs.append(stmp);
			}
		}
		return hs.toString().toUpperCase();
	}
	
	public static String getSessionKey(String appkey, String nick, String psw) {
		HttpClient httpClient = new HttpClient();
		String redirectURL = "";
		PostMethod postMethod = new PostMethod(LOGIN_URL);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"GBK");
		List<NameValuePair> nameValues = new ArrayList<NameValuePair>();
		nameValues.add(new NameValuePair("TPL_username", nick));
		nameValues.add(new NameValuePair("TPL_password", psw));
		nameValues.add(new NameValuePair("action", "Authenticator"));
		nameValues.add(new NameValuePair("TPL_redirect_url", ""));
		nameValues.add(new NameValuePair("from", "tb"));
		nameValues.add(new NameValuePair("event_submit_do_login", "anything"));
		nameValues.add(new NameValuePair("loginType", "3"));
		postMethod.setRequestBody(nameValues.toArray(new NameValuePair[nameValues.size()]));
		
		try {
			httpClient.executeMethod(postMethod);
			redirectURL = postMethod.getResponseHeader("Location").getValue();
			System.out.println("redirectURL: 1 " + redirectURL);
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
			return null;
		} finally {
			postMethod.releaseConnection();
		}
		
		String newUrl = CONTAINER_URL.replace("${appkey}", appkey);
		try {
			URI uri = new URI(newUrl);
			postMethod.setURI(uri);
			httpClient.executeMethod(postMethod);
			
			redirectURL = postMethod.getResponseHeader("Location").getValue();
			System.out.println("redirectURL: 2 " + redirectURL);
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
			return null;
		} finally {
			postMethod.releaseConnection();
		}
	
		GetMethod get = new GetMethod(redirectURL);
		/*try {
			httpClient.executeMethod(get);
		} catch (Exception e) {
			return null;
		} finally {
			get.releaseConnection();
		}*/
		return extractSessionKey(get.getQueryString(), SESSION_PARAM_KEY);
	}
	
	private static String extractSessionKey(String rsp, String key) {
		if (rsp == null)
			return null;
		Map<String, String> nameValuePair = new HashMap<String, String>();
		String[] array = rsp.split("&");
		for (int i = 0; i < array.length; i++) {
			String s = array[i];
			if (s.indexOf("=") > 0) {
				String[] nameValues = s.split("=");
				if (nameValues.length == 2) {
					nameValuePair.put(s.split("=")[0], s.split("=")[1]);
				}
			}
		}
		return nameValuePair.get(key);
	}

	/**
	 * 
	 * 添加参数的封装方法
	 */
	private static StringBuffer getBeforeSign(TreeMap<String, String> params, StringBuffer orgin) {
		if (params == null) return null;
		Map<String, String> treeMap = new TreeMap<String, String>();
		treeMap.putAll(params);
		Iterator<String> iter = treeMap.keySet().iterator();
		while (iter.hasNext()) {
			String name = (String) iter.next();
			orgin.append(name).append(params.get(name));
		}
		return orgin;
	}

	public static void main(String[] args) {
		String sessionKey = TopApiUtil.getSessionKey("12537613", "c测试账号23", "taobao1234");
		
		System.out.println(sessionKey);
	}
}
