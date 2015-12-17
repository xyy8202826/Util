package com.xyy.util.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 单项加密
 * @author Administrator
 *
 */
public class EncryptOneWay {
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static final String SHA1= "SHA-1";
	private static final String MD5= "MD5";
	/**
	 * 对字符串进行MD5加密(默认UTF8)
	 * @param text
	 *            明文
	 * @return 密文 小写
	 */
	public static String md5(String text) {
		return encrypt(MD5,text, "UTF-8");
	}
	/**
	 * 对字符串进行MD5加密(默认UTF8)
	 * 
	 * @param text  明文
	 * 
	 * @return 密文 大写
	 */
	public static String md5UpperCase(String text) {
		return md5(text).toUpperCase();
	}
	
	/**
	 * 对字符串进行SHA-1加密(默认UTF8)
	 * @param text
	 *            明文
	 * @return 密文 大写
	 */
	public static String SHA1(String text) {
		return encrypt(SHA1,text, "UTF-8");
	}
	
	/**
	 * 对字符串进行SHA-1加密(默认UTF8)
	 * @param text
	 *            明文
	 * @return 密文 小写
	 */
	public static String SHA1UpperCase(String text) {
		return encrypt(SHA1,text, "UTF-8").toUpperCase();
	}
	
	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param text明文
	 *            
	 * @param charsetname
	 * 			  编码集(为空时，默认UTF-8)
	 * 
	 * @return 密文
	 */
	public static String encrypt(String encryptType,String text, String charsetname) {
		if (charsetname == null || "".equals(charsetname)){
			charsetname = "UTF-8";
		}
		MessageDigest msgDigest = null;
		try {
			msgDigest = MessageDigest.getInstance(encryptType);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					"System doesn't support"+encryptType+" algorithm.");
		}
		try {
			msgDigest.update(text.getBytes(charsetname));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(
					"System doesn't support your  EncodingException.");

		}
		byte[] bytes = msgDigest.digest();
		String md5Str = new String(encodeHex(bytes));
		return md5Str;
	}
	public static char[] encodeHex(byte[] data) {
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}
		return out;
	}
}
