package com.m1905.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.bean.KjImageBean;
import com.m1905.mobile.bean.LoginBean;
import com.m1905.mobile.bean.MsgBean;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.util.NetHttpConnection;
import com.telecomsdk.phpso.TysxOA;
import com.telecomsdk.phpso.TysxOA2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends Activity{
	private Button bt_test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_test);
		bt_test = (Button) findViewById(R.id.bt_test);
		bt_test.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//init();
				getImagepath();
			}
		});
		tv_content_test = (TextView) findViewById(R.id.tv_content_test);
	}
	private void getImagepath(){
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					String path = NetHttpConnection.httpGet("http://180.168.69.121:8089/webroot/clt4/kpcp/szyx/gzz/clt5vb/kjhb/index.json", null);
					path = path.replaceAll("\n", "");
					path = path.replaceAll("\t", "");
					path = path.replaceAll(" ", "");
					path = path.substring(9, path.length());
					path = path.substring(0, path.length()-2);
					ObjectMapper mapper = new ObjectMapper();
					KjImageBean bean = mapper.readValue(path, KjImageBean.class);
					
					System.out.println("开机海报："+path);
					System.out.println("开机海报图："+bean.getInfo().getImagelist().getSrclist()[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		
	}
	private void init(){
		new Thread(){
			@Override
			public void run() {
				super.run();
				TysxOA2 oas = new TysxOA2();
				List mList = new ArrayList (); 
				mList.add(new BasicNameValuePair("accountNo", "1313131313@sina.com")); 
				mList.add(new BasicNameValuePair("password", "111111")); 
				mList.add(new BasicNameValuePair("channelID", TianyiContent.channelId)); 
				mList.add(new BasicNameValuePair("clienttype", "3")); 
				String url = oas.getUrl("https://api.tv189.com/v2/Live", "user", "userRegister", TianyiContent.devid, TianyiContent.appid, TianyiContent.appSecret, mList);
				try {
					String zcontent = NetHttpConnection.httpGet(url, null);
					System.out.println("注册反馈信息" + zcontent);
					if (zcontent.contains("820")) {
						myhandler.sendEmptyMessage(2);
					} else if (zcontent.contains("0")) {
						myhandler.sendEmptyMessage(1);
					} else {
						myhandler.sendEmptyMessage(2);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();	
	}
	private void init2(){
		new Thread(){
			@Override
			public void run() {
				super.run();
				TysxOA2 oas = new TysxOA2();
				List mList = new ArrayList (); 
				mList.add(new BasicNameValuePair("accountNo", "1212121212@sina.com")); 
				mList.add(new BasicNameValuePair("password", "111111")); 
				mList.add(new BasicNameValuePair("channelID", TianyiContent.channelId)); 
				mList.add(new BasicNameValuePair("clienttype", "3")); 
				String url = oas.getUrl("https://api.tv189.com/v2/Live", "user", "userRegister", TianyiContent.devid, TianyiContent.appid, TianyiContent.appSecret, mList);
				try {
					String zcontent = NetHttpConnection.httpGet(url, null);
					System.out.println("注册反馈信息" + zcontent);
					if (zcontent.contains("820")) {
						myhandler.sendEmptyMessage(2);
					} else if (zcontent.contains("0")) {
						myhandler.sendEmptyMessage(8);
					} else {
						myhandler.sendEmptyMessage(2);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();	
	}
	Handler myhandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Login();
				break;
			case 2:
				tv_content_test.setText("注册失败");
				break;
			case 3:
				tv_content_test.setText("登陆失败");
				break;
			case 4:
				TysxOA2 oa = new TysxOA2();
				List mlist = new ArrayList (); 
				mlist.add(new BasicNameValuePair("clienttype", "3")); 
				mlist.add(new BasicNameValuePair("token", TianyiContent.token)); 
				mlist.add(new BasicNameValuePair("productid", "1000000054")); 
				mlist.add(new BasicNameValuePair("carPass","b636fba5"));
				mlist.add(new BasicNameValuePair("carNum","SZ018450680"));

				final String url = oa.getUrl("https://api.tv189.com/v2/Internet", "user", "carActive", TianyiContent.devid, TianyiContent.appid, TianyiContent.appSecret, mlist);
				new Thread(){

					@Override
					public void run() {
						super.run();
						try {
							ObjectMapper mapper = new ObjectMapper();
							String kcontent = NetHttpConnection.httpGet(url, null);
							System.out.println("激活反馈内容："+kcontent);
							//bean = mapper.readValue(kcontent, MsgBean.class);
							/*if(bean.getCode()==0){
								myhandler.sendEmptyMessage(5);
							}else{
								myhandler.sendEmptyMessage(6);
							}*/
							if(kcontent.contains("OK")){
								myhandler.sendEmptyMessage(5);
							}else{
								myhandler.sendEmptyMessage(6);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
				break;
			case 5:
				tc();
				
				break;
			case 6:
				tv_content_test.setText("激活失败：");
				break;
			case 7:
				init2();
				break;
			case 8:
				Login2();
				break;
			case 9:
				TysxOA2 oas = new TysxOA2();
				List mlists = new ArrayList (); 
				mlists.add(new BasicNameValuePair("clienttype", "3")); 
				mlists.add(new BasicNameValuePair("token", TianyiContent.token)); 
				mlists.add(new BasicNameValuePair("productid", "1000000054")); 
				mlists.add(new BasicNameValuePair("carPass","c268dd2f"));
				mlists.add(new BasicNameValuePair("carNum","SZ018450355"));

				final String urls = oas.getUrl("https://api.tv189.com/v2/Internet", "user", "carActive", TianyiContent.devid, TianyiContent.appid, TianyiContent.appSecret, mlists);
				new Thread(){

					@Override
					public void run() {
						super.run();
						try {
							ObjectMapper mapper = new ObjectMapper();
							String kcontent = NetHttpConnection.httpGet(urls, null);
							System.out.println("激活反馈内容："+kcontent);
							//bean = mapper.readValue(kcontent, MsgBean.class);
							/*if(bean.getCode()==0){
								myhandler.sendEmptyMessage(5);
							}else{
								myhandler.sendEmptyMessage(6);
							}*/
							if(kcontent.contains("OK")){
								myhandler.sendEmptyMessage(10);
							}else{
								myhandler.sendEmptyMessage(6);
							}
							/*bean = mapper.readValue(kcontent, MsgBean.class);
							if(bean.getCode()==0){
								myhandler.sendEmptyMessage(10);
							}else{
								myhandler.sendEmptyMessage(6);
							}*/
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
				break;
			case 10:
				tv_content_test.setText("激活成功");
				break;
			default:
				break;
			}
		}
	};
	private TextView tv_content_test;
	private void tc(){
		//System.out.println("激活成功："+bean.getMsg());
		new Thread(){
			@Override
			public void run() {
				super.run();
				TysxOA oa = new TysxOA(TestActivity.this);
				String logout = oa.logout(TianyiContent.token,
						TianyiContent.devid, TianyiContent.appid,
						TianyiContent.appSecret);
				if (logout.contains("OK")) {
					myhandler.sendEmptyMessage(7);
				}
			}
		}.start();
	}
	private void Login(){
		new Thread(){
			@Override
			public void run() { 
				super.run();
				TysxOA oa = new TysxOA(TestActivity.this);
				ObjectMapper mapper = new ObjectMapper();
				String logincontent = oa.login(TianyiContent.token, TianyiContent.devid, TianyiContent.appid, TianyiContent.appSecret, "1313131313@sina.com","111111");
				
				System.out.println("登陆信息："+logincontent);
				//if (user != null&&user.getResult()==0) {
				try {
					LoginBean bean = mapper.readValue(logincontent, LoginBean.class);
					if (bean.getCode()==0) {
						myhandler.sendEmptyMessage(4);
					}else{
						myhandler.sendEmptyMessage(3);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	private void Login2(){
		new Thread(){
			@Override
			public void run() {
				super.run();
				TysxOA oa = new TysxOA(TestActivity.this);
				ObjectMapper mapper = new ObjectMapper();
				String logincontent = oa.login(TianyiContent.token, TianyiContent.devid, TianyiContent.appid, TianyiContent.appSecret, "1212121212@sina.com","111111");
				
				System.out.println("登陆信息："+logincontent);
				//if (user != null&&user.getResult()==0) {
				try {
					LoginBean bean = mapper.readValue(logincontent, LoginBean.class);
					if (bean.getCode()==0) {
						myhandler.sendEmptyMessage(9);
					}else{
						myhandler.sendEmptyMessage(3);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
