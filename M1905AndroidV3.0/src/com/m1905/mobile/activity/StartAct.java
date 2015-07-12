package com.m1905.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.adapter.ViewPagerAdapter;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.dao.Update;
import com.m1905.mobile.net.InitService;
import com.m1905.mobile.util.AppUtils;
import com.m1905.mobile.util.DeviceUtils;
import com.m1905.mobile.util.SDUtils;

public class StartAct extends Activity implements OnPageChangeListener{

	private ProgressBar pbrLaoding;
	private Update update = null;
	private List<View> views;
	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences home = getSharedPreferences("home", 0);
		int ishome = home.getInt("ishome", 0);
		if(ishome == 0){
			setContentView(R.layout.view_pager);
			LayoutInflater inflater = LayoutInflater.from(this);
			views = new ArrayList<View>();
			views.add(inflater.inflate(R.layout.what_new_one, null));
			views.add(inflater.inflate(R.layout.what_new_two, null));
			views.add(inflater.inflate(R.layout.what_new_three, null));
			vp = (ViewPager) findViewById(R.id.viewpager);
			vpAdapter = new ViewPagerAdapter(views ,handler);
			vp.setAdapter(vpAdapter);
			vp.setOnPageChangeListener(this);
		}else{
			setContentView(R.layout.act_start);
			update = null;
			pbrLaoding = (ProgressBar) findViewById(R.id.pbrLoading);
			TianyiContent.tokenInit(StartAct.this);
			new AsyncLoader().execute();
		}
	}
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				setContentView(R.layout.act_start);
				SharedPreferences homes = getSharedPreferences("home", 0);
				SharedPreferences.Editor editors = homes.edit();
				editors.putInt("ishome", 1);
				editors.commit();
				update = null;
				pbrLaoding = (ProgressBar) findViewById(R.id.pbrLoading);
				TianyiContent.tokenInit(StartAct.this);
				new AsyncLoader().execute();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	class AsyncLoader extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			
			// 初始化文件目录
			SDUtils.initAppDirectory();
			publishProgress(5);
			// 检测版本信息 (本地检测)
			AppUtils.checkVersion(getApplicationContext());
			publishProgress(15);
			// 初始化设备认证信息
			Identify.device = DeviceUtils.getDevice(StartAct.this);
			publishProgress(25);
			// 应用程序启动认证
			//InitService.initParams(StartAct.this);
			publishProgress(35);
			// 应用程序检测更新(网络检测)
			update = InitService.newCheckVersion(StartAct.this);
//			if (update != null)
//				update.setVersionpicx("http://b.hiphotos.baidu.com/image/w%3D2048/sign=9e6c2874af345982c58ae29238cc30ad/f2deb48f8c5494eef9aeb3192ff5e0fe99257e25.jpg");
			publishProgress(45);
			// 删除过期缓存
			SDUtils.deleteExpiredCacheFile();
			publishProgress(55);
			// 更新菜单数据
			publishProgress(75);
			// 更新地理位置数据
			publishProgress(90);
			// 下载启动图
			if (update != null && !TextUtils.isEmpty(update.getVersionpicx())) {
				loadSplash(update.getVersionpicx());
			}
			publishProgress(100);
			return 100;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int progress = values[0].intValue();
			pbrLaoding.setProgress(progress);
			if (progress == pbrLaoding.getMax()) {
				Intent intent = null;
				if (update != null
						&& !TextUtils.isEmpty(update.getVersionpicx())) {
					// 跳转到闪图
					intent = new Intent(StartAct.this, SplashAct.class);
					intent.putExtra("splashUrl", update.getVersionpicx());
				} else {
					// 跳转到推荐
					intent = new Intent(StartAct.this, HomeAct.class);
				}
				startActivity(intent);
				finish();
			}
		}
	}

	/**
	 * 判断、下载闪图
	 * 
	 * @param splashUrl
	 */
	private void loadSplash(String splashUrl) {
		if (!SDUtils.findSplash(splashUrl)) {
			InitService.downloadSplash(this, splashUrl);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		setCurDot(arg0);
	}
	private void setCurDot(int positon){
		if(positon < 0 || positon > views.size() - 1 || currentIndex == positon){
			return;
		}
		currentIndex = positon;
	}
	private int currentIndex;

}
