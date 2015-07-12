package com.m1905.mobile.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.m1905.mobile.dao.Column;
import com.m1905.mobile.dao.EColumn;
import com.m1905.mobile.util.CookieUtils;
import com.m1905.mobile.util.LogUtils;
import com.m1905.mobile.util.NetUtils;
import com.m1905.mobile.util.StringUtils;

public class NearByService {
	/**
	 * 
	 * @param context
	 * @param url
	 * @param isRefresh
	 * @return
	 */
	/*public static EColumn jsonToNearBy(Context context, String url,
			boolean isRefresh) {
		EColumn eColumn = null;
		String key = "nearby_" + "ecolumn";
		boolean b = NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh);
		LogUtils.e(b + "--------------------------->");
		if (NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh)) {
			String json = RestClient.newInstance(context).doMethod(true, url);
			if (!TextUtils.isEmpty(json)) {
				try {
					eColumn = new EColumn();
					JSONObject jsonObject = new JSONObject(json);
					eColumn.setMessage(jsonObject.getString("message"));
					String result = jsonObject.getString("res");
					if (result == null || result.equals("{}")) {

					} else {
						JSONObject jsonResult = new JSONObject(result);
						eColumn.setResult(jsonResult.has("result") ? StringUtils
								.parseInt(jsonResult.getString("result")) : -1);
						eColumn.setDid(jsonResult.has("did") ? jsonResult
								.getString("did") : "");
						eColumn.setSid(jsonResult.has("sid") ? jsonResult
								.getString("sid") : "");
						eColumn.setUid(jsonResult.has("uid") ? jsonResult
								.getString("uid") : "");
					}
					String data = jsonObject.getString("data");
					if (TextUtils.isEmpty(data) || data.equals("[]")) {
					} else {
						eColumn.setColumnList(parseJsonColumnList(data));

					}
					eColumn.setCacheKey(key);
					CookieUtils.saveObject(eColumn, key);

				} catch (Exception e) {
					eColumn = (EColumn) CookieUtils.readObject(key);
					if (eColumn == null) {
						LogUtils.e(e.getMessage());
						return null;
					}
				}

			} else {
				eColumn = (EColumn) CookieUtils.readObject(key);
				if (eColumn == null)
					eColumn = new EColumn();
			}
		} else {
			eColumn = (EColumn) CookieUtils.readObject(key);
			if (eColumn == null)
				eColumn = new EColumn();
		}

		return eColumn;
	}*/

	public static List<Column> parseJsonColumnList(String json) {
		List<Column> columnList = new ArrayList<Column>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int index = 0; index < jsonArray.length(); index++) {
				JSONObject jsonObject = jsonArray.getJSONObject(index);
				Column column = new Column();
				column.setId(jsonObject.has("id") ? StringUtils
						.parseInt(jsonObject.getString("id"), -1) : -1);
				column.setType(jsonObject.has("type") ? StringUtils
						.parseInt(jsonObject.getString("type"), -1) : -1);
				column.setImg(jsonObject.has("img")?jsonObject.getString("img"):"");
				column.setTitle(jsonObject.has("title")?jsonObject.getString("title"):"");
				column.setDescription(jsonObject.has("description")?jsonObject.getString("description"):"");
				column.setUrl(jsonObject.has("url")?jsonObject.getString("url"):"");
				columnList.add(column);
			}
		} catch (Exception e) {
			LogUtils.e(e.getMessage());
		}
		return columnList;
	}
}
