package org.javaclub.opensource.demos.baidublog;

import java.io.UnsupportedEncodingException;

/**
 * A wrap object for a http response.
 * 
 * @author jadmin
 */
public final class HttpResponse {

	private int code = 0; // store response code, default is 0, not fetched yet.
	private String url = null; // store url
	private String redirectUrl = null; // store redirect url
	private byte[] contentData = null; // default is null
	private long ifModifiedSince = 0; // default is 0
	private String contentType = null; // default is null: unknown type.
	private int contentLength = 0; // default is 0
	private String contentEncoding = null; // default is null
	
	public static final int SC_TEMPORARY_REDIRECT = 307;
	public static final int SC_MOVED_TEMPORARILY = 302;
	public static final int SC_MOVED_PERMANENTLY = 301;
	public static final int SC_NOT_MODIFIED = 304;
	public static final int SC_NOT_FOUND = 404;
	public static final int SC_OK = 200;

	static HttpResponse notFound(String url) {
		HttpResponse response = new HttpResponse();
		response.code = HttpResponse.SC_NOT_FOUND;
		response.url = url;
		return response;
	}

	static HttpResponse ok(String url, String contentType, String contentEncoding,
			byte[] contentData) {
		HttpResponse response = new HttpResponse();
		response.code = HttpResponse.SC_OK;
		response.url = url;
		response.contentType = contentType;
		response.contentEncoding = contentEncoding;
		// set content data:
		if (contentData != null && contentData.length == 0)
			contentData = null;
		response.contentData = contentData;
		// calculate content length:
		if (contentData == null) {
			response.contentLength = 0;
		} else {
			response.contentLength = contentData.length;
		}
		return response;
	}

	static HttpResponse redirect(String url, String redirectUrl) {
		HttpResponse response = new HttpResponse();
		response.code = HttpResponse.SC_MOVED_TEMPORARILY;
		response.url = url;
		response.redirectUrl = redirectUrl;
		return response;
	}

	static HttpResponse notModified(String url, long ifModifiedSince) {
		HttpResponse response = new HttpResponse();
		response.code = HttpResponse.SC_NOT_MODIFIED;
		response.url = url;
		response.ifModifiedSince = ifModifiedSince;
		return response;
	}

	private HttpResponse() {
	}

	public int getCode() {
		return code;
	}

	public String getUrl() {
		return url;
	}

	public static boolean isNotFound(int code) {
		return code == HttpResponse.SC_NOT_FOUND;
	}

	public boolean isNotFound() {
		return isNotFound(code);
	}

	public static boolean isText(String contentType) {
		return contentType != null
				&& (contentType.startsWith("text/") || contentType
						.startsWith("application/xhtml+xml"));
	}

	public boolean isText() {
		return contentType != null && isText(contentType);
	}

	public boolean isBinary() {
		return !isText(contentType);
	}

	public static boolean isOk(int code) {
		return code == HttpResponse.SC_OK;
	}

	public boolean isOk() {
		return isOk(code);
	}

	public static boolean isNotModified(int code) {
		return code == HttpResponse.SC_NOT_MODIFIED;
	}

	public boolean isNotModified() {
		return isNotModified(code);
	}

	public static boolean isRedirect(int code) {
		return code == HttpResponse.SC_TEMPORARY_REDIRECT
				|| code == HttpResponse.SC_MOVED_PERMANENTLY
				|| code == HttpResponse.SC_MOVED_TEMPORARILY;
	}

	public boolean isRedirect() {
		return isRedirect(code);
	}

	/**
	 * Return content data, maybe null for empty content data or content data is unavailable.
	 */
	public byte[] getContentData() {
		if (contentData != null && contentData.length == 0)
			return null;
		return contentData;
	}

	/**
	 * Return content encoding, maybe null.
	 */
	public String getContentEncoding() {
		return contentEncoding;
	}

	public String getText() {
		if (isText()) {
			String encoding = contentEncoding == null ? "UTF-8" : contentEncoding;
			try {
				return new String(contentData, encoding);
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException(
						"Bad encoding when convert to String.", e);
			}
		}
		throw new IllegalArgumentException("Not text content.");
	}

	public int getContentLength() {
		return contentLength;
	}

	public long getIfModifiedSince() {
		return ifModifiedSince;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public String getContentType() {
		return contentType;
	}
}
