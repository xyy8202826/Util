package com.xyy.util.encrypt;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.base.Strings;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 双向加密
 * @author Administrator
 *
 */
@SuppressWarnings("restriction")
public class EncryptTwoWay {
	
	/** 
     * AES加密为base64 code 
     * @param content 待加密的内容 
     * @param encryptKey 加密密钥 
     * @return 加密后的base 64 code 
     * @throws Exception 
     */  
    public static String aesEncrypt(String content, String encryptKey) { 
    	byte[] bytes=null;
		try {
			bytes = aesEncryptToBytes(content, encryptKey);
		} catch (Exception e) {
			throw new RuntimeException("aesEncrypt Exception");
		}
        return base64Encode(bytes);  
    } 
    
    /** 
     * 将base64 code AES解密 
     * @param encryptStr 待解密的base 64 code 
     * @param decryptKey 解密密钥 
     * @return 解密后的string 
     * @throws Exception 
     */  
    public static String aesDecrypt(String encryptStr, String decryptKey) {
    	if(Strings.isNullOrEmpty(encryptStr)){
    		return null;
    	}else{
    	String decodeStr=null;;
		try {
			decodeStr = aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
		} catch (Exception e) {
			throw new RuntimeException("base64 code AES解密 出错");
		}
    	  return decodeStr;
    	}
    } 
	
	 /** 
     * AES加密 
     * @param content 待加密的内容 
     * @param encryptKey 加密密钥 
     * @return 加密后的byte[] 
     * @throws Exception 
     */  
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );  
        secureRandom.setSeed(encryptKey.getBytes());  
        kgen.init(128, secureRandom);  
        Cipher cipher = Cipher.getInstance("AES");  
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));  
        return cipher.doFinal(content.getBytes("UTF-8"));  
    } 
    
    /** 
     * AES解密 
     * @param encryptBytes 待解密的byte[] 
     * @param decryptKey 解密密钥 
     * @return 解密后的String 
     * @throws Exception 
     */  
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" ); 
        secureRandom.setSeed(decryptKey.getBytes());  
        kgen.init(128, secureRandom);  
        Cipher cipher = Cipher.getInstance("AES");  
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));  
        byte[] decryptBytes = cipher.doFinal(encryptBytes);  
        return new String(decryptBytes,"UTF-8");  
    } 
    
    /** 
     * base 64 encode 
     * @param bytes 待编码的byte[] 
     * @return 编码后的base 64 code 
     */  
	public static String base64Encode(byte[] bytes){  
        return new BASE64Encoder().encode(bytes).replaceAll("\n", "").replaceAll("\r", "").replaceAll("\r\n", "");  
    } 
	 /** 
     * base 64 decode 
     * @param base64Code 待解码的base 64 code 
     * @return 解码后的byte[] 
     * @throws Exception 
     */  
	public static byte[] base64Decode(String base64Code) throws Exception{  
        return Strings.isNullOrEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);  
    } 

}
