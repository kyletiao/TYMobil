package com.m1905.mobile.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DES2 {

	private static final String MCRYPT_TRIPLEDES = "DESede";
	private static final String TRANSFORMATION = "DESede/CBC/PKCS5Padding";

	private static String key = "iufles8787rewjk1qkq9dj76";
	private static String iv = "vs0ld7w3";
  
    public static String decrypt(String data) {
		try {
			DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance(MCRYPT_TRIPLEDES);
			SecretKey sec = keyFactory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			IvParameterSpec IvParameters = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, sec, IvParameters);
			return new String(cipher.doFinal(Base64.decode(data)), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String encrypt(String data) {
		try {
			DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			SecretKey sec = keyFactory.generateSecret(spec);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			IvParameterSpec IvParameters = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, sec, IvParameters);
			return Base64.encode(cipher.doFinal(data.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/*
     * 根据字符串生成密钥字节数组 
     * @param keyStr 密钥字符串
     * @return 
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesKey(String keyStr){
    	byte[] key = new byte[24];    //声明一个24位的字节数组，默认里面都是0
        try {
			byte[] temp = keyStr.getBytes("UTF-8");    //将字符串转成字节数组
			
			/*
			 * 执行数组拷贝
			 * System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
			 */
			if(key.length > temp.length){
			    //如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
			    System.arraycopy(temp, 0, key, 0, temp.length);
			}else{
			    //如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
			    System.arraycopy(temp, 0, key, 0, key.length);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return key;
    } 
    
    /*
     * 根据字符串生成密钥字节数组 
     * @param keyStr 密钥字符串
     * @return 
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesIv(String keyStr){
    	byte[] key = new byte[8];    //声明一个24位的字节数组，默认里面都是0
        try {
			byte[] temp = keyStr.getBytes("UTF-8");    //将字符串转成字节数组
			
			/*
			 * 执行数组拷贝
			 * System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
			 */
			if(key.length > temp.length){
			    //如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
			    System.arraycopy(temp, 0, key, 0, temp.length);
			}else{
			    //如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
			    System.arraycopy(temp, 0, key, 0, key.length);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return key;
    } 
}