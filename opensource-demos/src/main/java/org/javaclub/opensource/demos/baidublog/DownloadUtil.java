package org.javaclub.opensource.demos.baidublog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Util for downloading url resource and try to detect encoding automatically.
 * 
 * @author gerald
 */
public final class DownloadUtil {

    private static final byte[] CHARSET_SINGLE;
    private static final byte[] CHARSET_DOUBLE;

    private static final byte[] UTF8;
    private static final byte[] UTF8_LOWERCASE;
    private static final byte[] UNICODE;
    private static final byte[] UNICODE_LOWERCASE;
    private static final byte[] GB2312;
    private static final byte[] GB2312_LOWERCASE;
    private static final byte[] GBK;
    private static final byte[] GBK_LOWERCASE;

    static {
        HttpURLConnection.setFollowRedirects(false);
        try {
            CHARSET_SINGLE = "charset=".getBytes();
            CHARSET_DOUBLE = "charset=".getBytes("UNICODE");
            UTF8 = "UTF-8".getBytes("UTF-8");
            UTF8_LOWERCASE = "utf-8".getBytes("UTF-8");
            UNICODE = "UNICODE".getBytes("UNICODE");
            UNICODE_LOWERCASE = "unicode".getBytes("UNICODE");
            GB2312 = "GB2312".getBytes("GB2312");
            GB2312_LOWERCASE = "gb2312".getBytes("GB2312");
            GBK = "GBK".getBytes("GBK");
            GBK_LOWERCASE = "gbk".getBytes("GBK");
        }
        catch(UnsupportedEncodingException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static final int MAX_SIZE = 1024 * 1024 * 18; // 18MB

    private static final String ACCEPT = "*/*";
    private static final String USER_AGENT = "Mozilla/4.0 (compatible; Windows XP 5.1; MSIE 6.0.2900.2180)";
    private static final String ACCEPT_ENCODING = "gzip";

    private static final Log log = LogFactory.getLog(DownloadUtil.class);
    public static final String DEFAULT_ENCODING = "UTF-8";

    public static HttpResponse download(String url) {
        return download(url, null, 0, DEFAULT_ENCODING);
    }

    public static HttpResponse download(String url, String defaultEncoding) {
        return download(url, null, 0, defaultEncoding);
    }

    public static HttpResponse download(String url, String referer, long ifModifiedSince) {
        return download(url, referer, ifModifiedSince, DEFAULT_ENCODING);
    }
    /**
     * Download specified url with referer header, ifModifiedSince header.
     */
    public static HttpResponse download(String url, String referer, long ifModifiedSince, String defaultEncoding) {
        if (url==null)
            throw new NullPointerException("Null pointer of url.");
        if (!url.startsWith("http://"))
            throw new IllegalArgumentException("Bad url: " + url);
        int retry = 3;
        int responseCode = 0;
        while (retry>0) {
            log.info("[try:" + (4-retry) + "] Start fetch: " + url);
            HttpURLConnection hc = null;
            InputStream input = null;
            try {
                hc = (HttpURLConnection) new URL(url).openConnection();
                hc.setRequestMethod("GET");
                hc.setUseCaches(false);
                hc.setConnectTimeout(60000);
                hc.addRequestProperty("Accept", ACCEPT);
                hc.addRequestProperty("User-Agent", USER_AGENT);
                hc.addRequestProperty("Accept-Encoding", ACCEPT_ENCODING);
                if (referer!=null)
                    hc.addRequestProperty("Referer", referer);
                if (ifModifiedSince>0)
                    hc.setIfModifiedSince(ifModifiedSince);
                hc.connect();
                responseCode = hc.getResponseCode();
                log.info("Code: " + responseCode);
                String realUrl = hc.getHeaderField("Content-Location");
                if(realUrl==null)
                    realUrl = url;
                log.info("URL: " + realUrl);
                if (responseCode == HttpResponse.SC_OK) {
                    // handle content:
                    String contentType = hc.getContentType();
                    log.info("Got Content-Type: " + (contentType==null ? "(null)" : contentType));
                    // try to detect encoding:
                    String contentEncoding = null;
                    if(contentType!=null) {
                        int pos = contentType.indexOf("charset=");
                        if(pos!=(-1)) {
                            contentEncoding = contentType.substring(pos + "charset=".length());
                            int sp = contentType.indexOf(';');
                            if(sp!=(-1))
                                contentType = contentType.substring(0, sp).trim();
                        }
                    }
                    log.info("Detect encoding: " + (contentEncoding==null ? "(null)" : contentEncoding));
                    boolean gzip = "gzip".equals(hc.getContentEncoding());
                    input = hc.getInputStream();
                    ByteArrayOutputStream output = new ByteArrayOutputStream(10240);
                    boolean overflow = false;
                    int read = 0;
                    byte[] buffer = new byte[1024];
            		int n = 0;
            		while (-1 != (n = input.read(buffer))) {
            			output.write(buffer, 0, n);
            			read += n;
            			if(read > MAX_SIZE) {
                            overflow = true;
                            break;
                        }
            		}
                    
                    output.close();
                    if(overflow) {
                        log.info("Fetch content but overflow!");
                        return HttpResponse.notFound(url);
                    }
                    else {
                        log.info("Fetch content ok: " + (read / 1024) + " kB.");
                    }
                    byte[] data = output.toByteArray();
                    if(gzip) {
                        data = GZipUtil.gunzip(data);
                        if(data.length>MAX_SIZE) {
                            log.info("Fetch content ok but overflow after gunzip!");
                            return HttpResponse.notFound(url);
                        }
                    }
                    // check text or binary:
                    if (HttpResponse.isText(contentType)) {
                        // detect encoding:
                        if(contentEncoding!=null)
                            return HttpResponse.ok(url, contentType, contentEncoding, data);
                        int singleChar = indexOf(data, CHARSET_SINGLE, 0);
                        if(singleChar>0) {
                            int start = singleChar + CHARSET_SINGLE.length;
                            if(startsFrom(data, GB2312, start) || startsFrom(data, GB2312_LOWERCASE, start))
                                contentEncoding = "GB2312";
                            if(startsFrom(data, UTF8, start) || startsFrom(data, UTF8_LOWERCASE, start))
                                contentEncoding = "UTF-8";
                            if(startsFrom(data, GBK, start) || startsFrom(data, GBK_LOWERCASE, start))
                                contentEncoding = "GBK";
                        }
                        else {
                            int doubleChar = indexOf(data, CHARSET_DOUBLE, 0);
                            if(doubleChar>0) {
                                int start = singleChar + CHARSET_DOUBLE.length;
                                if(startsFrom(data, UNICODE, start) || startsFrom(data, UNICODE_LOWERCASE, start))
                                    contentEncoding = "UNICODE";
                            }
                        }
                        log.info("Detect encoding from content: " + contentEncoding);
                        if (contentEncoding==null)
                            contentEncoding = defaultEncoding;
                        return HttpResponse.ok(url, contentType, contentEncoding, data);
                    }
                    return HttpResponse.ok(url, contentType, null, data);
                }
                if (HttpResponse.isNotModified(responseCode)) {
                    return HttpResponse.notModified(url, ifModifiedSince);
                }
                if (HttpResponse.isRedirect(responseCode)) {
                    String location = hc.getHeaderField("Location");
                    String redirect = null;
                    if(location!=null) {
                        if(location.startsWith("http://")) {
                            redirect = location;
                        }
                        else {
                            if(location.startsWith("/")) {
                                // absolute path:
                                int p = url.indexOf('/', "http://".length());
                                redirect = url.substring(0, p) + location;
                            }
                            else {
                                // relative path:
                                int p = url.lastIndexOf('/');
                                redirect = url.substring(0, p+1) + location;
                            }
                        }
                        return HttpResponse.redirect(url, redirect);
                    }
                    // bad location:
                    return HttpResponse.notFound(url);
                }
                // all other response code:
                // retry:
                retry--;
                sleep(10);
            }
            catch(SocketTimeoutException e) {
                log.info("Timeout.");
                retry--;
                sleep(10);
            }
            catch(IOException e) {
                retry--;
                sleep(10);
            }
            finally {
                if(input!=null) {
                    try { input.close(); } catch (IOException e) {}
                }
                if(hc!=null) {
                    hc.disconnect();
                }
            }
        }
        return HttpResponse.notFound(url);
    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        }
        catch(InterruptedException e) {
        }
    }

    /**
     * We do a byte-match just like String.indexOf().
     */
    private static int indexOf(byte[] source, byte[] target, int start) {
        if(source==null || target==null)
            return (-1);
        for(int i=start; i<source.length-target.length; i++) {
            // test from source[i] and target:
            boolean match = true;
            for(int j=0; j<target.length; j++) {
                if(source[i+j]!=target[j]) {
                    match = false;
                    break;
                }
            }
            if(match)
                return i;
        }
        return (-1);
    }

    /**
     * We do a byte-match just like String.startsWith() but with an extra 
     * "start" parameter.
     */
    public static boolean startsFrom(byte[] source, byte[] target, int start) {
        if(source==null || target==null)
            return false;
        if(source.length-start < target.length)
            return false;
        for(int i=0; i<target.length; i++) {
            if(source[i+start]!=target[i])
                return false;
        }
        return true;
    }

    public static void main(String[] args) {
    	String url = "http://hi.baidu.com/jadmin";
    	String response = DownloadUtil.download(url).getText();
    	System.out.println(response);
    }
}
