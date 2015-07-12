package com.m1905.mobile.net;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.m1905.mobile.bean.KjImageBean;
import com.m1905.mobile.bean.KvBean;
import com.m1905.mobile.bean.NavigationBean;
import com.m1905.mobile.bean.RecommendBean;
import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.common.Constant;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.dao.Content;
import com.m1905.mobile.dao.EContent;
import com.m1905.mobile.dao.ETop;
import com.m1905.mobile.dao.ETopAd;
import com.m1905.mobile.dao.HomePage;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.dao.Top;
import com.m1905.mobile.dao.TopAd;
import com.m1905.mobile.dao.Update;
import com.m1905.mobile.util.CookieUtils;
import com.m1905.mobile.util.EncryptUtils;
import com.m1905.mobile.util.LogUtils;
import com.m1905.mobile.util.NetHttpConnection;
import com.m1905.mobile.util.NetUtils;
import com.m1905.mobile.util.StringUtils;

public class InitService {
	// 程序启动认证
	/*public static void initParams(Context context) {
		String json = RestClient.newInstance(context).doMethod(
				false,
				"/Member/register",
				new BasicNameValuePair("model", URLEncoder
						.encode(Identify.device.getModel())),
				new BasicNameValuePair("os", URLEncoder.encode(Identify.device
						.getOs())),
				new BasicNameValuePair("language", URLEncoder
						.encode(Identify.device.getLanguage())),
				new BasicNameValuePair("network", Identify.device
						.getNetworkTypeName()),
				new BasicNameValuePair("versionName", Constant.APP_VERSION
						.getVersionName()),
				new BasicNameValuePair("versionCode", String
						.valueOf(Constant.APP_VERSION.getVersionCode())),
				new BasicNameValuePair("versionMini", String
						.valueOf(Constant.APP_VERSION.getVersionMini())));
		LogUtils.i(json);
	}*/
	public static Update newCheckVersion(Context context){
		Update update = null;
		String path;
		try {
			update = new Update();
			path = NetHttpConnection.httpGet("http://180.168.69.121:8089/webroot/clt4/kpcp/szyx/gzz/clt5vb/kjhb/index.json", null);
			path = path.replaceAll("\n", "");
			path = path.replaceAll("\t", "");
			path = path.replaceAll(" ", "");
			path = path.substring(9, path.length());
			path = path.substring(0, path.length()-2);
			ObjectMapper mapper = new ObjectMapper();
			KjImageBean bean = mapper.readValue(path, KjImageBean.class);
			update.setResult(-1);
			update.setDid("");
			update.setSid("");
			update.setUid("");
			update.setNeedUpdate(-1);
			update.setVersionCode(-1);
			update.setVersionMini(-1);
			update.setVersionpicx(bean.getInfo().getImagelist().getSrclist()[1]);
			update.setInfo("");
			update.setSrcdate("");
			update.setDstdate("");
			update.setUrl("");
			update.setVersionName("");
			System.out.println("开机海报："+path);
			System.out.println("开机海报图："+bean.getInfo().getImagelist().getSrclist()[1]);
			return update;
		} catch (Exception e) {
			e.printStackTrace();
			return update;
		}
	}
	/*public static Update checkVersion(Context context) {
		Update update = null;
		String json = RestClient.newInstance(context).doMethod(true,
				"/Index/update");
		LogUtils.i(json);
		if (!TextUtils.isEmpty(json)) {
			update = new Update();
			try {
				JSONObject jsonObject = new JSONObject(json);
				update.setMessage(jsonObject.getString("message"));
				String res = jsonObject.getString("res");
				if (TextUtils.isEmpty(res) || res.equals("{}")) {

				} else {
					JSONObject jsonResult = new JSONObject(res);
					update.setResult(jsonResult.has("result") ? StringUtils
							.parseInt(jsonResult.getString("result")) : -1);
					update.setDid(jsonResult.has("did") ? jsonResult
							.getString("did") : "");
					update.setSid(jsonResult.has("sid") ? jsonResult
							.getString("sid") : "");
					update.setUid(jsonResult.has("uid") ? jsonResult
							.getString("uid") : "");
				}
				String data = jsonObject.getString("data");
				if (TextUtils.isEmpty(data) || res.equals("{}")) {

				} else {
					JSONObject jsonResult = new JSONObject(data);
					update.setNeedUpdate(jsonResult.has("needupdate") ? StringUtils
							.parseInt(jsonResult.getString("needupdate")) : -1);
					update.setVersionCode(jsonResult.has("versioncode") ? StringUtils
							.parseInt(jsonResult.getString("versioncode")) : -1);
					update.setVersionMini(jsonResult.has("versionmini") ? StringUtils
							.parseLong(jsonResult.getString("versionmini"))
							: -1);
					update.setVersionpicx(jsonResult.has("versionpicy") ? jsonResult
							.getString("versionpicy") : "");
					update.setInfo(jsonResult.getString("info"));
					update.setSrcdate(jsonResult.has("srcdata") ? jsonResult
							.getString("srcdata") : "");
					update.setDstdate(jsonResult.has("dstdata") ? jsonResult
							.getString("dstdata") : "");
					update.setUrl(jsonResult.has("url") ? jsonResult
							.getString("url") : "");
					update.setVersionName(jsonResult.has("versionname") ? jsonResult
							.getString("versionname") : "");
				}
			} catch (Exception e) {
				LogUtils.e("数据返回：" + update.getMessage() + " error by: "
						+ e.getMessage());
			}
		}
		return update;
	}*/

	public static boolean downloadSplash(Context context, String splashUrl) {
		if (TextUtils.isEmpty(splashUrl))
			return false;
		LogUtils.i("下载图片");
		File file = new File(AppConfig.M1905_CACHE_PATH,
				EncryptUtils.MD5(splashUrl));
		return RestClient.newInstance(context).saveImageToFile(splashUrl, file);
	}

	/**
	 * 
	 * @param context
	 * @param url
	 * @param isRefresh
	 * @return
	 */
	public static HomePage jsonToHomePage(Context context, String url,
			boolean isRefresh) {
		HomePage homePage = null;
		String key = "index_" + "homepage";
		boolean b = NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh);
		LogUtils.e(b + "--------------------------->");
		ObjectMapper mapper = new ObjectMapper();
		homePage = new HomePage();
		if(b){
			try {
				String json = NetHttpConnection.httpGet(TianyiContent.NavigationUrl, null);
				NavigationBean navigation = mapper.readValue(json, NavigationBean.class);
				for(int i=0;i<navigation.getTabs().length;i++){
					if(navigation.getTabs()[i].getName().equals("KV图")){
						try {
							ETop eTop = new ETop();
							String kvjson = NetHttpConnection.httpGet(TianyiContent.TianyiUrl+navigation.getTabs()[i].getPath(), null);
							KvBean kv = mapper.readValue(kvjson, KvBean.class);
							eTop.setTitle("KV");
							eTop.setTopList(KvTopList(kv));
							homePage.setTopData(eTop);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else if(navigation.getTabs()[i].getName().equals("强片推荐")){
						EContent eContent = new EContent();
						String qpjson = NetHttpConnection.httpGet(TianyiContent.TianyiUrl+navigation.getTabs()[i].getPath(), null);
						System.out.println("强片推荐内容：：：："+qpjson);
						RecommendBean rBean = mapper.readValue(qpjson, RecommendBean.class);
						eContent.setTitle(rBean.getLabel().getName());
						eContent.setContentList(ContentList(rBean));
						homePage.setTodayData(eContent);
					}else if(navigation.getTabs()[i].getName().equals("好莱坞")){
						EContent eContent = new EContent();
						String xpjson = NetHttpConnection.httpGet(TianyiContent.TianyiUrl+navigation.getTabs()[i].getPath(), null);
						RecommendBean rBean = mapper.readValue(xpjson, RecommendBean.class);
						eContent.setTitle(rBean.getLabel().getName());
						eContent.setContentList(ContentList(rBean));
						homePage.setHotData(eContent);
					}else if(navigation.getTabs()[i].getName().equals("日韩专区")){
						EContent eContent = new EContent();
						String xpjson = NetHttpConnection.httpGet(TianyiContent.TianyiUrl+navigation.getTabs()[i].getPath(), null);
						RecommendBean rBean = mapper.readValue(xpjson, RecommendBean.class);
						eContent.setTitle(rBean.getLabel().getName());
						eContent.setContentList(ContentList(rBean));
						homePage.setRhData(eContent);
					}else if(navigation.getTabs()[i].getName().equals("异色欧美")){
						EContent eContent = new EContent();
						String xpjson = NetHttpConnection.httpGet(TianyiContent.TianyiUrl+navigation.getTabs()[i].getPath(), null);
						RecommendBean rBean = mapper.readValue(xpjson, RecommendBean.class);
						eContent.setTitle(rBean.getLabel().getName());
						eContent.setContentList(ContentList(rBean));
						homePage.setYsData(eContent);
					}
					else if(navigation.getTabs()[i].getName().equals("华语强档")){
						EContent eContent = new EContent();
						String xpjson = NetHttpConnection.httpGet(TianyiContent.TianyiUrl+navigation.getTabs()[i].getPath(), null);
						RecommendBean rBean = mapper.readValue(xpjson, RecommendBean.class);
						eContent.setTitle(rBean.getLabel().getName());
						eContent.setContentList(ContentList(rBean));
						homePage.setCctv6Data(eContent);
					}else if(navigation.getTabs()[i].getName().equals("新片预告")){
						EContent eContent = new EContent();
						String xpjson = NetHttpConnection.httpGet(TianyiContent.TianyiUrl+navigation.getTabs()[i].getPath(), null);
						RecommendBean rBean = mapper.readValue(xpjson, RecommendBean.class);
						eContent.setTitle(rBean.getLabel().getName());
						eContent.setContentList(ContentList(rBean));
						homePage.setLatestData(eContent);
					}
				}
				homePage.setCacheKey(key);
				CookieUtils.saveObject(homePage, key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			homePage = (HomePage) CookieUtils.readObject(key);
		}
		
		if(homePage==null){
			System.out.println("原数据空");
		}else{
			System.out.println("原数据不为空");
		}
		
		
		/*if (NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh)) {
			String json = RestClient.newInstance(context).doMethod(true, url);
			if (!TextUtils.isEmpty(json)) {
				try {
					homePage = new HomePage();
					JSONObject jsonObject = new JSONObject(json);
					homePage.setMessage(jsonObject.getString("message"));
					String result = jsonObject.getString("res");
					if (result == null || result.equals("{}")) {
					} else {
						JSONObject jsonResult = new JSONObject(result);
						homePage.setResult(jsonResult.has("result") ? StringUtils
								.parseInt(jsonResult.getString("result")) : -1);
						homePage.setDid(jsonResult.has("did") ? jsonResult
								.getString("did") : "");
						homePage.setSid(jsonResult.has("sid") ? jsonResult
								.getString("sid") : "");
						homePage.setUid(jsonResult.has("uid") ? jsonResult
								.getString("uid") : "");
					}
					String data = jsonObject.getString("data");
					if (data == null || data.equals("{}")) {
					} else {
						JSONObject contentObject = new JSONObject(data);
						// 处理头图
						String topListJson = contentObject.has("topList") ? contentObject
								.getString("topList") : "";

						if (TextUtils.isEmpty(topListJson)
								|| topListJson.equalsIgnoreCase("null")
								|| topListJson.equals("{}")) {
							homePage.setTopData(null);
						} else {
							ETop eTop = new ETop();
							JSONObject topObject = new JSONObject(topListJson);
							eTop.setTitle(topObject.has("title") ? topObject
									.getString("title") : "");
							eTop.setTopList(parseJsonTopList(topListJson));
							homePage.setTopData(eTop);
						}

						// 免费剧场
						String freeListJson = contentObject
								.getString("freeList");
						if (TextUtils.isEmpty(freeListJson)
								|| freeListJson.equalsIgnoreCase("null")
								|| freeListJson.equals("{}")) {
							homePage.setFreeData(null);
						} else {
							ETop eTop = new ETop();
							JSONObject freeObject = new JSONObject(freeListJson);
							eTop.setTitle(freeObject.has("title") ? freeObject
									.getString("title") : "");
							eTop.setTopList(parseJsonTopList(freeListJson));
							homePage.setFreeData(eTop);
						}
						// 今日推荐
						String todayListJson = contentObject.has("todayList") ? contentObject
								.getString("todayList") : "";
						if (TextUtils.isEmpty(todayListJson)
								|| todayListJson.equalsIgnoreCase("null")
								|| todayListJson.equals("{}")) {
							homePage.setTodayData(null);
						} else {
							EContent eContent = new EContent();
							JSONObject todayObject = new JSONObject(
									todayListJson);
							eContent.setTitle(todayObject.has("title") ? todayObject
									.getString("title") : "");
							eContent.setContentList(parseJsonContentList(todayListJson));
							homePage.setTodayData(eContent);
						}

						// 最新上线
						String latestListJson = contentObject.has("latestList") ? contentObject
								.getString("latestList") : "";
						if (TextUtils.isEmpty(latestListJson)
								|| latestListJson.equalsIgnoreCase("null")
								|| latestListJson.equals("{}")) {
							homePage.setLatestData(null);
						} else {
							EContent eContent = new EContent();
							JSONObject latestObject = new JSONObject(
									latestListJson);
							eContent.setTitle(latestObject.has("title") ? latestObject
									.getString("title") : "");
							eContent.setContentList(parseJsonContentList(latestListJson));
							homePage.setLatestData(eContent);
						}

						// 热门大片
						String hotListJson = contentObject.has("hotList") ? contentObject
								.getString("hotList") : "";
						if (TextUtils.isEmpty(hotListJson)
								|| hotListJson.equalsIgnoreCase("null")
								|| hotListJson.equals("{}")) {
							homePage.setHotData(null);
						} else {
							EContent eContent = new EContent();
							JSONObject hotObject = new JSONObject(hotListJson);
							eContent.setTitle(hotObject.getString("title"));
							eContent.setContentList(parseJsonContentList(hotListJson));
							homePage.setHotData(eContent);
						}

						// CCTV-6热播影片
						String cctv6ListJson = contentObject
								.has("cctv6hotList") ? contentObject
								.getString("cctv6hotList") : "";
						if (TextUtils.isEmpty(cctv6ListJson)
								|| cctv6ListJson.equalsIgnoreCase("null")
								|| cctv6ListJson.equals("{}")) {
							homePage.setCctv6Data(null);
						} else {
							EContent eContent = new EContent();
							JSONObject cctv6Object = new JSONObject(
									cctv6ListJson);
							eContent.setTitle(cctv6Object.has("title") ? cctv6Object
									.getString("title") : "");
							eContent.setContentList(parseJsonContentList(cctv6ListJson));
							homePage.setCctv6Data(eContent);
						}

					}
					homePage.setCacheKey(key);
					CookieUtils.saveObject(homePage, key);

				} catch (Exception e) {
					LogUtils.e(e.getMessage());
				}

			} else {
				homePage = (HomePage) CookieUtils.readObject(key);
			}
		} else {
			homePage = (HomePage) CookieUtils.readObject(key);
		}*/

		return homePage;
	}
	public static List<Top> KvTopList(KvBean json){
		System.out.println("KV个数:::::::::"+json.getData().length);
		List<Top> topList = new ArrayList<Top>();
		for(int i=0;i<json.getData().length;i++){
			Top top = new Top();
			top.setId(Integer.parseInt(json.getData()[i].getContentId()));
			top.setType(-1);
			top.setTitle(json.getData()[i].getTitle());
			top.setImg("http://img3.tv189.cn/"+json.getData()[i].getCover());
			System.out.println("http://img3.tv189.cn/"+json.getData()[i].getCover());
			top.setDesc("");
			top.setUrl("");
			topList.add(top);
		}
		System.out.println("数据嵌套个数："+topList.size());
		return topList;
	}
	public static List<Content> ContentList(RecommendBean json){
		System.out.println(json.getLabel().getName()+"::"+json.getData().length);
		List<Content> contentList = new ArrayList<Content>();
		for(int i=0;i<json.getData().length;i++){
			Content content = new Content();
			content.setId(Integer.parseInt(json.getData()[i].getContentId()));
			content.setType(-1);
			content.setTitle(json.getData()[i].getTitle());
			content.setImg("http://img3.tv189.cn/"+json.getData()[i].getCover());
			content.setUrl(json.getData()[i].getAspect());
			contentList.add(content);
		}
		System.out.println(json.getLabel().getName()+"嵌套完成");
		return contentList;
	}
	public static List<Content> parseJsonContentList(String json) {
		List<Content> contentList = new ArrayList<Content>();
		try {
			JSONObject jsonobject = new JSONObject(json);
			Iterator iterator = jsonobject.keys();
			while (iterator.hasNext()) {
				Object s = iterator.next();
				if (s.toString().equals("title")) {
					continue;
				}
				JSONObject contentObject = jsonobject.getJSONObject(s
						.toString());
				Content content = new Content();
				content.setId(contentObject.has("id") ? StringUtils.parseInt(
						contentObject.getString("id"), -1) : -1);
				content.setType(contentObject.has("type") ? StringUtils
						.parseInt(contentObject.getString("type"), -1) : -1);
				content.setTitle(contentObject.has("title") ? contentObject
						.getString("title") : "");
				content.setImg(contentObject.has("img") ? contentObject
						.getString("img") : "");
				content.setUrl(contentObject.has("url") ? contentObject
						.getString("url") : "");
				contentList.add(content);
			}
		} catch (Exception e) {
			LogUtils.e(e.getMessage());
		}
		return contentList;
	}
	public static List<Top> parseJsonTopList(String json) {
		List<Top> topList = new ArrayList<Top>();
		try {
			JSONObject jsonobject = new JSONObject(json);
			Iterator iterator = jsonobject.keys();
			while (iterator.hasNext()) {
				Object s = iterator.next();
				if (s.toString().equals("title")) {
					continue;
				}
				JSONObject topObject = jsonobject.getJSONObject(s.toString());
				Top top = new Top();
				top.setId(topObject.has("id") ? StringUtils.parseInt(
						topObject.getString("id"), -1) : -1);
				top.setType(topObject.has("type") ? StringUtils.parseInt(
						topObject.getString("type"), -1) : -1);
				top.setTitle(topObject.has("title") ? topObject
						.getString("title") : "");
				top.setImg(topObject.has("img") ? topObject.getString("img")
						: "");
				top.setDesc(topObject.has("desc") ? topObject.getString("desc")
						: "");
				top.setUrl(topObject.has("url") ? topObject.getString("url")
						: "");
				topList.add(top);
			}
		} catch (Exception e) {
			LogUtils.e(e.getMessage());
		}
		return topList;
	}

	

	/*public static ETopAd getUserTopAd(Context context, boolean isRefresh) {
		ETopAd eTopAd = null;
		String url = "/User/topPic";
		String key = "top_ad_key";
		boolean isOkRefresh = NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh);
		LogUtils.i("url: " + url + "  isRefresh: " + isRefresh
				+ "  isOkRefresh: " + isOkRefresh);
		if (isOkRefresh) {
			String json = RestClient.newInstance(context).doMethod(false, url);
			LogUtils.i(json);
			if (!TextUtils.isEmpty(json)) {
				try {
					eTopAd = new ETopAd();
					JSONObject jsonObject = new JSONObject(json);
					eTopAd.setMessage(jsonObject.getString("message"));
					String result = jsonObject.getString("res");
					if (TextUtils.isEmpty(result) || result.equals("{}")) {
					} else {
						JSONObject jsonResult = new JSONObject(result);
						eTopAd.setResult(jsonResult.has("did") ? StringUtils
								.parseInt(jsonResult.getString("result"), -1)
								: -1);
						eTopAd.setDid(jsonResult.has("did") ? jsonResult
								.getString("did") : "");
						eTopAd.setSid(jsonResult.has("sid") ? jsonResult
								.getString("sid") : "");
						eTopAd.setUid(jsonResult.has("uid") ? jsonResult
								.getString("uid") : "");
					}
					String data = jsonObject.getString("data");
					if (TextUtils.isEmpty(data) || data.equals("{}")) {

					} else {
						eTopAd.setLstTopAd(parseJsonTopAd(data));
					}
					eTopAd.setCacheKey(key);
					CookieUtils.saveObject(eTopAd, key);
				} catch (Exception e) {
					LogUtils.e(e.getMessage());
					// 网络连接正常，但是无数据
					// eContent = (ESearchContent) CookieUtils.readObject(key);
				}
			} else {
				eTopAd = (ETopAd) CookieUtils.readObject(key);
			}
		} else {
			eTopAd = (ETopAd) CookieUtils.readObject(key);
		}
		return eTopAd;
	}*/

	private static List<TopAd> parseJsonTopAd(String json) {
		List<TopAd> lstTopAd = new ArrayList<TopAd>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			LogUtils.i(json);
			for (int index = 0; index < jsonArray.length(); index++) {
				JSONObject jsonObject = jsonArray.getJSONObject(index);
				TopAd topAd = new TopAd();
				topAd.setId(jsonObject.has("movieid") ? StringUtils.parseInt(
						jsonObject.getString("movieid"), -1) : -1);
				topAd.setTitle(jsonObject.has("title") ? jsonObject
						.getString("title") : "");
				topAd.setImg(jsonObject.has("img") ? jsonObject
						.getString("img") : "");
				topAd.setType(jsonObject.has("type") ? StringUtils.parseInt(
						jsonObject.getString("type"), -1) : -1);
				topAd.setUrl(jsonObject.has("url") ? jsonObject
						.getString("url") : "");
				lstTopAd.add(topAd);
			}
		} catch (Exception e) {
			LogUtils.e(e.getMessage());
		}
		return lstTopAd;
	}

}
