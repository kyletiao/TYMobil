package com.m1905.mobile.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;

public class Md5 {
	public static void Md5Test() {
		List paras = new ArrayList<NameValuePair>();
		// paras.add(new BasicNameValuePair(Keys., value))
	}

	/*private String getURLParas(List<NameValuePair> paras, String appSecret) {
		String timeStamp = getDate();
		// list 的key 按照英文字母顺序排序
		Collections.sort(paras, new Comparator<NameValuePair>() {
			@Override
			public int compare(NameValuePair lhs, NameValuePair rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}
		});
		// 拼接请求校验串。格式：list（key=value&....） + & + 时间戳 +&+ 密钥
		StringBuffer sb = new StringBuffer();
		// encodeUrl 方法见下
		sb.append(encodeUrl(paras)).append("&");
		sb.append(Keys.KEY_TIME).append("=").append(timeStamp).append("&");
		sb.append(appSecret);
		// MD5 加密校验串
		String md5Sign = EncoderByMd5(sb.toString());
		paras.add(new BasicNameValuePair(Keys.KEY_TIME, timeStamp));
		paras.add(new BasicNameValuePair(Keys.KEY_SIGN, md5Sign));
		return encodeUrl(paras);
	}

	public static String encodeUrl(List<NameValuePair> list) {
		if (list == null || 0 == list.size()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (int loc = 0, lenght = list.size(); loc < lenght; loc++) {
			if (first)
				first = false;
			else
				sb.append("&");
			// 判断值是否为空
			if (TextUtils.isEmpty(list.get(loc).getValue())
					|| TextUtils.isEmpty(list.get(loc).getValue().trim())) {
				sb.append(list.get(loc).getName() + "=null");
			} else {
				sb.append(list.get(loc).getName() + "="
						+ list.get(loc).getValue().trim());
			}
		}
		return sb.toString();
	}

	public static String EncoderByMd5(String str) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = str.getBytes();
			// 获得MD5 摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str_char[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str_char[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str_char[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str_char);
		} catch (Exception e) {
			return null;
		}
	}*/
}
