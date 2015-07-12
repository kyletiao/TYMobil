package com.m1905.mobile.util;

import java.security.MessageDigest;

import android.text.TextUtils;

public class EncryptUtils {

	/**
	 * 
	 * @param val
	 *            原有字符串
	 * @return MD5后的字符串
	 */
	public final static String MD5(String val) {
		if (TextUtils.isEmpty(val))
			return val;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = val.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * @param val
	 * @param secret
	 * @return
	 */
	public final static String encryptOrUncrypt(String val) {
		return new String(encryptOrUncrypt(val.getBytes(), 1905));
	}

	public static byte[] encryptOrUncrypt(byte[] bt, char secret) {
		return encryptOrUncrypt(bt, (int) secret);
	}

	public static byte[] encryptOrUncrypt(byte[] bt, int secret) {
		for (int i = 0; i < bt.length; i++) {
			bt[i] = (byte) (bt[i] ^ secret); // 通过异或运算进行加密
		}
		return bt;
	}

}
