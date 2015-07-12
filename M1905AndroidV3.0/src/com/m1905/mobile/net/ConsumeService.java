package com.m1905.mobile.net;

import android.content.Context;
import android.text.TextUtils;
import com.m1905.mobile.dao.Consume;
import com.m1905.mobile.dao.EConsume;
import com.m1905.mobile.util.DES2;
import com.m1905.mobile.util.LogUtils;
import com.m1905.mobile.util.NetUtils;
import com.m1905.mobile.util.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Eric on 14-2-8.
 */
public class ConsumeService {

    /*public static EConsume jsonToEConsume(Context context, String url,
                                          String token) {
        EConsume eConsume = new EConsume();
        boolean b = NetUtils.isConnect(context);
        LogUtils.e(b + "--------------------------->");
        if (NetUtils.isConnect(context)) {
            String request = DES2.encrypt(StringUtils
                    .basicValue2String(new BasicNameValuePair("token", token)));
            String jsonnew = RestClient.newInstance(context).doMethod(false, url,
                    new BasicNameValuePair("request", URLEncoder.encode(request)));
            String json = DES2.decrypt(jsonnew);
            LogUtils.i(json);
            try {
                JSONObject jsonObject = new JSONObject(json);
                eConsume.setMessage(jsonObject.getString("message"));
                String result = jsonObject.getString("res");
                if (result == null || result.equals("{}")) {
                } else {
                    JSONObject jsonResult = new JSONObject(result);
                    eConsume.setResult(jsonResult.has("result") ? StringUtils
                            .parseInt(jsonResult.getString("result")) : -1);
                    eConsume.setDid(jsonResult.has("did") ? jsonResult
                            .getString("did") : "");
                    eConsume.setSid(jsonResult.has("sid") ? jsonResult
                            .getString("sid") : "");
                    eConsume.setUid(jsonResult.has("uid") ? jsonResult
                            .getString("uid") : "");
                }
                String data = jsonObject.getString("data");
                if (TextUtils.isEmpty(data) || data.equals("[]")) {
                } else {
                    eConsume.setConsumes(parseJsonConsumeList(data));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return eConsume;
    }*/

    public static ArrayList<Consume> parseJsonConsumeList(String json) {
        ArrayList<Consume> consumes = new ArrayList<Consume>();
        try {
            JSONObject jsonobject = new JSONObject(json);
            Iterator iterator = jsonobject.keys();
            while (iterator.hasNext()) {
                Object s = iterator.next();
                if (s.toString().equalsIgnoreCase("pi")
                        || s.toString().equalsIgnoreCase("ps")
                        || s.toString().equalsIgnoreCase("count")) {
                    continue;
                }
                JSONObject consumeObject = jsonobject.getJSONObject(s
                        .toString());
                Consume consume = new Consume();
                consume.setId(consumeObject.has("id") ? StringUtils.parseInt(
                        consumeObject.getString("id"), -1) : -1);
                consume.setNumber(consumeObject.has("number") ? StringUtils
                        .parseFloat(consumeObject.getString("number"), 0.0f)
                        : 0.0f);
                consume.setUsername(consumeObject.has("username") ? consumeObject
                        .getString("username") : "");
                consume.setTime(consumeObject.has("time") ? consumeObject
                        .getString("time") : "");
                consume.setNote(consumeObject.has("note") ? consumeObject
                        .getString("note") : "");
                consumes.add(consume);
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
        return consumes;
    }
}
