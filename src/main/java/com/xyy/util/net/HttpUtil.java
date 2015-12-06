package com.xyy.util.net;

import java.util.Map;

public class HttpUtil {

	/**
	 * httpGet CHARSET UTF-8
	 * @param url
	 * @return
	 */
	public static String httpGet(String url) {
		return HttpBase.httpGet(url, null, null, HttpBase.CHARSET_UTF8);
	}
	
	/**
	 *  httpGet CHARSET UTF-8
	 * @param url
	 * @param header 
	 * @param params 
	 * @return
	 */
	public static String httpGet(String url, Map<String, String> header, Map<String, String> params) {
		return HttpBase.httpGet(url, header, params, HttpBase.CHARSET_UTF8);
	}
	
	/**
	 *  httpsGet CHARSET UTF-8
	 * @param url
	 * @return
	 */
	public static String httpsGet(String url) {
		return HttpBase.httpsGet(url, null, null, HttpBase.CHARSET_UTF8);
	}

	/**
	 * httpsGet CHARSET UTF-8
	 * @param url
	 * @param header
	 * @param params
	 * @return
	 */
	public static String httpsGet(String url, Map<String, String> header, Map<String, String> params) {
		return HttpBase.httpsGet(url, header, params, HttpBase.CHARSET_UTF8);
	}
	
	
	/**
	 * httpPost CHARSET UTF-8
	 * @param url
	 * @param header
	 * @param params
	 * @return
	 */
	public static String httpPost(String url, Map<String, String> header, Map<String, String> params) {
		return HttpBase.httpPost(url, header, params, HttpBase.CHARSET_UTF8);
	}
	
	/**
	 * httpPost CHARSET UTF-8
	 * @param url
	 * @param header
	 * @param requestBody
	 * @return
	 */
	public static String httpPost(String url, Map<String, String> header, String requestBody) {
		return HttpBase.httpPost(url, header, requestBody, HttpBase.CHARSET_UTF8);
	}
	
	/**
	 * httpsPost CHARSET UTF-8
	 * @param url
	 * @param header
	 * @param requestBody
	 * @return
	 */
	public static String httpsPost(String url, Map<String, String> header, String requestBody) {
		return HttpBase.httpsPost(url, header, requestBody, HttpBase.CHARSET_UTF8);
	}
	
	
	
	/**
	 * httpsPost CHARSET UTF-8
	 * @param url
	 * @param header
	 * @param params
	 * @return
	 */
	public static String httpsPost(String url, Map<String, String> header, Map<String, String> params) {
		return HttpBase.httpsPost(url, header, params, HttpBase.CHARSET_UTF8);
	}
	
}
