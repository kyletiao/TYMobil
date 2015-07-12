package com.m1905.mobile.common;

import java.io.File;

/**
 * 程序的一些配置信息
 * 
 * @author forcetech
 * 
 */
public class AppConfig {

	/**
	 * APP INFO
	 */
	public static final String M1905_APP_INFO = "xxxxx";
	public static final String M1905_APP_KEY = "xxxxx";

	/**
	 * APP VERSION UPDATE INFO
	 */
	public static final String M1905_VERSION_UPDATE_KEY = "M1905_VERSION_UPDATE_KEY";
	public static final String DEFAULT_VERSION_UPDATE_VALUE = String
			.valueOf(Constant.APP_VERSION.getVersionMini())
			+ Constant.SPLITER
			+ String.valueOf(Constant.APP_VERSION.getLastUpdate());
	/**
	 * APP USER
	 */
	public static final String M1905_USER_NAME_KEY = "M1905_USER_NAME_KEY";
	public static final String M1905_USER_PASS_KEY = "M1905_USER_PASS_KEY";
	/*
	 * 设备信息
	 */
	/**
	 * 设备id
	 */
	public final static String DEFAULT_DID = "000000000000000";
	public final static int NET_TYPE_UNKNOWN = -1;
	public final static String NET_TYPE_UNKNOWN_NAME = "无网络";
	public final static String M1905_DEVICE_INFO_KEY = "M1905_DEVICE_INFO_KEY";
	/**
	 * 当SDCard 只剩下50M时，不可用
	 */
	public final static long SDCARD_BLOCK_SIZE = 50 * 1024 * 1024;
	/**
	 * 缓存等主目录格式/sdcard/m1905xxx/ 其中xxx表示版本ID，目的是不同的版本不会相互干扰
	 */
	public final static String M1905_HOME_PATH = File.separator + "sdcard"
			+ File.separator + "m1905100" + File.separator;
	// 缓存
	public final static String M1905_CACHE_PATH = AppConfig.M1905_HOME_PATH
			+ "cache" + File.separator;
	// 视频
	public final static String M1905_MOVIE_PATH = AppConfig.M1905_HOME_PATH
			+ "movie" + File.separator;
	// 下载文件
	public final static String M1905_DOWNLOAD_PATH = AppConfig.M1905_HOME_PATH
			+ "download" + File.separator;
	/**
	 * CACHE过期时间 24小时=86400000毫秒
	 */
	public static final long CACHE_EXPIRED_MILLISECONDS = 60 * 1000;// 24 * 60 *
																	// 60 *
																	// 1000;

	public final static String CONF_COOKIE = "cookie";

	/*
	 * 推送设置
	 */
	public static final boolean DEFAULT_IS_PUSH = true;
	public static final String M1905_PUSH_KEY = "M1905_PUSH_KEY";
    public static final String M1905_APP_PUSH_KEY = "M1905_APP_PUSH_KEY";

	/*
	 * 网络设置
	 */
	public static final boolean DEFAULT_IS_MOBLIE = false;
	public static final String M1905_MOBLIE_KEY = "M1905_MOBLIE_KEY";

	/*
	 * 视频设置
	 */
	public static final int DEFAULT_VIDEO_TYPE = 0;
	public static final String M1905_VIDEO_TYPE_KEY = "M1905_VIDEO_TYPE_KEY";

	/**
	 * 请求时间设置
	 */
	public static final int CONNECT_TIMEOUT = 0 * 1000;
	public static final int SO_TIMEOUT = 10 * 1000;

	/**
	 * 页容量
	 */
	public static final int PAGE_SIZE = 18;
}
