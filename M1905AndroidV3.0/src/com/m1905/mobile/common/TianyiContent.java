package com.m1905.mobile.common;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.m1905.mobile.bean.InitBean;
import com.telecomsdk.phpso.TysxOA;

import android.content.Context;

public class TianyiContent {
	public final static String NavigationUrl = "http://180.168.69.121:8089/webroot/clt4/kpcp/szyx/gzz/clt5vb/sywhq/sy/index.json";
	public final static String TianyiUrl = "http://180.168.69.121:8089/webroot/";
	public final static String devid = "000001";
	public final static String appid = "10260036000";
	public final static String appSecret = "540D8B04BF2643CABDAED333729566FD";
	public final static String sdkUrl = "https://api.tv189.com";
	public final static String channelId = "01061921";
	public static String token = "";
	public static String user = "";
	
	public static String DEFAULT_PARTNER = "2088701346765380";
	public static String DEFAULT_SELLER = "tysx10000@189.cn";
	public TianyiContent(){
		token = "";
	}
	public static void tokenInit(final Context context){
		
		new Thread() {
			@Override 
			public void run() {
				super.run();
				TysxOA oa = new TysxOA(context);
				ObjectMapper mapper = new ObjectMapper();
				String init = oa.loading(context, devid, appid,
						appSecret, sdkUrl, channelId);
				try {
					System.out.println("初始化：" + init);
					InitBean bean = mapper.readValue(init, InitBean.class);
					TianyiContent.token = bean.getToken();
					if(bean.getNickName()!=null){
						TianyiContent.user = bean.getNickName();
					}
					System.out.println("token:"+bean.getToken()+"---user:"+bean.getNickName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
