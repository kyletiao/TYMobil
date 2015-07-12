package com.m1905.mobile.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/**
 * 
 * 文件功能简述： 描述一个URL或图片地址是否有效
 * 
 * @author leepan
 * @version
 * @copyright
 */
@SuppressWarnings("unused")
public class URLAvailability {
	private static URL urlStr;
	private static HttpURLConnection connection;
	private static int state = -1;

	/**
	 * 功能描述 : 检测当前URL是否可连接或是否有效, 最多连接网络 5 次, 如果 5 次都不成功说明该地址不存在或视为无效地址.
	 * 
	 * @param url
	 *            指定URL网络地址
	 * 
	 * @return String
	 */
	public synchronized static boolean isConnect(String url) {
		int counts = 0;
		if (url == null || url.length() <= 0) {
			return false;
		}
		while (counts < 5) {
			try {
				urlStr = new URL(url);
				connection = (HttpURLConnection) urlStr.openConnection();
				state = connection.getResponseCode();
				if (state == 200) {
					return true;
				}
				break;
			} catch (Exception ex) {
				counts++;
				continue;
			}
		}
		return false;
	}
}