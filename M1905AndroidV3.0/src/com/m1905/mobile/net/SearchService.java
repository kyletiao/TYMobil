package com.m1905.mobile.net;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.m1905.mobile.bean.HotBean;
import com.m1905.mobile.bean.ImageUrlBean;
import com.m1905.mobile.bean.ScreeningBean;
import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.dao.EHot;
import com.m1905.mobile.dao.ESearchContent;
import com.m1905.mobile.dao.Hot;
import com.m1905.mobile.dao.SearchContent;
import com.m1905.mobile.util.CookieUtils;
import com.m1905.mobile.util.LogUtils;
import com.m1905.mobile.util.NetHttpConnection;
import com.m1905.mobile.util.NetUtils;
import com.m1905.mobile.util.StringUtils;
import com.telecomsdk.phpso.TysxOA;

public class SearchService {

	/**
	 * 搜索
	 * 
	 * @param context
	 * @param kw
	 *            //关键字
	 * @param pi
	 *            //页码
	 * @param ps
	 *            //页容量
	 * @param isRefresh
	 *            是否刷新 //刷新失败将获取本地数据
	 * @return
	 */
	private static ESearchContent search(Context context, String kw, String pi,
			String ps, boolean isRefresh) {
		ESearchContent eContent = null;
		ObjectMapper mapper = new ObjectMapper();
		String url = "/search/searchList";
		String key = "search_" + kw + "_" + pi + "_" + ps + "_key";
		boolean isOkRefresh = NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh);
		LogUtils.i("url: " + url + "  kw: " + kw + "  pi：" + pi + "  ps：" + ps
				+ "  isRefresh: " + isRefresh + "  isOkRefresh: " + isOkRefresh);
		if (isOkRefresh) {
			TysxOA oa = new TysxOA(context);
			String searchcontent = oa.getSearchResult("1", TianyiContent.token, kw, Integer.parseInt(pi), Integer.parseInt(ps), "1000000054", "description,director,cast,averageScoreValue", TianyiContent.appid, TianyiContent.devid, TianyiContent.appSecret);
			System.out.println("搜索内容："+searchcontent);
			try {
				eContent = new ESearchContent();
				ScreeningBean bean = mapper.readValue(searchcontent, ScreeningBean.class);
				eContent.setPi(Integer.parseInt(pi));
				eContent.setPs(Integer.parseInt(ps));
				eContent.setCount(bean.getInfo().getTotal());
				eContent.setLstSearchContents(newParseJsonSearch(bean));
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*eContent.setPi(jsonInfo.has("pi") ? StringUtils
					.parseInt(jsonInfo.getString("pi"),
							Integer.parseInt(pi)) : Integer
					.parseInt(pi));
			eContent.setPs(jsonInfo.has("ps") ? StringUtils
					.parseInt(jsonInfo.getString("ps"), StringUtils
							.parseInt(ps, AppConfig.PAGE_SIZE))
					: StringUtils.parseInt(ps, AppConfig.PAGE_SIZE));
			eContent.setCount(jsonInfo.has("count") ? StringUtils
					.parseInt(jsonInfo.getString("count"), 0) : 0);
			eContent.setLstSearchContents(parseJsonSearch(data));*/
			
			
			/*String json = RestClient.newInstance(context).doMethod(false, url,
					new BasicNameValuePair("kw", URLEncoder.encode(kw)),
					new BasicNameValuePair("pi", pi),
					new BasicNameValuePair("ps", ps));
			LogUtils.i(json);
			if (!TextUtils.isEmpty(json)) {
				try {
					eContent = new ESearchContent();
					JSONObject jsonObject = new JSONObject(json);
					eContent.setMessage(jsonObject.getString("message"));
					String result = jsonObject.getString("res");
					if (TextUtils.isEmpty(result) || result.equals("{}")) {
					} else {
						JSONObject jsonResult = new JSONObject(result);
						eContent.setResult(jsonResult.has("did") ? StringUtils
								.parseInt(jsonResult.getString("result"), -1)
								: -1);
						eContent.setDid(jsonResult.has("did") ? jsonResult
								.getString("did") : "");
						eContent.setSid(jsonResult.has("sid") ? jsonResult
								.getString("sid") : "");
						eContent.setUid(jsonResult.has("uid") ? jsonResult
								.getString("uid") : "");
					}
					String data = jsonObject.getString("data");
					if (TextUtils.isEmpty(data) || data.equals("{}")) {

					} else {
						JSONObject jsonInfo = new JSONObject(data);
						eContent.setPi(jsonInfo.has("pi") ? StringUtils
								.parseInt(jsonInfo.getString("pi"),
										Integer.parseInt(pi)) : Integer
								.parseInt(pi));
						eContent.setPs(jsonInfo.has("ps") ? StringUtils
								.parseInt(jsonInfo.getString("ps"), StringUtils
										.parseInt(ps, AppConfig.PAGE_SIZE))
								: StringUtils.parseInt(ps, AppConfig.PAGE_SIZE));
						eContent.setCount(jsonInfo.has("count") ? StringUtils
								.parseInt(jsonInfo.getString("count"), 0) : 0);
						eContent.setLstSearchContents(parseJsonSearch(data));
					}
					eContent.setCacheKey(key);
					CookieUtils.saveObject(eContent, key);
				} catch (Exception e) {
					LogUtils.e(e.getMessage());
					// 网络连接正常，但是无数据
					// eContent = (ESearchContent) CookieUtils.readObject(key);
				}
			} else {
				eContent = (ESearchContent) CookieUtils.readObject(key);
			}*/
		} else {
			eContent = (ESearchContent) CookieUtils.readObject(key);
		}
		return eContent;
	}

	/**
	 * 搜索
	 * 
	 * @param context
	 * @param kw
	 *            //关键字
	 * @param pi
	 *            //页码
	 * @param ps
	 *            //页容量
	 * @param isRefresh
	 *            是否刷新 //刷新失败将获取本地数据
	 * @return
	 */
	public static ESearchContent search(Context context, String kw, int pi,
			int ps, boolean isRefresh) {
		return search(context, kw, String.valueOf(pi), String.valueOf(ps),
				isRefresh);
	}

	/**
	 * 搜索
	 * 
	 * @param context
	 * @param kw
	 *            //关键字
	 * @param pi
	 *            //页码
	 * @param isRefresh
	 *            是否刷新 //刷新失败将获取本地数据
	 * @return
	 */
	public static ESearchContent search(Context context, String kw, int pi,
			boolean isRefresh) {
		return search(context, kw, pi, AppConfig.PAGE_SIZE, isRefresh);
	}

	/**
	 * 热词搜索
	 * 
	 * @param context
	 * @param pi
	 * @param ps
	 * @param isRefresh
	 * @return
	 */
	private static EHot getHot(Context context, String pi, String ps,
			boolean isRefresh) {
		EHot eHot = null;
		List<Hot> lstHot = new ArrayList<Hot>();
		try {
			String hotContent = NetHttpConnection.httpGet("http://180.168.69.121:8089/webroot/clt4/kpcp/szyx/ikdy/ss/ssgjc/index.json", null);
			System.out.println("热词内容："+hotContent);
			ObjectMapper mapper = new ObjectMapper();
			HotBean hbean = mapper.readValue(hotContent, HotBean.class);
			eHot = new EHot();
			
			eHot.setPi(1);
			eHot.setPs(10);
			eHot.setCount(hbean.getAreaCode());
			for(int i=0;i<hbean.getData().length;i++){
				Hot hot = new Hot();
				hot.setTitle(hbean.getData()[i].getKeyword());
				hot.setNum(1);
				lstHot.add(hot);
			}
			
			eHot.setLstHots(lstHot);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*EHot eHot = null;
		String url = "/search/hotWord";
		String key = "hotWord_" + pi + "_" + ps + "_key";
		boolean isOkRefresh = NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh);
		LogUtils.i("url: " + url + "  pi：" + pi + "  ps：" + ps
				+ "  isRefresh: " + isRefresh + "  isOkRefresh: " + isOkRefresh);
		if (isOkRefresh) {
			String json = RestClient.newInstance(context).doMethod(false, url,
					new BasicNameValuePair("pi", pi),
					new BasicNameValuePair("ps", ps));
			LogUtils.i(json);
			if (!TextUtils.isEmpty(json)) {
				try {
					eHot = new EHot();
					JSONObject jsonObject = new JSONObject(json);
					eHot.setMessage(jsonObject.getString("message"));
					String result = jsonObject.getString("res");
					if (TextUtils.isEmpty(result) || result.equals("{}")) {
					} else {
						JSONObject jsonResult = new JSONObject(result);
						eHot.setResult(jsonResult.has("did") ? StringUtils
								.parseInt(jsonResult.getString("result"), -1)
								: -1);
						eHot.setDid(jsonResult.has("did") ? jsonResult
								.getString("did") : "");
						eHot.setSid(jsonResult.has("sid") ? jsonResult
								.getString("sid") : "");
						eHot.setUid(jsonResult.has("uid") ? jsonResult
								.getString("uid") : "");
					}
					String data = jsonObject.getString("data");
					if (TextUtils.isEmpty(data) || data.equals("{}")) {

					} else {
						JSONObject jsonInfo = new JSONObject(data);
						eHot.setPi(jsonInfo.has("pi") ? StringUtils.parseInt(
								jsonInfo.getString("pi"), Integer.parseInt(pi))
								: Integer.parseInt(pi));
						eHot.setPs(jsonInfo.has("ps") ? StringUtils.parseInt(
								jsonInfo.getString("ps"),
								StringUtils.parseInt(ps, AppConfig.PAGE_SIZE))
								: StringUtils.parseInt(ps, AppConfig.PAGE_SIZE));
						eHot.setCount(jsonInfo.has("count") ? StringUtils
								.parseInt(jsonInfo.getString("count"), 0) : 0);
						eHot.setLstHots(parseJsonHot(data));
					}
					eHot.setCacheKey(key);
					CookieUtils.saveObject(eHot, key);
				} catch (Exception e) {
					LogUtils.e(e.getMessage());
					eHot = (EHot) CookieUtils.readObject(key);
				}
			} else {
				eHot = (EHot) CookieUtils.readObject(key);
			}
		} else {
			eHot = (EHot) CookieUtils.readObject(key);
		}*/
		return eHot;
	}

	/**
	 * 热词搜索
	 * 
	 * @param context
	 * @param pi
	 * @param ps
	 * @param isRefresh
	 * @return
	 */
	public static EHot getHot(Context context, int pi, int ps, boolean isRefresh) {
		return getHot(context, String.valueOf(pi), String.valueOf(ps),
				isRefresh);
	}

	/**
	 * 热词搜索 10条 1页
	 * 
	 * @param context
	 * @param isRefresh
	 * @return
	 */
	public static EHot getHot(Context context, boolean isRefresh) {
		return getHot(context, 1, 10, isRefresh);
	}

	/**
	 * 解析热词数据
	 * 
	 * @param json
	 * @return
	 */
	private static List<Hot> parseJsonHot(String json) {
		List<Hot> lstHot = new ArrayList<Hot>();
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
				Hot hot = new Hot();
				JSONObject jsonObject = jsonobject.getJSONObject(s.toString());
				hot.setTitle(jsonObject.has("title") ? jsonObject
						.getString("title") : "");
				hot.setNum(jsonObject.has("num") ? StringUtils.parseInt(
						jsonObject.getString("num"), 0) : 0);
				lstHot.add(hot);
			}
		} catch (Exception e) {
			LogUtils.e(e.getMessage());
		}
		return lstHot;
	}
	private static List<SearchContent> newParseJsonSearch(ScreeningBean bean){
		List<SearchContent> content = new ArrayList<SearchContent>();
		ObjectMapper mapper = new ObjectMapper();
		ImageUrlBean imagebean;
		try {
			for(int i=0;i<bean.getInfo().getData().length;i++){
				SearchContent scontent = new SearchContent();
				scontent.setId(Integer.parseInt(bean.getInfo().getData()[i].getContentId()));
				scontent.setTitle(bean.getInfo().getData()[i].getTitle());
				ImageUrlBean ibean = mapper.readValue(bean.getInfo().getData()[i].getCover(), ImageUrlBean.class);
				scontent.setImg(ibean.getImage().getM7());
				scontent.setType(-1);
				scontent.setUrl("");
				scontent.setSorce(bean.getInfo().getData()[i].getAverageScoreValue());
				scontent.setDircotor(bean.getInfo().getData()[i].getDirector());
				scontent.setActor(bean.getInfo().getData()[i].getCast());
				scontent.setDescription(bean.getInfo().getData()[i].getDescription());
				content.add(scontent);
				System.out.println("添加次数"+i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	private static List<SearchContent> parseJsonSearch(String json) {
		List<SearchContent> lstSearch = new ArrayList<SearchContent>();
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
				SearchContent search = new SearchContent();
				JSONObject jsonObject = jsonobject.getJSONObject(s.toString());
				search.setId(jsonObject.has("id") ? StringUtils.parseInt(
						jsonObject.getString("id"), -1) : -1);
				search.setTitle(jsonObject.has("title") ? jsonObject
						.getString("title") : "");
				search.setImg(jsonObject.has("img") ? jsonObject
						.getString("img") : "");
				search.setDircotor(jsonObject.has("director") ? jsonObject
						.getString("director") : "");
				search.setActor(jsonObject.has("starring") ? jsonObject
						.getString("starring") : "");
				search.setMtype(jsonObject.has("mtype") ? jsonObject
						.getString("mtype") : "");
				search.setClime(jsonObject.has("clime") ? jsonObject
						.getString("clime") : "");
				search.setSorce(jsonObject.has("score") ? StringUtils
						.parseFloat(jsonObject.getString("score"), 0.0f) : 0.0f);
				search.setYears(jsonObject.has("years") ? jsonObject
						.getString("years") : "");
				search.setDescription(jsonObject.has("description") ? jsonObject
						.getString("description") : "");
				search.setBmonth(jsonObject.has("bmonth") ? StringUtils
						.parseInt(jsonObject.getString("bmonth"), 0) : 0);
				search.setPrice(jsonObject.has("price") ? StringUtils
						.parseFloat(jsonObject.getString("price"), 0.0f) : 0.0f);
				search.setType(jsonObject.has("type") ? StringUtils.parseInt(
						jsonObject.getString("type"), 0) : 0);
				search.setUrl(jsonObject.has("url") ? jsonObject
						.getString("url") : "");
				lstSearch.add(search);
			}
		} catch (Exception e) {
			LogUtils.e(e.getMessage());
		}
		return lstSearch;
	}
}
