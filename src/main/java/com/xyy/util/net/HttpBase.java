package com.xyy.util.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

class HttpBase {
	
	public static final String CHARSET_UTF8 = "UTF-8";// 编码格式
	public static final String HTTPTYPE_HTTP="HTTP";
	public static final String HTTPTYPE_HTTPS="HTTPS";

	private static final int connectTimeoutLimit = 60000;// 连接超时限制

	private static final int socketTimeoutLimt = 15000;// 通信超时限制
	
	
	public static String httpGet(String url, Map<String, String> header, Map<String, String> params, String charset) {
		return doGet(HTTPTYPE_HTTP, url, header, params, charset);
	}

	public static String httpsGet(String url, Map<String, String> header, Map<String, String> params, String charset) {
		return doGet(HTTPTYPE_HTTPS, url, header, params, charset);
	}
	
	public static String httpPost(String url, Map<String, String> header, String requestBody, String charset) {
		return doPost(HTTPTYPE_HTTP, url, header, requestBody, charset);

	}
	public static String httpPost(String url, Map<String, String> header, Map<String, String> params, String charset) {
		return doPost(HTTPTYPE_HTTP, url, header, params, charset);

	}
	public static String httpsPost(String url, Map<String, String> header, String requestBody, String charset) {
		return doPost(HTTPTYPE_HTTPS, url, header, requestBody, charset);
	}
	
	public static String httpsPost(String url, Map<String, String> header, Map<String, String> params, String charset) {
		return doPost(HTTPTYPE_HTTPS, url, header, params, charset);
	}
	
	
	private static CloseableHttpClient createClint() {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(HttpBase.connectTimeoutLimit)
				.setSocketTimeout(HttpBase.socketTimeoutLimt).build();
		return HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	}

	/**
	 * 初始化信任所有的SSL
	 * 
	 * @return
	 */
	private static CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		}
	}

	
	public static String encodedParameter(Map<String, String> params, String charset) {
		String parameterStr = "";
		HttpEntity httpEntity=formEntity(params, charset);
		if(httpEntity!=null){
			try {
				parameterStr = EntityUtils.toString(httpEntity);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return parameterStr;
	}
	
	public static HttpEntity formEntity(Map<String, String> params,String charset){
		List<NameValuePair> pairs = null;
		HttpEntity httpEntity=null;
		if (params != null && !params.isEmpty()) {
			pairs = new ArrayList<NameValuePair>(params.size());
			for (Map.Entry<String, String> entry : params.entrySet()) {
				String value = entry.getValue();
				if (value != null) {
					pairs.add(new BasicNameValuePair(entry.getKey(), value));
				}
			}
		}
		if(pairs!=null){
			try {
				httpEntity=new UrlEncodedFormEntity(pairs, charset);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		return httpEntity;
	}
	
	private static String doGet(String httpType, String url, Map<String, String> header, Map<String, String> params, String charset){
		Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "httpGet request url can not be null");
		if (Strings.isNullOrEmpty(charset)) {
			charset = HttpBase.CHARSET_UTF8;
		}
		String parameterStr = HttpBase.encodedParameter(params, charset);
		if(!Strings.isNullOrEmpty(parameterStr)){
			if (url.contains("?")) {
				url += "&" + parameterStr;
			} else {
				url += "?" + parameterStr;
			}
		}
		CloseableHttpClient httpclient = null ;
		if(HTTPTYPE_HTTP.equals(httpType)){
			httpclient=createClint();
		}
		if(HTTPTYPE_HTTPS.equals(httpType)){
			httpclient=createSSLClientDefault();
		}
		HttpGet httpGet = new HttpGet(url);
		if (header != null && !header.isEmpty()) {
			for (String key : header.keySet()) {
				httpGet.setHeader(key, header.get(key));
			}
		}
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
			//int responseCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			String body = EntityUtils.toString(entity, charset);
			EntityUtils.consume(entity);
			//if (responseCode == 200) {
				return body;
			//}
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			httpGet.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	

	private static String doPost(String httpType,String url, Map<String, String> header, Map<String, String> params, String charset) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "httpPost request url can not be null");
		if (Strings.isNullOrEmpty(charset)) {
			charset = HttpBase.CHARSET_UTF8;
		}
		HttpEntity requestEntity=formEntity(params, charset);
		CloseableHttpClient httpclient = null ;
		if(HTTPTYPE_HTTP.equals(httpType)){
			httpclient=createClint();
		}
		if(HTTPTYPE_HTTPS.equals(httpType)){
			httpclient=createSSLClientDefault();
		}
		HttpPost httpPost = new HttpPost(url);
		if (header != null && !header.isEmpty()) {
			for (String key : header.keySet()) {
				httpPost.setHeader(key, header.get(key));
			}
		}
		if(requestEntity!=null){
			httpPost.setEntity(requestEntity);
		}
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			//int responseCode = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			String body = EntityUtils.toString(responseEntity, charset);
			EntityUtils.consume(responseEntity);
			//if (responseCode == 200) {
				return body;
			//}
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			httpPost.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}
	
	private static String doPost(String httpType, String url, Map<String, String> header, String requestBody, String charset) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(url), "httpPost request url can not be null");
		if (Strings.isNullOrEmpty(charset)) {
			charset = HttpBase.CHARSET_UTF8;
		}
		HttpEntity requestEntity=null;
		if(!Strings.isNullOrEmpty(requestBody)){
			try {
				requestEntity=new StringEntity(requestBody, charset);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		CloseableHttpClient httpclient = null ;
		if(HTTPTYPE_HTTP.equals(httpType)){
			httpclient=createClint();
		}
		if(HTTPTYPE_HTTPS.equals(httpType)){
			httpclient=createSSLClientDefault();
		}
		HttpPost httpPost = new HttpPost(url);
		if (header != null && !header.isEmpty()) {
			for (String key : header.keySet()) {
				httpPost.setHeader(key, header.get(key));
			}
		}
		if(requestEntity!=null){
			httpPost.setEntity(requestEntity);
		}
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			//int responseCode = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			String body = EntityUtils.toString(responseEntity, charset);
			EntityUtils.consume(responseEntity);
			//if (responseCode == 200) {
				return body;
			//}
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			httpPost.releaseConnection();
			try {
				httpclient.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}
	

}
