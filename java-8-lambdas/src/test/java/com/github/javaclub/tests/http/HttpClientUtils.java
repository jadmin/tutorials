package com.github.javaclub.tests.http;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.*;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.CodingErrorAction;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {


    private static CloseableHttpClient httpClient = null;

    private static final int CONNECTION_REQUEST_TIMEOUT = 5000;// ms毫秒,从池中获取链接超时时间
    private static final int CONNECT_TIMEOUT = 5000;// ms毫秒,建立链接超时时间
    private static final int SOCKET_TIMEOUT = 30000;// ms毫秒,读取超时时间
    private static final int MAX_TOTAL = 200;// 最大总并发
    private static final int MAX_PER_ROUTE = 100;// 每路并发

    private static final String CHARSET = "utf-8";// 编码


    private HttpClientUtils() {

    }

    static {
        try {
            //enable ssl
            TrustManager x509m = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{x509m}, null);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", socketFactory).register("http", PlainConnectionSocketFactory.INSTANCE).build();
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            // http连接池设置socket属性
            SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
            connManager.setDefaultSocketConfig(socketConfig);

            //http连接池设置connection属性
            MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(2000).build();
            ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).setMessageConstraints(messageConstraints).build();
            connManager.setDefaultConnectionConfig(connectionConfig);

            //http连接池设置并发属性
            connManager.setMaxTotal(MAX_TOTAL);
            connManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);

            //默认请求设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(CONNECTION_REQUEST_TIMEOUT)
                    .setSocketTimeout(CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(SOCKET_TIMEOUT)
                    .build();

            httpClient = HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(requestConfig).build();
        } catch (Exception e) {
            throw new IllegalStateException("初始化httpClient异常", e);
        }

    }

    public static String doGet(String url) {
        return doGet(url, null);
    }

    public static String doGet(String url, Map<String, Object> params) {
        try {
            URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue().toString());
                }
            }
            URI uri = builder.build();
            String responseStr = doExecute(new HttpGet(uri));
            // logger.info("get request--->{} , params--->{},response--->{}", uri, params, responseStr);
            return responseStr;
        } catch ( URISyntaxException e) {
            throw new RuntimeException("get请求失败", e);
        }

    }

    public static String doPost(String url) {
        return doPost(url, null);
    }

    public static String doPost(String url, Map<String, Object> params) {
        try {
            HttpPost httpPost = new HttpPost(url);
            if (params != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, CHARSET);
                httpPost.setEntity(entity);
            }
            String responseStr = doExecute(httpPost);
            // logger.info("post form request--->{} , params--->{},response--->{}", url, params, responseStr);
            return responseStr;

        } catch (Exception e) {
            throw new RuntimeException("post请求失败", e);
        }
    }


    public static String doPostJson(String url, String json) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        String responseStr = doExecute(httpPost);
        // logger.info("post json request--->{} , params--->{},response--->{}", url, TextUtils.filter(json), TextUtils.filter(responseStr));
        return responseStr;
    }


    //执行请求
    private static String doExecute(HttpUriRequest request) {
        try (CloseableHttpResponse response = httpClient.execute(request);) {
            HttpEntity entity = response.getEntity();
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    String responseContent = EntityUtils.toString(entity, CHARSET);
                    return responseContent;
                }
                // logger.error("something error when get resource,status---> {},message--->{}", statusCode, response.getStatusLine().getReasonPhrase());
                throw new RuntimeException("请求服务器异常");
            } finally {
                if (entity != null) {
                    entity.getContent().close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("doExecute error:", e);
        }
    }

}
