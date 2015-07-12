package com.m1905.mobile.util;

import com.m1905.mobile.common.AppConfig;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
	/**
	 * 返回当前网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnect(Context context) {
		ConnectivityManager connectManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectManager == null)
			return false;
		NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
		return networkInfo == null || !networkInfo.isAvailable() ? false : true;
	}

	/**
	 * 返回网络类型
	 * 
	 * @param context
	 * @return -1：无网络、0：移动网络、1：WIFI other:未知网络
	 */
	public static int getNetworkType(Context context) {
		ConnectivityManager connectManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectManager == null)
			return AppConfig.NET_TYPE_UNKNOWN;
		NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
		return networkInfo == null || !networkInfo.isAvailable() ? AppConfig.NET_TYPE_UNKNOWN
				: networkInfo.getType();
	}

	/**
	 * 获取当前网络名称；
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetworkTypeName(Context context) {
		String name = AppConfig.NET_TYPE_UNKNOWN_NAME;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			NetworkInfo activeNetInfo = manager.getActiveNetworkInfo();
			if (activeNetInfo != null && activeNetInfo.isAvailable()) {
				name = activeNetInfo.getExtraInfo();
			}
		} catch (Exception e) {
			return name;
		}
		return name;
	}

}
