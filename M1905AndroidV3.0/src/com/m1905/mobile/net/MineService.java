package com.m1905.mobile.net;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.m1905.mobile.dao.Feedback;
import com.m1905.mobile.util.LogUtils;
import com.m1905.mobile.util.NetUtils;
import com.m1905.mobile.util.StringUtils;

public class MineService {

	/**
	 * 意见反馈
	 * 
	 * @param context
	 * @param comment
	 * @param email
	 * @return
	 */
	/*public static Feedback submitFeedback(Context context, String comment,
			String email) {
		LogUtils.i(email + "----" + comment);
		Feedback feedback = null;
		if (NetUtils.isConnect(context)) {
			String json = RestClient.newInstance(context).doMethod(true,
					"/Func/retroaction", new BasicNameValuePair("email", email),
					new BasicNameValuePair("comment", comment));
			LogUtils.i(json);
			if (!TextUtils.isEmpty(json)) {
				feedback = new Feedback();
				try {
					JSONObject jsonObject = new JSONObject(json);
					feedback.setMessage(jsonObject.getString("message"));
					String res = jsonObject.getString("res");
					if (TextUtils.isEmpty(res) || res.equals("{}")) {

					} else {
						JSONObject jsonResult = new JSONObject(res);
						feedback.setResult(jsonResult.has("result") ? StringUtils
								.parseInt(jsonResult.getString("result")) : -1);
						feedback.setDid(jsonResult.has("did") ? jsonResult
								.getString("did") : "");
						feedback.setSid(jsonResult.has("sid") ? jsonResult
								.getString("sid") : "");
						feedback.setUid(jsonResult.has("uid") ? jsonResult
								.getString("uid") : "");
					}
					String data = jsonObject.getString("data");
					if (TextUtils.isEmpty(data) || res.equals("{}")) {

					} else {
						JSONObject jsonResult = new JSONObject(data);
						feedback.setStatus((jsonResult.has("status") ? StringUtils
								.parseInt(jsonResult.getString("status"), 0)
								: 0));
						feedback.setSubmitMessage(jsonResult.has("message") ? jsonResult
								.getString("message") : "");
					}
				} catch (Exception e) {
					LogUtils.e("数据返回：" + feedback.getMessage() + " error by: "
							+ e.getMessage());
				}
			}
		}
		return feedback;
	}*/

	/**
	 * 意见反馈
	 * 
	 * @param context
	 * @param comment
	 * @return
	 */
	/*public static Feedback submitFeedback(Context context, String comment) {
		return submitFeedback(context, comment, null);
	}*/
}
