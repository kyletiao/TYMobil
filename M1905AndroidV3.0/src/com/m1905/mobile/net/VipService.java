package com.m1905.mobile.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.m1905.mobile.dao.EVipProduct;
import com.m1905.mobile.dao.Register;
import com.m1905.mobile.dao.User;
import com.m1905.mobile.dao.VIPInfo;
import com.m1905.mobile.dao.VipPayment;
import com.m1905.mobile.dao.VipProduct;
import com.m1905.mobile.util.DES2;
import com.m1905.mobile.util.LogUtils;
import com.m1905.mobile.util.NetUtils;
import com.m1905.mobile.util.StringUtils;

public class VipService {

	/**
	 * 
	 * @param context
	 * @param url
	 * @return
	 */
	/*public static User jsonToThirdUser(Context context, String url) {
		User user = null;
		boolean b = NetUtils.isConnect(context);
		LogUtils.e(b + "--------------------------->");
		if (NetUtils.isConnect(context)) {
			String jsonNew = RestClient.newInstance(context)
					.doMethod(true, url);
			String json = DES2.decrypt(jsonNew);
			LogUtils.i(json);
			if (!TextUtils.isEmpty(json)) {
				try {
					user = new User();
					JSONObject jsonObject = new JSONObject(json);
					user.setMessage(jsonObject.getString("message"));
					String result = jsonObject.getString("res");
					if (result == null || result.equals("{}")) {
					} else {
						JSONObject jsonResult = new JSONObject(result);
						user.setResult(jsonResult.has("result") ? StringUtils
								.parseInt(jsonResult.getString("result")) : -1);
						user.setDid(jsonResult.has("did") ? jsonResult
								.getString("did") : "");
						user.setSid(jsonResult.has("sid") ? jsonResult
								.getString("sid") : "");
						user.setUid(jsonResult.has("uid") ? jsonResult
								.getString("uid") : "");
					}
					String data = jsonObject.getString("data");
					if (data == null || data.equals("{}")) {
					} else {
						JSONObject contentObject = new JSONObject(data);
						user.setUserCode(jsonObject.has("usercode") ? StringUtils
								.parseLong(jsonObject.getString("usercode"), -1)
								: -1);
						user.setUserName(contentObject.has("username") ? contentObject
								.getString("username") : "");
						user.setUserSite(contentObject.has("site") ? contentObject
								.getString("site") : "");
						user.setUserEmail(contentObject.has("email") ? contentObject
								.getString("email") : "");
						user.setUserToken(contentObject.has("token") ? contentObject
								.getString("token") : "");
						user.setUserSp(contentObject.has("sp") ? contentObject
								.getString("sp") : "");
						VIPInfo vipInfo = new VIPInfo(
								(contentObject.has("vip_start_time") ? StringUtils.parseLong(
										contentObject
												.getString("vip_start_time"),
										-1)
										: -1),
								(contentObject.has("vip_end_time") ? StringUtils
										.parseLong(contentObject
												.getString("vip_end_time"), -1)
										: -1),
								contentObject.has("m1905_vip") ? StringUtils
										.parseInt(contentObject
												.getString("m1905_vip"), -1)
										: -1);
						user.setVipInfo(vipInfo);

					}

				} catch (Exception e) {
					if (user == null) {
						return null;
					}
				}
			} else {
				if (user == null)
					user = new User();
			}
		} else {
			if (user == null)
				user = new User();
		}

		return user;
	}*/

	/*public static User jsonToUser(Context context, String url, String email,
			String password) {
		User user = null;
		boolean b = NetUtils.isConnect(context);
		LogUtils.e(b + "--------------------------->");
		if (NetUtils.isConnect(context)) {
			String jsonnew = RestClient.newInstance(context).doMethod(true,
					url, new BasicNameValuePair("username", email),
					new BasicNameValuePair("password", password));
			String json = DES2.decrypt(jsonnew);
			if (!TextUtils.isEmpty(json)) {
				try {
					user = new User();
					JSONObject jsonObject = new JSONObject(json);
					user.setMessage(jsonObject.getString("message"));
					String result = jsonObject.getString("res");
					if (result == null || result.equals("{}")) {
					} else {
						JSONObject jsonResult = new JSONObject(result);
						user.setResult(jsonResult.has("did") ? StringUtils
								.parseInt(jsonResult.getString("result")) : -1);
						user.setDid(jsonResult.has("did") ? jsonResult
								.getString("did") : "");
						user.setSid(jsonResult.has("sid") ? jsonResult
								.getString("sid") : "");
						user.setUid(jsonResult.has("uid") ? jsonResult
								.getString("uid") : "");
					}
					String data = jsonObject.getString("data");
					if (data == null || data.equals("{}")) {
					} else {
						JSONObject contentObject = new JSONObject(data);
						user.setUserCode(jsonObject.has("usercode") ? StringUtils
								.parseLong(jsonObject.getString("usercode"), -1)
								: -1);
						user.setUserName(contentObject.has("username") ? contentObject
								.getString("username") : "");
						user.setUserSite(contentObject.has("site") ? contentObject
								.getString("site") : "");
						user.setUserEmail(contentObject.has("email") ? contentObject
								.getString("email") : "");
						user.setUserToken(contentObject.has("token") ? contentObject
								.getString("token") : "");
						user.setUserSp(contentObject.has("sp") ? contentObject
								.getString("sp") : "");
						VIPInfo vipInfo = new VIPInfo(
								(contentObject.has("vip_start_time") ? StringUtils.parseLong(
										contentObject
												.getString("vip_start_time"),
										-1)
										: -1),
								(contentObject.has("vip_end_time") ? StringUtils
										.parseLong(contentObject
												.getString("vip_end_time"), -1)
										: -1),
								contentObject.has("m1905_vip") ? StringUtils
										.parseInt(contentObject
												.getString("m1905_vip"), -1)
										: -1);
						user.setVipInfo(vipInfo);
					}
				} catch (Exception e) {
					if (user == null) {
						return null;
					}
				}
			} else {
				if (user == null)
					user = new User();
			}
		} else {
			if (user == null)
				user = new User();
		}

		return user;
	}*/

	/*public static Register jsonToRegister(Context context, String url,
			String email, String password) {
		Register register = null;
		boolean b = NetUtils.isConnect(context);
		LogUtils.e(b + "--------------------------->");
		if (NetUtils.isConnect(context)) {
			String jsonnew = RestClient.newInstance(context).doMethod(true,
					url, new BasicNameValuePair("email", email),
					new BasicNameValuePair("password", password));
			String json = DES2.decrypt(jsonnew);
			if (!TextUtils.isEmpty(json)) {
				try {
					register = new Register();
					JSONObject jsonObject = new JSONObject(json);
					register.setMessage(jsonObject.getString("message"));
					String result = jsonObject.getString("res");
					if (result == null || result.equals("{}")) {
					} else {
						JSONObject jsonResult = new JSONObject(result);
						register.setResult(jsonResult.has("result") ? StringUtils
								.parseInt(jsonResult.getString("result")) : -1);
						register.setDid(jsonResult.has("did") ? jsonResult
								.getString("did") : "");
						register.setSid(jsonResult.has("sid") ? jsonResult
								.getString("sid") : "");
						register.setUid(jsonResult.has("uid") ? jsonResult
								.getString("uid") : "");
					}
					String data = jsonObject.getString("data");
					if (data == null || data.equals("{}")) {
					} else {
						JSONObject contentObject = new JSONObject(data);
						register.setUserName(contentObject.has("username") ? contentObject
								.getString("username") : "");
						register.setUserEmail(contentObject.has("email") ? contentObject
								.getString("email") : "");
						register.setUserSite(contentObject.has("site") ? contentObject
								.getString("site") : "");
						register.setToken(contentObject.has("token") ? contentObject
								.getString("token") : "");
					}
				} catch (Exception e) {
					if (register == null) {
						return null;
					}
				}
			} else {
				if (register == null)
					register = new Register();
			}
		} else {
			if (register == null)
				register = new Register();
		}

		return register;
	}*/

	/*public static EVipProduct getVipMonthProduct(Context context) {
		EVipProduct eVipProduct = null;
		String url = "/Pay/productList";
		if (NetUtils.isConnect(context)) {
			String data = RestClient.newInstance(context).doMethod(false, url);
			if (!TextUtils.isEmpty(data)) {
				LogUtils.i(data);
				try {
					eVipProduct = new EVipProduct();
					JSONObject jsonObject = new JSONObject(data);
					eVipProduct.setMessage(jsonObject.optString("message", ""));
					String result = jsonObject.getString("res");
					if (TextUtils.isEmpty(result) || result.equals("{}")) {
					} else {
						JSONObject jsonResult = new JSONObject(result);
						eVipProduct
								.setResult(jsonResult.has("did") ? StringUtils
										.parseInt(
												jsonResult.getString("result"),
												-1) : -1);
						eVipProduct.setDid(jsonResult.has("did") ? jsonResult
								.getString("did") : "");
						eVipProduct.setSid(jsonResult.has("sid") ? jsonResult
								.getString("sid") : "");
						eVipProduct.setUid(jsonResult.has("uid") ? jsonResult
								.getString("uid") : "");
					}
					String lstData = jsonObject.getString("data");
					if (TextUtils.isEmpty(lstData) || lstData.equals("{}")) {
						LogUtils.i("暂无相关信息");
					} else {
						eVipProduct
								.setLstVipProduct(parseJsonVipProduct(lstData));
					}
				} catch (Exception e) {
					LogUtils.e(e.getMessage());
				}
			} else {
				LogUtils.e("网络请求错误");
			}
		} else {
			LogUtils.e("网络错误");
		}
		return eVipProduct;
	}*/

	private static List<VipProduct> parseJsonVipProduct(String json) {
		List<VipProduct> lstVipProduct = new ArrayList<VipProduct>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			LogUtils.i(json);
			for (int index = 0; index < jsonArray.length(); index++) {
				JSONObject jsonObject = jsonArray.getJSONObject(index);
				VipProduct vipProduct = new VipProduct();
				vipProduct.setProCode(jsonObject.optString("product_code", ""));
				vipProduct.setTitle(jsonObject.optString("title", ""));
				vipProduct.setOprice(jsonObject.has("oprice") ? StringUtils
						.parseFloat(jsonObject.getString("oprice"), 0.00f)
						: 0.00f);
				vipProduct.setPrice(jsonObject.has("price") ? StringUtils
						.parseFloat(jsonObject.getString("price"), 0.00f)
						: 0.00f);
				vipProduct.setNote(jsonObject.optString("note", ""));
				vipProduct.setThumb(jsonObject.optString("thumb", ""));
				vipProduct.setDescription(jsonObject.optString("description",
						""));
				lstVipProduct.add(vipProduct);
			}
		} catch (Exception e) {
			LogUtils.e(e.getMessage());
		}
		return lstVipProduct;
	}

	/*public static VipPayment getVipPaymentUrl(Context context,
			String productCode, String userCode, String userName, String token) {
		VipPayment vipPayment = null;
		String url = "/Pay/productPay";
		if (NetUtils.isConnect(context)) {
			// 加密请求参数
			String request = DES2.encrypt(StringUtils.basicValue2String(
					new BasicNameValuePair("productcode", productCode),
					new BasicNameValuePair("usercode", userCode),
					new BasicNameValuePair("token", token),
					new BasicNameValuePair("username", userName)));
			String odata = RestClient.newInstance(context).doMethod(false, url,
					new BasicNameValuePair("request", request));
			String data = DES2.decrypt(odata);
			if (!TextUtils.isEmpty(data)) {
				LogUtils.i(data);
				try {
					vipPayment = new VipPayment();
					JSONObject jsonObject = new JSONObject(data);
					vipPayment.setMessage(jsonObject.optString("message", ""));
					String result = jsonObject.getString("res");
					if (TextUtils.isEmpty(result) || result.equals("{}")) {
					} else {
						JSONObject jsonResult = new JSONObject(result);
						vipPayment
								.setResult(jsonResult.has("did") ? StringUtils
										.parseInt(
												jsonResult.getString("result"),
												-1) : -1);
						vipPayment.setDid(jsonResult.has("did") ? jsonResult
								.getString("did") : "");
						vipPayment.setSid(jsonResult.has("sid") ? jsonResult
								.getString("sid") : "");
						vipPayment.setUid(jsonResult.has("uid") ? jsonResult
								.getString("uid") : "");
					}
					String lstData = jsonObject.getString("data");
					if (TextUtils.isEmpty(lstData) || lstData.equals("{}")) {
						LogUtils.i("暂无相关信息");
					} else {
						JSONObject jsonResult = new JSONObject(lstData);
						vipPayment.setSn(jsonResult.optString("sn", ""));
						vipPayment.setPaymentUrl(jsonResult.optString(
								"paymenturl", ""));
						vipPayment.setUserCode(jsonResult.optString("usercode",
								userCode));
						vipPayment.setUserName(jsonResult.optString("username",
								userName));
						vipPayment
								.setAmount(jsonResult.has("amount") ? StringUtils
										.parseFloat(
												jsonResult.getString("amount"),
												0.00f) : 0.00f);
					}
				} catch (Exception e) {
					LogUtils.e(e.getMessage());
				}
			} else {
				LogUtils.e("网络请求错误");
			}
		} else {
			LogUtils.e("网络错误");
		}
		return vipPayment;
	}*/
}
