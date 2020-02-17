package com.dkha.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.config.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Package: com.sishuok.es.zkp2p.app.controller.util Description:
 * HttpClientUtilS http请求工具类 Author: Boonie Create: Boonie(2015-11-25 15:05)
 */
public class HttpClientUtils {

	private final static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	private static PoolingHttpClientConnectionManager connManager = null;
	private static CloseableHttpClient httpclient = null;
	public final static int DefaultConnectTimeout = 120 * 1000; // 默认的超时时间

	static {
		try {
			SSLContext sslContext = SSLContexts.custom().build();
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} }, null);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", new SSLConnectionSocketFactory(sslContext)).build();

			connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

			HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
				@Override
				public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
					if (executionCount > 5) {
						return false;
					}

					HttpRequest request = (HttpRequest) context.getAttribute(HttpCoreContext.HTTP_REQUEST);
					if (request.getRequestLine().getMethod().equals(HttpPost.METHOD_NAME)) {
						try {
							logger.warn("retry --- {} --- request---->{},Data---->{}", executionCount,
									(HttpRequestWrapper.wrap(request)).getURI(),
									EntityUtils.toString((((HttpEntityEnclosingRequest) request)).getEntity()));
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						logger.warn("retry --- {} --- request---->{}", executionCount,
								request.getRequestLine().getUri());
					}

					if (exception instanceof InterruptedIOException) {
						// Timeout
						return true;
					}
					if (exception instanceof UnknownHostException) {
						// Unknown host
						return true;
					}
					if (exception instanceof ConnectTimeoutException) {
						// Connection refused
						return true;
					}
					if (exception instanceof SSLException) {
						// SSL handshake exception
						return true;
					}
					if (exception instanceof HttpHostConnectException) {
						return true;
					}
					if (exception instanceof SocketTimeoutException) {
						return true;
					}
					if (exception instanceof SocketException) {
						return true;
					}
					if (exception instanceof IOException) {
						return true;
					}
					return false;
				}

			};

			httpclient = HttpClients.custom().setConnectionManager(connManager).setRetryHandler(retryHandler).build();

			// Create socket configuration
			SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).setSoTimeout(DefaultConnectTimeout)
					.build();
			connManager.setDefaultSocketConfig(socketConfig);
			// Create message constraints
			MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
					.setMaxLineLength(2000).build();
			// Create connection configuration
			ConnectionConfig connectionConfig = ConnectionConfig.custom()
					.setMalformedInputAction(CodingErrorAction.IGNORE)
					.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
					.setMessageConstraints(messageConstraints).build();
			connManager.setDefaultConnectionConfig(connectionConfig);
			connManager.setMaxTotal(200);
			connManager.setDefaultMaxPerRoute(25);
		} catch (KeyManagementException e) {
			logger.error("KeyManagementException", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("NoSuchAlgorithmException", e);
		}
	}

	/**
	 * 发起application/json请求
	 * 
	 * @param url
	 * @param json
	 * @param encoding
	 * @return
	 */
	public static String postJson(String url, String json, String encoding, int timeout) {
		HttpPost post = new HttpPost(url);
		try {
			int time = DefaultConnectTimeout;
			if (timeout != 0) {
				time = timeout;
			}
			post.setHeader("Content-type", "application/json");
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(DefaultConnectTimeout)
					.setConnectTimeout(DefaultConnectTimeout).setConnectionRequestTimeout(time)
					.setExpectContinueEnabled(false).build();
			post.setConfig(requestConfig);
			post.setEntity(new StringEntity(json, encoding));
			logger.warn("[HttpUtils Json Post] begin invoke url:" + url + " , params:" + json);
			CloseableHttpResponse response = httpclient.execute(post);
			try {
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						String str = EntityUtils.toString(entity, encoding);
						logger.warn("[HttpUtils Json Post]Debug response, url :" + url + " , response string :" + str);
						return str;
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException", e);
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
			post.releaseConnection();
		}
		return "";
	}

	public static String postJsonBody(String url, int timeout, Map<String, Object> map, String encoding) {
		HttpPost post = new HttpPost(url);
		try {
			post.setHeader("Content-type", "application/json");
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
					.setConnectionRequestTimeout(timeout).setExpectContinueEnabled(false).build();
			post.setConfig(requestConfig);
			String str1 = JSON.toJSONString(map).replace("\\", "");
			post.setEntity(new StringEntity(str1, encoding));
			logger.warn("[HttpUtils Post] begin invoke url:" + url + " , params:" + str1);
			CloseableHttpResponse response = httpclient.execute(post);
			try {
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						String str = EntityUtils.toString(entity, encoding);
						logger.warn("[HttpUtils Post]Debug response, url :" + url + " , response string :" + str);
						return str;
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException", e);
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
			post.releaseConnection();
		}
		return "";
	}

	public static String invokeGet(String url, Map<String, Object> params, Integer connectTimeout) {
		String responseString = null;
		if (null == connectTimeout || connectTimeout < 0) {
			connectTimeout = DefaultConnectTimeout;
		}

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeout)
				.setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout).build();

		StringBuilder sb = new StringBuilder();
		sb.append(url);
		int i = 0;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (i == 0 && !url.contains("?")) {
				sb.append("?");
			} else {
				sb.append("&");
			}
			sb.append(entry.getKey());
			sb.append("=");
			String value = entry.getValue() + "";
			try {
				sb.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				logger.warn("encode http get params error, value is " + value, e);
				sb.append(URLEncoder.encode(value));
			}
			i++;
		}
		logger.warn("[HttpUtils Get] begin invoke:" + sb.toString());
		HttpGet get = new HttpGet(sb.toString());
		get.setConfig(requestConfig);

		try {
			CloseableHttpResponse response = httpclient.execute(get);
			try {
				HttpEntity entity = response.getEntity();
				try {
					if (entity != null) {
						responseString = EntityUtils.toString(entity, "utf-8");
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} catch (Exception e) {
				logger.error(String.format("[HttpUtils Get]get response error, url:%s", sb.toString()), e);
				return responseString;
			} finally {
				if (response != null) {
					response.close();
				}
			}
			logger.warn(
					String.format("[HttpUtils Get]Debug url:%s , response string %s:", sb.toString(), responseString));
		} catch (SocketTimeoutException e) {
			logger.error(String.format("[HttpUtils Get]invoke get timout error, url:%s", sb.toString()), e);
			return responseString;
		} catch (Exception e) {
			logger.error(String.format("[HttpUtils Get]invoke get error, url:%s", sb.toString()), e);
		} finally {
			get.releaseConnection();
		}
		return responseString;
	}

	/**
	 * HTTPS请求
	 * 
	 * @param reqURL
	 * @param params
	 * @return
	 */
	public static String connectPostHttps(String reqURL, Map<String, Object> params, Integer connectTimeout) {

		String responseContent = null;
		if (null == connectTimeout || connectTimeout < 0) {
			connectTimeout = DefaultConnectTimeout;
		}

		HttpPost httpPost = new HttpPost(reqURL);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeout)
					.setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout).build();

			List<NameValuePair> formParams = new ArrayList<NameValuePair>();

			httpPost.setConfig(requestConfig);
			// 绑定到请求 Entry
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String name = entry.getKey();
				String value = ConvertUtils.convert(entry.getValue());
				if (StringUtils.isNotBlank(name)) {
					formParams.add(new BasicNameValuePair(name, value));
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));
			logger.warn("requestURI : " + httpPost.getURI() + ", requestContent: "
					+ EntityUtils.toString(httpPost.getEntity()));
			System.out.println("requestURI : " + httpPost.getURI() + ", requestContent: "
					+ EntityUtils.toString(httpPost.getEntity()));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				// 执行POST请求
				HttpEntity entity = response.getEntity(); // 获取响应实体
				try {
					if (null != entity) {
						responseContent = EntityUtils.toString(entity, Consts.UTF_8);
					}
				} finally {
					if (entity != null) {
						entity.getContent().close();
					}
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
			logger.warn("requestURI : " + httpPost.getURI() + ", responseContent: " + responseContent);
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		} finally {
			httpPost.releaseConnection();
		}
		return responseContent;
	}
}
