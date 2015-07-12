package com.m1905.mobile.dao;

import android.content.Context;

import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.common.Device;
import com.m1905.mobile.util.AppUtils;
import com.m1905.mobile.util.EncryptUtils;

public class Identify {
	public static User currentUser;// 用户实体类
	public static Device device;// 设备信息实体类
	public static String sid;// 会话ID

    private static PushUser pushUser;// 推送信息

    public static PushUser getPushUser(Context context) {
        return pushUser == null ? AppUtils.getDefaultPushUser(context)
                : pushUser;
    }

    public static void setPushUser(PushUser pushUser) {
        Identify.pushUser = pushUser;
    }

	public static String getUid() {
		if (currentUser != null) {
			return String.valueOf(currentUser.getUserCode());
		} else {
			return "";
		}
	}

	public static String getKey() {
		return device == null ? "" : EncryptUtils.MD5(device.getDid()
				+ AppConfig.M1905_APP_KEY);
	}

	/**
	 * 是否是会员
	 * 
	 * @return
	 */
	public static boolean isM1905VIP() {
		return currentUser != null && currentUser.isM1905VIP();
	}
}
