package com.m1905.mobile.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.widget.Toast;
import com.m1905.mobile.common.Constant;
import com.m1905.mobile.dao.PushUser;

public class AppUtils {
	public static void toastShowMsg(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void checkVersion(Context context) {
		String versionInfo = SettingUtils.loadVersionUpdateInfo(context);
		if (!TextUtils.isEmpty(versionInfo)) {
			String[] versionInfos = versionInfo.split(String
					.valueOf(Constant.SPLITER));
			long versionMini = Long.parseLong(versionInfos[0]);
			long lastUpdate = Long.parseLong(versionInfos[1]);
			if (versionMini < Constant.APP_VERSION.getVersionMini()) {
				SDUtils.deleteCacheFile();
			} else {
				try {
					PackageInfo packageInfo = context.getPackageManager()
							.getPackageInfo(context.getPackageName(), 0);
					Constant.APP_VERSION.setVersionMini(versionMini);
					Constant.APP_VERSION.setLastUpdate(lastUpdate);
					Constant.APP_VERSION
							.setVersionCode(packageInfo.versionCode);
					Constant.APP_VERSION
							.setVersionName(packageInfo.versionName);
				} catch (NameNotFoundException e) {
					LogUtils.e(e.getMessage());
				}
			}
		}
		// 保存应用程序更新信息
		SettingUtils.saveVersionUpdateInfo(context);
	}

    /**
     * 获得推送用户信息
     * @param context
     * @return
     */
    public static PushUser getDefaultPushUser(Context context) {
        String pushUserInfo = SettingUtils.loadPushUserInfo(context);
        PushUser pushUser = new PushUser();
        if (!TextUtils.isEmpty(pushUserInfo)) {
            String[] infos = pushUserInfo.split(String
                    .valueOf(Constant.SPLITER));
            pushUser.setAppId(infos[0]);
            pushUser.setChannelId(infos[1]);
            pushUser.setUserId(infos[2]);
        }
        return pushUser;
    }
}
