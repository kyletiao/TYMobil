package com.m1905.mobile.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.m1905.mobile.bean.ContentPath;
import com.m1905.mobile.bean.DetailsBean;
import com.m1905.mobile.bean.ImageUrlBean;
import com.m1905.mobile.bean.ScreeningBean;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.dao.EReleated;
import com.m1905.mobile.dao.EVideo;
import com.m1905.mobile.dao.Releated;
import com.m1905.mobile.dao.Video;
import com.m1905.mobile.dao.VipVideoDetail;
import com.m1905.mobile.dao.VodVideoDetail;
import com.m1905.mobile.util.CookieUtils;
import com.m1905.mobile.util.DES2;
import com.m1905.mobile.util.LogUtils;
import com.m1905.mobile.util.NetUtils;
import com.m1905.mobile.util.StringUtils;
import com.telecomsdk.phpso.TysxOA;

public class VideoService {

	public static EVideo jsonToVipVideo(Context context, String url,
			String type, String pi, String ps, boolean isRefresh) {

		return jsonToVipVideo(context, url, type, pi, ps, null, isRefresh);
	}

	public static EVideo jsonToVipVideo(Context context, String url,
			String type, String pi, String ps, String or, boolean isRefresh) {
		return jsonToVipVideo(context, url, type, pi, ps, or, null, isRefresh);
	}

	public static EVideo jsonToVipVideo(Context context, String url,
			String type, String pi, String ps, String or, String mtype,
			boolean isRefresh) {
		return jsonToVipVideo(context, url, type, pi, ps, or, mtype, null,
				isRefresh);
	}

	public static EVideo jsonToVipVideo(Context context, String url,
			String type, String pi, String ps, String or, String mtype,
			String years, boolean isRefresh) {
		return jsonToVipVideo(context, url, type, pi, ps, or, mtype, years,
				null, isRefresh);
	}

	/**
	 * 
	 * @param context
	 * @param url
	 * @param type
	 * @param pi
	 * @param ps
	 * @param or
	 * @param mtype
	 * @param years
	 * @param areas
	 * @param isRefresh
	 * @return
	 */
	public static EVideo jsonToVipVideo(Context context, String url,
			String type, String pi, String ps, String or, String mtype,
			String years, String areas, boolean isRefresh) {
		EVideo eVideo = null;
		ObjectMapper mapper = new ObjectMapper();
		String key = "vipvideo_" + type + "_" + pi + "_" + ps + "_" + or + "_"
				+ mtype + "_" + years + "_" + areas + "_vip";
		System.out.println(key);
		boolean b = NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh);
		LogUtils.e(b + "--------------------------->"
				+ CookieUtils.isReadDataCache(key));
		
		if (NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh)) {
			System.out.println("筛选数据获取");
			TysxOA oa = new TysxOA(context);
			List<BasicNameValuePair> vp = new ArrayList<BasicNameValuePair>();
			if(!mtype.equals("全部")){
				vp.add(new BasicNameValuePair("categoryname", mtype));
			}
			if(!years.equals("全部")){
				if(years.equals("21世纪初")){
					vp.add(new BasicNameValuePair("releaseyear","2001-2002-2003-2004-2005-2006-2007-2008-2009-2010"));
				}else if(years.equals("90年代")){
					vp.add(new BasicNameValuePair("releaseyear", "1991-1992-1993-1994-1995-1996-1997-1998-1999"));
				}else if(years.equals("80年代")){
					vp.add(new BasicNameValuePair("releaseyear", "1981-1982-1983-1984-1985-1986-1987-1988-1989"));
				}else{
					vp.add(new BasicNameValuePair("releaseyear", years));
				}
			}
			if(!areas.equals("0")){
				vp.add(new BasicNameValuePair("originalcountry", areas));
			}
			if(or.equals("最热")){
				vp.add(new BasicNameValuePair("orderby", "playcount"));
			}
			
			vp.add(new BasicNameValuePair("ecid", "55"));
			vp.add(new BasicNameValuePair("productId", "1000000054"));
			vp.add(new BasicNameValuePair("isprevue", "1"));
			String jsons = oa.search("1", TianyiContent.token, "1", Integer.parseInt(pi), Integer.parseInt(ps), vp, "cover", TianyiContent.appid, TianyiContent.devid, TianyiContent.appSecret);
			
			System.out.println("筛选数据为："+jsons);
			
			try {
				ScreeningBean bean = mapper.readValue(jsons, ScreeningBean.class);
				System.out.println("电影名称："+bean.getInfo().getData()[0].getTitle()+"图片路径："+bean.getInfo().getData()[0].getCover()+"内容id:"+bean.getInfo().getData()[0].getContentId());
				eVideo = ScreeningVideo(bean,Integer.parseInt(pi),Integer.parseInt(ps));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("筛选数据解析失败");
			}
			/*String json = RestClient.newInstance(context).doMethod(false, url,
					new BasicNameValuePair("type", type),
					new BasicNameValuePair("pi", pi),
					new BasicNameValuePair("ps", ps),
					new BasicNameValuePair("or", or),+
					new BasicNameValuePair("mtype", mtype),
					new BasicNameValuePair("years", years),
					new BasicNameValuePair("areas", areas));
			LogUtils.i(json);
			eVideo = parseJsonVideo(context, key, json);*/
			
		} else {
			eVideo = (EVideo) CookieUtils.readObject(key);
		}

		return eVideo;
	}

	public static EVideo jsonToGeneralVideo(Context context, String url,
			String type, String pi, String ps, boolean isRefresh) {
		return jsonToGeneralVideo(context, url, type, pi, ps, null, isRefresh);
	}

	public static EVideo jsonToGeneralVideo(Context context, String url,
			String type, String pi, String ps, String or, boolean isRefresh) {
		return jsonToGeneralVideo(context, url, type, pi, ps, or, null,
				isRefresh);
	}

	public static EVideo jsonToGeneralVideo(Context context, String url,
			String type, String pi, String ps, String or, String mtype,
			boolean isRefresh) {
		return jsonToGeneralVideo(context, url, type, pi, ps, or, mtype, null,
				isRefresh);
	}

	public static EVideo jsonToGeneralVideo(Context context, String url,
			String type, String pi, String ps, String or, String mtype,
			String years, boolean isRefresh) {

		return jsonToGeneralVideo(context, url, type, pi, ps, or, mtype, years,
				null, isRefresh);
	}

	/**
	 * 
	 * @param context
	 * @param url
	 * @param type
	 * @param pi
	 * @param ps
	 * @param or
	 * @param mtype
	 * @param years
	 * @param areas
	 * @param isRefresh
	 * @return
	 */
	public static EVideo jsonToGeneralVideo(Context context, String url,
			String type, String pi, String ps, String or, String mtype,
			String years, String areas, boolean isRefresh) {
		EVideo eVideo = null;
		ObjectMapper mapper = new ObjectMapper();
		TysxOA oa = new TysxOA(context);
		List<BasicNameValuePair> vp = new ArrayList<BasicNameValuePair>();
		vp.add(new BasicNameValuePair("productid", "1000000089"));
		String jsons = oa.search("1", TianyiContent.token, "1", Integer.parseInt(pi), Integer.parseInt(ps), vp, "cover", TianyiContent.appid, TianyiContent.devid, TianyiContent.appSecret);
		try {
			ScreeningBean bean = mapper.readValue(jsons, ScreeningBean.class);
			System.out.println("电影名称："+bean.getInfo().getData()[0].getTitle()+"图片路径："+bean.getInfo().getData()[0].getCover()+"内容id:"+bean.getInfo().getData()[0].getContentId());
			eVideo = ScreeningVideo(bean,Integer.parseInt(pi),Integer.parseInt(ps));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("筛选数据解析失败");
		}
		
		/*String key = "vipvideo_" + type + "_" + pi + "_" + ps + "_" + or + "_"
				+ mtype + "_" + years + "_" + areas;
		boolean b = NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh);
		LogUtils.e(b + "--------------------------->");
		if (NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh)) {
			String json = RestClient.newInstance(context).doMethod(false, url,
					new BasicNameValuePair("type", type),
					new BasicNameValuePair("pi", pi),
					new BasicNameValuePair("ps", ps),
					new BasicNameValuePair("or", or),
					new BasicNameValuePair("mtype", mtype),
					new BasicNameValuePair("years", years),
					new BasicNameValuePair("areas", areas));
			LogUtils.i(json);
			eVideo = parseJsonVideo(context, key, json);

		} else {
			eVideo = (EVideo) CookieUtils.readObject(key);
		}*/

		return eVideo;
	}

	/*public static VipVideoDetail jsonToVipVideoDetail(Context context,
			String url, int id, boolean isRefresh) {
		VipVideoDetail videoDetail = null;
		String key = "vipVideoDetail_" + id;
		boolean b = NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh);
		LogUtils.e(b + "--------------------------->");
		if (NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh)) {
			String jsonnew = RestClient.newInstance(context).doMethod(false,
					url);
			String json = DES2.decrypt(jsonnew);
			LogUtils.e(json);
			videoDetail = parseJsonVipVideoDetail(context, key, json);
		} else {
			videoDetail = (VipVideoDetail) CookieUtils.readObject(key);
			if (videoDetail == null)
				videoDetail = new VipVideoDetail();
		}

		return videoDetail;
	}*/

	// vip视频详情
	public static VipVideoDetail parseJsonVipVideoDetail(Context context,
			String key, String json) {
		VipVideoDetail vipVideoDetail = null;
		LogUtils.e(json);
		if (!TextUtils.isEmpty(json)) {
			try {
				vipVideoDetail = new VipVideoDetail();
				JSONObject jsonObject = new JSONObject(json);
				vipVideoDetail
						.setMessage(jsonObject.has("message") ? jsonObject
								.getString("message") : "");
				String result = jsonObject.getString("res");
				if (TextUtils.isEmpty(result) || result.equals("{}")) {

				} else {
					JSONObject jsonResult = new JSONObject(result);
					vipVideoDetail
							.setResult(jsonResult.has("result") ? StringUtils
									.parseInt(jsonResult.getString("result"))
									: -1);
					vipVideoDetail.setDid(jsonResult.has("did") ? jsonResult
							.getString("did") : "");
					vipVideoDetail.setSid(jsonResult.has("sid") ? jsonResult
							.getString("sid") : "");
					vipVideoDetail.setUid(jsonResult.has("uid") ? jsonResult
							.getString("uid") : "");
				}
				String data = jsonObject.getString("data");
				if (data == null || data.equals("{}")) {
				} else {
					JSONObject contentObject = new JSONObject(data);
					vipVideoDetail.setId(contentObject.has("id") ? StringUtils
							.parseInt(contentObject.getString("id"), -1) : -1);
					vipVideoDetail
							.setTitle(contentObject.has("title") ? contentObject
									.getString("title") : "");
					vipVideoDetail
							.setImg(contentObject.has("img") ? contentObject
									.getString("img") : "");
					vipVideoDetail
							.setActor(contentObject.has("actor") ? contentObject
									.getString("actor") : "");
					vipVideoDetail
							.setDirector(contentObject.has("director") ? contentObject
									.getString("director") : "");
					vipVideoDetail
							.setStageurl(contentObject.has("stageurl") ? contentObject
									.getString("stageurl") : "");
					vipVideoDetail
							.setMtype(contentObject.has("mtype") ? contentObject
									.getString("mtype") : "");
					vipVideoDetail
							.setYears(contentObject.has("years") ? contentObject
									.getString("years") : "");
					vipVideoDetail.setDescription(contentObject
							.has("description") ? contentObject
							.getString("description") : "");
					vipVideoDetail
							.setDuration(contentObject.has("duration") ? StringUtils.parseInt(
									contentObject.getString("duration"), -1)
									: -1);
					vipVideoDetail
							.setScore(contentObject.has("score") ? StringUtils
									.parseFloat(
											contentObject.getString("score"),
											0.0f) : 0.0f);
					vipVideoDetail
							.setHdplayurl(contentObject.has("hdplayurl") ? contentObject
									.getString("hdplayurl") : "");
					vipVideoDetail
							.setPalyurl(contentObject.has("playurl") ? contentObject
									.getString("playurl") : "");
					vipVideoDetail
							.setOtherurl(contentObject.has("otherurl") ? contentObject
									.getString("otherurl") : "");
					vipVideoDetail
							.setType(contentObject.has("type") ? StringUtils
									.parseInt(contentObject.getString("type"),
											-1) : -1);
					vipVideoDetail
							.setUrl(contentObject.has("url") ? contentObject
									.getString("url") : "");
				}
				vipVideoDetail.setCacheKey(key);
				CookieUtils.saveObject(vipVideoDetail, key);

			} catch (Exception e) {
				vipVideoDetail = (VipVideoDetail) CookieUtils.readObject(key);
				if (vipVideoDetail == null) {
					LogUtils.e(e.getMessage());
				}
			}

		} else {
			vipVideoDetail = (VipVideoDetail) CookieUtils.readObject(key);
			if (vipVideoDetail == null)
				vipVideoDetail = new VipVideoDetail();
		}
		return vipVideoDetail;
	}

	public static VodVideoDetail jsonToVodVideoDetail(Context context,
			String url, int id, boolean isRefresh) {
		VodVideoDetail vodVideoDetail = null;
		String key = "vodVideoDetail_" + id;
		boolean b = NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh);
		LogUtils.e(b + "--------------------------->");
		if (NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh)) {
			
			ObjectMapper mapper = new ObjectMapper();
			TysxOA oa = new TysxOA(context);
			String json = oa.getPInfo(TianyiContent.token,id+"", "averageScoreValue,releasyear,length,director,cast,description,subcategoryName", null, TianyiContent.appid, TianyiContent.devid, TianyiContent.appSecret);
			String paths = oa.getVideoPlayInfo(TianyiContent.token, "1", null, id+"", null, "AKDY5011101", TianyiContent.appid, TianyiContent.devid, TianyiContent.appSecret);
			System.out.println("详情内容："+json);
			System.out.println("内容播放地址："+paths);
			try {
				DetailsBean bean = mapper.readValue(json, DetailsBean.class);
				ContentPath contentpath = mapper.readValue(paths, ContentPath.class);
				if(contentpath.getCode()==0){
					vodVideoDetail = parseJsonVodVideoDetails(context,key,bean,id,contentpath.getInfo().getVideos()[1].getPlayUrl());
				}else if(contentpath.getCode()==917){
					vodVideoDetail = parseJsonVodVideoDetails(context,key,bean,id,"917");
				}else if(contentpath.getCode()==136){
					vodVideoDetail = parseJsonVodVideoDetails(context,key,bean,id,"136");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*String json = RestClient.newInstance(context).doMethod(false, url);
			LogUtils.e(json);
			vodVideoDetail = parseJsonVodVideoDetail(context, key, json);*/
		} else {
			vodVideoDetail = (VodVideoDetail) CookieUtils.readObject(key);
			if (vodVideoDetail == null)
				vodVideoDetail = new VodVideoDetail();
		}

		return vodVideoDetail;
	}

	public static VodVideoDetail parseJsonVodVideoDetails(Context context,
			String key, DetailsBean json,int id,String path) {
		VodVideoDetail vodVideoDetail = null;
			try {
				vodVideoDetail = new VodVideoDetail();
				vodVideoDetail.setId(id);
				vodVideoDetail.setTitle(json.getInfo().getTitle());
				vodVideoDetail.setImg("");
					vodVideoDetail.setActor(json.getInfo().getCast());
					vodVideoDetail
							.setDirector(json.getInfo().getDirector());
					vodVideoDetail
							.setMtype(json.getInfo().getSubcategoryName());
					vodVideoDetail
							.setYears(json.getInfo().getReleasyear());
					vodVideoDetail
							.setStageurl("");
					vodVideoDetail.setDescription(json.getInfo().getDescription());
					vodVideoDetail
							.setDuration(json.getInfo().getLength());
					vodVideoDetail
							.setScore(json.getInfo().getAverageScoreValue());
					vodVideoDetail
							.setPlayurl(path);
					vodVideoDetail.setArea(json.getInfo().getCountryName());
					
				vodVideoDetail.setCacheKey(key);
				CookieUtils.saveObject(vodVideoDetail, key);

			} catch (Exception e) {
				vodVideoDetail = (VodVideoDetail) CookieUtils.readObject(key);
				if (vodVideoDetail == null) {
					LogUtils.e(e.getMessage());
				}
			}

		
		return vodVideoDetail;
	}
	
	
	public static VodVideoDetail parseJsonVodVideoDetail(Context context,
			String key, String json) {
		VodVideoDetail vodVideoDetail = null;
		LogUtils.e(json);
		if (!TextUtils.isEmpty(json)) {
			try {
				vodVideoDetail = new VodVideoDetail();
				JSONObject jsonObject = new JSONObject(json);
				vodVideoDetail
						.setMessage(jsonObject.has("message") ? jsonObject
								.getString("message") : "");
				String result = jsonObject.getString("res");
				if (TextUtils.isEmpty(result) || result.equals("{}")) {

				} else {
					JSONObject jsonResult = new JSONObject(result);
					vodVideoDetail
							.setResult(jsonResult.has("result") ? StringUtils
									.parseInt(jsonResult.getString("result"))
									: -1);
					vodVideoDetail.setDid(jsonResult.has("did") ? jsonResult
							.getString("did") : "");
					vodVideoDetail.setSid(jsonResult.has("sid") ? jsonResult
							.getString("sid") : "");
					vodVideoDetail.setUid(jsonResult.has("uid") ? jsonResult
							.getString("uid") : "");
				}
				String data = jsonObject.getString("data");
				if (data == null || data.equals("{}")) {
				} else {
					JSONObject contentObject = new JSONObject(data);
					vodVideoDetail.setId(contentObject.has("id") ? StringUtils
							.parseInt(contentObject.getString("id"), -1) : -1);
					vodVideoDetail
							.setTitle(contentObject.has("title") ? contentObject
									.getString("title") : "");
					vodVideoDetail
							.setImg(contentObject.has("img") ? contentObject
									.getString("img") : "");
					vodVideoDetail
							.setActor(contentObject.has("actor") ? contentObject
									.getString("actor") : "");
					vodVideoDetail
							.setDirector(contentObject.has("director") ? contentObject
									.getString("director") : "");
					vodVideoDetail
							.setMtype(contentObject.has("mtype") ? contentObject
									.getString("mtype") : "");
					vodVideoDetail
							.setYears(contentObject.has("years") ? contentObject
									.getString("years") : "");
					vodVideoDetail
							.setStageurl(contentObject.has("stageurl") ? contentObject
									.getString("stageurl") : "");
					vodVideoDetail.setDescription(contentObject
							.has("description") ? contentObject
							.getString("description") : "");
					vodVideoDetail
							.setDuration(contentObject.has("duration") ? StringUtils.parseInt(
									contentObject.getString("duration"), -1)
									: -1);
					vodVideoDetail
							.setScore(contentObject.has("score") ? StringUtils
									.parseFloat(
											contentObject.getString("score"),
											0.0f) : 0.0f);
					vodVideoDetail
							.setPlayurl(contentObject.has("playurl") ? contentObject
									.getString("playurl") : "");
				}
				vodVideoDetail.setCacheKey(key);
				CookieUtils.saveObject(vodVideoDetail, key);

			} catch (Exception e) {
				vodVideoDetail = (VodVideoDetail) CookieUtils.readObject(key);
				if (vodVideoDetail == null) {
					LogUtils.e(e.getMessage());
				}
			}

		} else {
			vodVideoDetail = (VodVideoDetail) CookieUtils.readObject(key);
			if (vodVideoDetail == null)
				vodVideoDetail = new VodVideoDetail();
		}
		return vodVideoDetail;
	}

	// public static VideoPlay jsonToVideoPlay(Context context, String url,
	// long id, boolean isRefresh) {
	// VideoPlay videoPlay = null;
	// String key = "videoplay_" + id;
	// boolean b = NetUtils.isConnect(context)
	// && (!CookieUtils.isReadDataCache(key) || isRefresh);
	// LogUtils.e(b + "--------------------------->");
	// if (NetUtils.isConnect(context)
	// && (!CookieUtils.isReadDataCache(key) || isRefresh)) {
	// String json = RestClient.newInstance(context).doMethod(false, url);
	// LogUtils.e(json);
	// videoPlay = parseJsonVideoPlay(context, key, json);
	//
	// } else {
	// videoPlay = (VideoPlay) CookieUtils.readObject(key);
	// if (videoPlay == null)
	// videoPlay = new VideoPlay();
	// }
	//
	// return videoPlay;
	// }

	// public static VideoPlay parseJsonVideoPlay(Context context, String key,
	// String json) {
	// VideoPlay videoPlay = null;
	// LogUtils.e(json);
	// if (!TextUtils.isEmpty(json)) {
	// try {
	// videoPlay = new VideoPlay();
	// JSONObject jsonObject = new JSONObject(json);
	// videoPlay.setMessage(jsonObject.has("message") ? jsonObject
	// .getString("message") : "");
	// String result = jsonObject.getString("res");
	// if (result == null || result.equals("{}")) {
	//
	// } else {
	// JSONObject jsonResult = new JSONObject(result);
	// videoPlay.setResult(jsonResult.has("result") ? StringUtils
	// .parseInt(jsonResult.getString("result")) : -1);
	// videoPlay.setDid(jsonResult.has("did") ? jsonResult
	// .getString("did") : "");
	// videoPlay.setSid(jsonResult.has("sid") ? jsonResult
	// .getString("sid") : "");
	// videoPlay.setUid(jsonResult.has("uid") ? jsonResult
	// .getString("uid") : "");
	// }
	// String data = jsonObject.getString("data");
	// if (data == null || data.equals("[]")) {
	// } else {
	// JSONArray jsonArray = new JSONArray(data);
	// JSONObject contentObject = jsonArray.getJSONObject(0);
	// videoPlay.setId(contentObject.has("id") ? StringUtils
	// .parseLong(contentObject.getString("id"), -1) : -1);
	// videoPlay
	// .setPlayurl(contentObject.has("playurl") ? contentObject
	// .getString("playurl") : "");
	// videoPlay
	// .setTitle(contentObject.has("title") ? contentObject
	// .getString("title") : "");
	// videoPlay
	// .setDuration(contentObject.has("duration") ? StringUtils.parseInt(
	// contentObject.getString("duration"), -1)
	// : -1);
	//
	// }
	// videoPlay.setCacheKey(key);
	// CookieUtils.saveObject(videoPlay, key);
	//
	// } catch (Exception e) {
	// videoPlay = (VideoPlay) CookieUtils.readObject(key);
	// if (videoPlay == null) {
	// LogUtils.e(e.getMessage());
	// }
	// }
	//
	// } else {
	// videoPlay = (VideoPlay) CookieUtils.readObject(key);
	// if (videoPlay == null)
	// videoPlay = new VideoPlay();
	// }
	// return videoPlay;
	// }
	public static EVideo ScreeningVideo(ScreeningBean bean,int pi,int ps){
		EVideo eVideo = null;
		try {
			if(bean!=null){
				eVideo = new EVideo();
				eVideo.setPi(pi);
				eVideo.setPs(ps);
				eVideo.setCount(bean.getInfo().getTotal());
				eVideo.setVideoList(ScreeningVideoList(bean));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eVideo;
	}
	
	public static EVideo parseJsonVideo(Context context, String key, String json) {
		EVideo eVideo = null;
		if (!TextUtils.isEmpty(json)) {
			try {
				eVideo = new EVideo();
				JSONObject jsonObject = new JSONObject(json);
				eVideo.setMessage(jsonObject.has("message") ? jsonObject
						.getString("message") : "");
				String result = jsonObject.getString("res");
				if (result == null || result.equals("{}")) {

				} else {
					JSONObject jsonResult = new JSONObject(result);
					eVideo.setResult(jsonResult.has("result") ? StringUtils
							.parseInt(jsonResult.getString("result")) : -1);
					eVideo.setDid(jsonResult.has("did") ? jsonResult
							.getString("did") : "");
					eVideo.setSid(jsonResult.has("sid") ? jsonResult
							.getString("sid") : "");
					eVideo.setUid(jsonResult.has("uid") ? jsonResult
							.getString("uid") : "");
				}
				String data = jsonObject.getString("data");
				if (data == null || data.equals("{}")) {
				} else {
					JSONObject contentObject = new JSONObject(data);
					eVideo.setPi(contentObject.has("pi") ? StringUtils
							.parseInt(contentObject.getString("pi"), -1) : -1);
					eVideo.setPs(contentObject.has("ps") ? StringUtils
							.parseInt(contentObject.getString("ps"), -1) : -1);
					eVideo.setCount(contentObject.has("count") ? StringUtils
							.parseInt(contentObject.getString("count"), -1)
							: -1);
					eVideo.setVideoList(parseJsonVideoList(data));
				}
				eVideo.setCacheKey(key);
				CookieUtils.saveObject(eVideo, key);
			} catch (Exception e) {
				LogUtils.e(e.getMessage());
			}
		} else {
			eVideo = (EVideo) CookieUtils.readObject(key);
		}
		return eVideo;
	}
	public static List<Video> ScreeningVideoList(ScreeningBean bean){
		List<Video> videoList = new ArrayList<Video>();
		ObjectMapper mapper = new ObjectMapper();
		ImageUrlBean imagebean;
		try {
			for(int i=0;i<bean.getInfo().getData().length;i++){
				Video video = new Video();
				video.setId(Integer.parseInt(bean.getInfo().getData()[i].getContentId()));
				video.setTitle(bean.getInfo().getData()[i].getTitle());
				ImageUrlBean ibean = mapper.readValue(bean.getInfo().getData()[i].getCover(), ImageUrlBean.class);
				video.setImg(ibean.getImage().getM7());
				video.setType(-1);
				video.setUrl("");
				video.setDescription("");
				videoList.add(video);
				System.out.println("添加次数"+i);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return videoList;
	}
	public static List<Video> parseJsonVideoList(String json) {
		List<Video> videoList = new ArrayList<Video>();
		try {
			JSONObject jsonobject = new JSONObject(json);
			Iterator iterator = jsonobject.keys();
			while (iterator.hasNext()) {
				Object s = iterator.next();
				if (s.toString().equals("count")) {
					continue;
				}
				if (s.toString().equals("pi")) {
					continue;
				}
				if (s.toString().equals("ps")) {
					continue;
				}
				JSONObject videoObject = jsonobject.getJSONObject(s.toString());
				Video video = new Video();
				video.setId(videoObject.has("id") ? StringUtils.parseInt(
						videoObject.getString("id"), -1) : -1);
				video.setTitle(videoObject.has("title") ? videoObject
						.getString("title") : "");
				video.setImg(videoObject.has("img") ? videoObject
						.getString("img") : "");
				video.setType(videoObject.has("type") ? StringUtils.parseInt(
						videoObject.getString("type"), -1) : -1);
				video.setUrl(videoObject.has("url") ? videoObject
						.getString("url") : "");
				video.setDescription(videoObject.has("description") ? videoObject
						.getString("description") : "");
				videoList.add(video);
			}
		} catch (Exception e) {
			LogUtils.e(e.getMessage());
		}
		return videoList;
	}

	public static EReleated jsonToVideoReleated(Context context, String url,
			int type, int id, boolean isRefresh) {
		EReleated eReleated = null;
		String key = "videoreleated_" + type + "_" + id;
		boolean b = NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh);
		LogUtils.e(b + "--------------------------->");
		TysxOA oa = new TysxOA(context);
		List<BasicNameValuePair> vp = new ArrayList<BasicNameValuePair>();
		vp.add(new BasicNameValuePair("categoryname", url));
		String json = oa.search("1", TianyiContent.token, "1", 1, 6, vp, "cover", TianyiContent.appid, TianyiContent.devid, TianyiContent.appSecret);
		System.out.println("相关反馈数据位："+json);
		ObjectMapper mapper = new ObjectMapper();
		try {
			ScreeningBean bean = mapper.readValue(json, ScreeningBean.class);
			eReleated = parsejsonvideo(context,bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*if (NetUtils.isConnect(context)
				&& (!CookieUtils.isReadDataCache(key) || isRefresh)) {
			String json = RestClient.newInstance(context).doMethod(false, url);
			LogUtils.e(json);
			eReleated = parseJsonVideoReleated(context, key, json);
		} else {
			eReleated = (EReleated) CookieUtils.readObject(key);
			if (eReleated == null)
				eReleated = new EReleated();
		}*/

		return eReleated;
	}
	public static EReleated parsejsonvideo(Context context,ScreeningBean json){
		List<Releated> releatedList = new ArrayList<Releated>();
		ObjectMapper mapper = new ObjectMapper();
		EReleated eReleated = null;
		eReleated = new EReleated();
		eReleated.setMessage("");
		eReleated.setResult(-1);
		eReleated.setDid("");
		eReleated.setSid("");
		eReleated.setUid("");
		if(json.getCode()==522){
			
		}else{
			for(int i=0;i<json.getInfo().getData().length;i++){
				Releated releated = new Releated();
				releated.setId(Integer.parseInt(json.getInfo().getData()[i].getContentId()));
				releated.setType(-1);
				releated.setTitle(json.getInfo().getData()[i].getTitle());
				ImageUrlBean ibean;
				try {
					ibean = mapper.readValue(json.getInfo().getData()[i].getCover(), ImageUrlBean.class);
					releated.setImg(ibean.getImage().getM7());
				} catch (Exception e) {
					e.printStackTrace();
				}
				releated.setUrl("");
				releated.setDirector("");
				releated.setStarring("");
				releated.setAreas("");
				releated.setMtype("");
				releated.setDescription("");
				releatedList.add(releated);
				System.out.println("添加相关推荐内容："+json.getInfo().getData()[i].getTitle());
			}
			eReleated.setReleatedList(releatedList);
		}
		return eReleated;
	}
	public static List<Releated> parseJsonVideoReleatedList(String json) {
		List<Releated> releatedList = new ArrayList<Releated>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int index = 0; index < jsonArray.length(); index++) {
				JSONObject jsonObject = jsonArray.getJSONObject(index);
				Releated releated = new Releated();
				releated.setId(jsonObject.has("id") ? StringUtils.parseInt(
						jsonObject.getString("id"), -1) : -1);
				releated.setType(jsonObject.has("type") ? StringUtils.parseInt(
						jsonObject.getString("type"), -1) : -1);
				releated.setTitle(jsonObject.has("title") ? jsonObject
						.getString("title") : "");
				releated.setImg(jsonObject.has("img") ? jsonObject
						.getString("img") : "");
				releated.setUrl(jsonObject.has("url") ? jsonObject
						.getString("url") : "");
				releated.setDirector(jsonObject.has("director") ? jsonObject
						.getString("director") : "");
				releated.setStarring(jsonObject.has("starring") ? jsonObject
						.getString("starring") : "");
				releated.setAreas(jsonObject.has("clime") ? jsonObject
						.getString("clime") : "");
				releated.setMtype(jsonObject.has("mtype") ? jsonObject
						.getString("mtype") : "");
				releated.setDescription(jsonObject.has("description") ? jsonObject
						.getString("description") : "");
				releatedList.add(releated);
			}
		} catch (Exception e) {
			LogUtils.e(e.getMessage());
		}
		return releatedList;
	}
	
	public static EReleated parseJsonVideoReleated(Context context, String key,
			String json) {
		EReleated eReleated = null;
		LogUtils.e(json);
		if (!TextUtils.isEmpty(json)) {
			try {
				eReleated = new EReleated();
				JSONObject jsonObject = new JSONObject(json);
				eReleated.setMessage(jsonObject.has("message") ? jsonObject
						.getString("message") : "");
				String result = jsonObject.getString("res");
				if (result == null || result.equals("{}")) {

				} else {
					JSONObject jsonResult = new JSONObject(result);
					eReleated.setResult(jsonResult.has("result") ? StringUtils
							.parseInt(jsonResult.getString("result")) : -1);
					eReleated.setDid(jsonResult.has("did") ? jsonResult
							.getString("did") : "");
					eReleated.setSid(jsonResult.has("sid") ? jsonResult
							.getString("sid") : "");
					eReleated.setUid(jsonResult.has("uid") ? jsonResult
							.getString("uid") : "");
				}
				String data = jsonObject.getString("data");
				if (TextUtils.isEmpty(data) || data.equals("[]")) {
					eReleated.setReleatedList(null);
				} else {
					eReleated.setReleatedList(parseJsonVideoReleatedList(data));
				}
				eReleated.setCacheKey(key);
				CookieUtils.saveObject(eReleated, key);

			} catch (Exception e) {
				eReleated = (EReleated) CookieUtils.readObject(key);
				if (eReleated == null) {
					LogUtils.e(e.getMessage());
				}
			}

		} else {
			eReleated = (EReleated) CookieUtils.readObject(key);
			if (eReleated == null)
				eReleated = new EReleated();
		}
		return eReleated;
	}

	

}
