package com.m1905.mobile.baidupush;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.baidu.android.pushservice.PushConstants;
import com.m1905.mobile.activity.M1905VideoPlayerActivity;
import com.m1905.mobile.activity.StartAct;
import com.m1905.mobile.activity.WebAct;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.dao.PushUser;
import com.m1905.mobile.util.AppUtils;
import com.m1905.mobile.util.LogUtils;
import com.m1905.mobile.util.SettingUtils;
import com.m1905.mobile.util.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Eric on 14-2-19.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {

    /**
     * TAG to Log
     */
    public static final String TAG = MyPushMessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, ">>> Receive intent: \r\n" + intent);
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            //获取消息内容
            String message = intent.getExtras().getString(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            //消息的用户自定义内容读取方式
            Log.i(TAG, "onMessage: " + message);
            //自定义内容的 json 串
            Log.d(TAG, "EXTRA_EXTRA = " + intent.getStringExtra(PushConstants.EXTRA_EXTRA));
        } else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
            //处理绑定等方法的返回数据
            //PushManager.startWork()的返回值通过 PushConstants.METHOD_BIND 得到
            //获取方法
            final String method = intent.getStringExtra(PushConstants.EXTRA_METHOD);
            //方法返回错误码。若绑定返回错误（非 0），则应用将不能正常接收消息。
            //绑定失败的原因有多种，如网络原因，或 access token 过期。
            //请不要在出错时进行简单的 startWork 调用，这有可能导致死循环。
            //可以通过限制重试次数，或者在其他时机重新调用来解决。
            int errorCode = intent
                    .getIntExtra(PushConstants.EXTRA_ERROR_CODE,
                            PushConstants.ERROR_SUCCESS);
            String content = "";
            if (intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT) != null) {
                //返回内容
                content = new String(intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
            }
            //用户在此自定义处理消息,以下代码为 demo 界面展示用
            Log.d(TAG, "onMessage: method : " + method);
            Log.d(TAG, "onMessage: result : " + errorCode);
            Log.d(TAG, "onMessage: content : " + content);

            if (TextUtils.isEmpty(content)) {
                Identify.setPushUser(AppUtils.getDefaultPushUser(context));
            } else {
                try {
                    JSONObject data = new JSONObject(content);
                    String pushInfo = data.has("response_params") ? data
                            .getString("response_params") : "";
                    if (TextUtils.isEmpty(pushInfo)) {
                        Identify.setPushUser(AppUtils
                                .getDefaultPushUser(context));
                    } else {
                        PushUser pushUser = new PushUser();
                        JSONObject jsonData = new JSONObject(pushInfo);
                        pushUser.setAppId(jsonData.has("appid") ? jsonData
                                .getString("appid") : "");
                        pushUser.setChannelId(jsonData.has("channel_id") ? jsonData
                                .getString("channel_id") : "");
                        pushUser.setUserId(jsonData.has("user_id") ? jsonData
                                .getString("user_id") : "");
                        Identify.setPushUser(pushUser);
                    }
                } catch (Exception e) {
                    LogUtils.e(e.getMessage());
                    Identify.setPushUser(AppUtils.getDefaultPushUser(context));
                }

            }
            SettingUtils.savePushUserInfo(context);

        } else if (intent.getAction().equals(PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
            //可选。通知用户点击事件处理
            Log.d(TAG, "intent=" + intent.toUri(0));
            //自定义内容的 json 串
            String jsonStr = intent.getStringExtra(PushConstants.EXTRA_EXTRA);
            Log.d(TAG, "EXTRA_EXTRA = " + jsonStr);

            Intent intentPush = new Intent();
            if (!TextUtils.isEmpty(jsonStr)) {
                try {
                    JSONObject jsonData = new JSONObject(jsonStr);
                    LogUtils.i(jsonStr);
                    int cid = jsonData.has("i.cid") ? StringUtils.parseInt(jsonData
                            .getString("i.cid")) : 0;

                    int newsId = jsonData.has("newsId") ? StringUtils
                            .parseInt(jsonData.getString("newsId"), 8) : 8;
                    String wapUrl = jsonData.has("l.wapurl") ? jsonData.getString("l.wapurl") : "";

                    switch (cid){
                        case 1: // 推送资讯
                            intentPush.setClass(context, WebAct.class);
                            intentPush.putExtra("address", wapUrl);
                            break;

                        case 2: // 全片
                            intentPush.setClass(context, M1905VideoPlayerActivity.class);

                            break;

                        case 7: // 短视频

                            break;
                    }
                    intentPush.putExtra("fromNotify",
                            jsonData.getBoolean("fromNotify"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (!isRunning(context)) {
                    intentPush.setClass(context, StartAct.class);
                    intentPush.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentPush);
                } else {
                    LogUtils.d("已启动");
                }
            }
        }
    }

    // 判断是否在运行中
    private boolean isRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        String MY_PKG_NAME = context.getPackageName();
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }
}
