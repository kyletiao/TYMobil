package com.m1905.mobile.service;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.m1905.mobile.bean.OneLoginBean;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.content.LoginContent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String ss = intent.getAction();
		System.out.println("广播反馈数据:"+intent.getAction()+"appid:"+intent.getStringExtra("appid")+"result:"+intent.getStringExtra("result"));
		ObjectMapper mapper = new ObjectMapper();
		try {
			OneLoginBean json = mapper.readValue(intent.getStringExtra("result"), OneLoginBean.class);
			if(json.getMsg().equals("OK")){
				TianyiContent.user = json.getInfo().getNickName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
