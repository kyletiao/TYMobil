package com.m1905.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.common.Constant;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.dao.User;

public class SettingUtils {

	/**
	 * 设置程序配置信息
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	private static void setAppInfo(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(
				AppConfig.M1905_APP_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 设置程序配置信息
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	private static void setAppInfo(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(
				AppConfig.M1905_APP_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 设置程序配置信息
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	private static void setAppInfo(Context context, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(
				AppConfig.M1905_APP_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 设置程序配置信息
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	private static void setAppInfo(Context context, String key, float value) {
		SharedPreferences sp = context.getSharedPreferences(
				AppConfig.M1905_APP_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	/**
	 * 设置程序配置信息
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	private static void setAppInfo(Context context, String key, long value) {
		SharedPreferences sp = context.getSharedPreferences(
				AppConfig.M1905_APP_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * 是否开启推送
	 * 
	 * @return
	 */
	public static boolean isPush(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				AppConfig.M1905_APP_INFO, Context.MODE_PRIVATE);
		boolean isPush = sp.getBoolean(AppConfig.M1905_PUSH_KEY,
				AppConfig.DEFAULT_IS_PUSH);
		LogUtils.i("推送状态：" + isPush);
		return isPush;
	}

	/**
	 * 保存推送设置
	 * 
	 * @param isPush
	 */
	public static void savePush(Context context, boolean isPush) {
		setAppInfo(context, AppConfig.M1905_PUSH_KEY, isPush);
		LogUtils.i("保存推送状态：" + isPush);
	}

	/**
	 * 是否允许在数据流量情况下播放和下载视频(2G/3G网络)
	 * 
	 * @return
	 */
	public static boolean isPlayOrDownloadByMoblie(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				AppConfig.M1905_APP_INFO, Context.MODE_PRIVATE);
		boolean isMobile = sp.getBoolean(AppConfig.M1905_MOBLIE_KEY,
				AppConfig.DEFAULT_IS_MOBLIE);
		LogUtils.i("是否支持2G/3G网络：" + isMobile);
		return isMobile;
	}

	/**
	 * 保存是否允许在数据流量情况下播放和下载视频(2G/3G网络)
	 * 
	 * @param isMoblie
	 */
	public static void savePlayOrDownloadByMoblie(Context context,
			boolean isMoblie) {
		setAppInfo(context, AppConfig.M1905_MOBLIE_KEY, isMoblie);
		LogUtils.i("保存是否支持2G/3G网络：" + isMoblie);
	}

	/**
	 * 加载使用视频清晰度(下载和观看时的选择)
	 * 
	 * @return 0：高清、1：标清
	 */
	public static int loadUseFilmResolution(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				AppConfig.M1905_APP_INFO, Context.MODE_PRIVATE);
		int videoType = sp.getInt(AppConfig.M1905_VIDEO_TYPE_KEY,
				AppConfig.DEFAULT_VIDEO_TYPE);
		LogUtils.i("视频清晰度：" + videoType);
		return videoType;
	}

	/**
	 * 保存使用视频清晰度(下载和观看时的选择)
	 * 
	 * @param videoType
	 *            0：高清、1：标清
	 */
	public static void saveUseFilmResolution(Context context, int videoType) {
		setAppInfo(context, AppConfig.M1905_VIDEO_TYPE_KEY, videoType);
		LogUtils.i("保存视频清晰度：" + videoType);
	}

	/**
	 * 清除缓存文件， 对于缓存较大，最好在单独线程中完成处理
	 */
	public static void clearCache() {
		SDUtils.deleteCacheFile();
	}

	/**
	 * 获得缓存大小
	 * 
	 * @return 带单位KB、M、G、Byte
	 */
	public static String getCacheSize() {
		return StringUtils.conversionBytesUnit(SDUtils.getCacheSize());
	}

	/**
	 * 加载设备信息字符串
	 * 
	 * @param context
	 * @return
	 */
	public static String loadDeviceInfo(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				AppConfig.M1905_APP_INFO, Context.MODE_PRIVATE);
		String deviceInfo = sp.getString(AppConfig.M1905_DEVICE_INFO_KEY, "");
		LogUtils.i("设备信息：" + deviceInfo);
		return deviceInfo;
	}

	/**
	 * 保存设备信息
	 * 
	 * @param context
	 * @param deviceInfo
	 */
	public static void saveDeviceInfo(Context context, String deviceInfo) {
		setAppInfo(context, AppConfig.M1905_DEVICE_INFO_KEY, deviceInfo);
		LogUtils.i("保存设备信息：" + deviceInfo);
	}

	/**
	 * 保存用户信息
	 * 
	 * @param context
	 * @param user
	 */
	public static void saveUserInfo(Context context, User user) {
		setAppInfo(context, AppConfig.M1905_USER_NAME_KEY, user.getUserName());
		setAppInfo(context, AppConfig.M1905_USER_PASS_KEY, user.getUserPass());
		LogUtils.i("记住用户信息：" + user.toString());
	}

	/**
	 * 加载用户基本信息
	 * 
	 * @param context
	 * @return
	 */
	public static User loadUserInfo(Context context) {
		User user = new User("", "");
		SharedPreferences sp = context.getSharedPreferences(
				AppConfig.M1905_APP_INFO, Context.MODE_PRIVATE);
		String userName = sp.getString(AppConfig.M1905_USER_NAME_KEY, "");
		String userPass = sp.getString(AppConfig.M1905_USER_PASS_KEY, "");
		user.setUserName(userName);
		user.setUserPass(userPass);
		LogUtils.i("加载用户信息：" + user.toString());
		return user;
	}

	/**
	 * 加载版本最后一次更新信息
	 * 
	 * @param context
	 * @return versionMini+Constant.SPLITER+lastUpdate
	 */
	public static String loadVersionUpdateInfo(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				AppConfig.M1905_APP_INFO, Context.MODE_PRIVATE);
		String updateInfo = sp.getString(AppConfig.M1905_VERSION_UPDATE_KEY,
				AppConfig.DEFAULT_VERSION_UPDATE_VALUE);
		LogUtils.i("加载应用程序更新信息：" + updateInfo);
		return updateInfo;
	}

	/**
	 * 保存应用程序更新信息
	 * 
	 * @param context
	 */
	public static void saveVersionUpdateInfo(Context context) {
		String updateInfo = String.valueOf(Constant.APP_VERSION
				.getVersionMini())
				+ Constant.SPLITER
				+ String.valueOf(Constant.APP_VERSION.getLastUpdate());
		setAppInfo(context, AppConfig.M1905_VERSION_UPDATE_KEY, updateInfo);
		LogUtils.i("保存应用程序更新信息：" + updateInfo);
	}

	/**
	 * 加载刷新时间
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String loadRefreshTime(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(
				AppConfig.M1905_APP_INFO, Context.MODE_PRIVATE);
		String value = sp.getString(key, "暂未更新");
		LogUtils.i("加载最后一次刷新时间：" + key + ":" + value);
		return value;
	}

	/**
	 * 更新刷新时间
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveRefreshTime(Context context, String key, String value) {
		setAppInfo(context, key, value);
		LogUtils.i("保存最后一次刷新时间：" + key + ":" + value);
	}

    /**
     * 加载PushInfo
     *
     * @param context
     * @return appId+Constant.SPLITER+channelId+Constant.SPLITER+userId
     */
    public static String loadPushUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                AppConfig.M1905_APP_INFO, Context.MODE_PRIVATE);
        String pushUserInfo = sp.getString(AppConfig.M1905_APP_PUSH_KEY, "");
        LogUtils.i("加载PushInfo：" + pushUserInfo);
        return pushUserInfo;
    }

    /**
     * 保存PushInfo
     *
     * @param context
     */
    public static void savePushUserInfo(Context context) {
        String pushUserInfo = Identify.getPushUser(context).getAppId()
                + Constant.SPLITER
                + Identify.getPushUser(context).getChannelId()
                + Constant.SPLITER + Identify.getPushUser(context).getUserId();
        setAppInfo(context, AppConfig.M1905_APP_PUSH_KEY, pushUserInfo);
        LogUtils.i("保存PushInfo：" + pushUserInfo);
    }
}
