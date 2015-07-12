package com.m1905.mobile.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.common.Constant;
import com.m1905.mobile.common.Device;

/**
 * 设备工具类
 * 
 * @author forcetech
 * 
 */
public class DeviceUtils {
	/**
	 * 获得设备系统
	 * 
	 * @return
	 */
	public static String getDeviceOs() {
		return "android/" + android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获得设备型号
	 * 
	 * @return
	 */
	public static String getDeviceModel() {
		return android.os.Build.MANUFACTURER + "/" + android.os.Build.MODEL;
	}

	/**
	 * 获得设备系统语言
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceLanguage(Context context) {
		return context.getResources().getConfiguration().locale
				.getDisplayName()
				+ "/"
				+ context.getResources().getConfiguration().locale
						.getLanguage();
	}

	/**
	 * 获得设备id
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		String did = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		return TextUtils.isEmpty(did) ? AppConfig.DEFAULT_DID : did;
	}

	/**
	 * 获得设备网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceNetworkType(Context context) {
		return NetUtils.getNetworkTypeName(context);
	}

	/**
	 * 获得设备信息
	 * 
	 * @param context
	 * @return
	 */
	public static Device getDevice(Context context) {
		return loadDeviceInfo(context);
	}

	/**
	 * 加载设备信息
	 * 
	 * @param context
	 * @return
	 */
	private static Device loadDeviceInfo(Context context) {
		String deviceInfo = SettingUtils.loadDeviceInfo(context);
		Device device = null;
		if (TextUtils.isEmpty(deviceInfo)) {
			device = new Device(getDeviceModel(), getDeviceOs(),
					getDeviceLanguage(context), getDeviceNetworkType(context),
					getDeviceId(context));
		} else {
			String[] infos = deviceInfo.split(String.valueOf(Constant.SPLITER));
			device = new Device(infos[0], infos[1], infos[2],
					getDeviceNetworkType(context), infos[4]);
		}
		// 保存设备信息
		saveDeviceInfo(context, device);
		return device;
	}

	/**
	 * 保存设备信息
	 * 
	 * @param context
	 * @param device
	 */
	private static void saveDeviceInfo(Context context, Device device) {
		StringBuffer deviceInfo = new StringBuffer();
		deviceInfo.append(device.getModel()).append(Constant.SPLITER)
				.append(device.getOs()).append(Constant.SPLITER)
				.append(device.getLanguage()).append(Constant.SPLITER)
				.append(device.getNetworkTypeName()).append(Constant.SPLITER)
				.append(device.getDid());
		SettingUtils.saveDeviceInfo(context, deviceInfo.toString());
	}

	/**
	 * 获得屏幕设备信息
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics;
	}
}
