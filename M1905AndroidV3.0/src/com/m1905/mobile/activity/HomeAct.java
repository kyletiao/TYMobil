package com.m1905.mobile.activity;

import java.util.zip.Inflater;

import android.app.TabActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.dianxin.mobilefree.R;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.service.ConnectionChangeReceiver;
import com.m1905.mobile.window.SignOut;
import com.umeng.analytics.MobclickAgent;

public class HomeAct extends TabActivity implements OnCheckedChangeListener {
	public static TabHost tabHost;
	private RadioGroup rgpHomeTabs;
	private final static String TAB_TAG_HOME_RECOMMENT = "TAB_TAG_HOME_RECOMMENT";
	private final static String TAB_TAG_HOME_MOVIE = "TAB_TAG_HOME_MOVIE";
	private final static String TAB_TAG_HOME_SEARCH = "TAB_TAG_HOME_SEARCH";
	private final static String TAB_TAG_HOME_NEARBY = "TAB_TAG_HOME_NEARBY";
	private final static String TAB_TAG_HOME_MINE = "TAB_TAG_HOME_MINE";

    private final static String API_KEY = "UOdfT5jECHliyBkvbQ8Cy7s4";

	private boolean isExit = false;
	private View parent = null;
	private final static int MSG_CLOSE_EXIT = 0;
	private final static int MSG_SHOW_EXIT = 1;
	private final static long MSG_TIME = 2000;
	private PopupWindow popupExit = null;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) { 
			switch (msg.what) { 
			case MSG_SHOW_EXIT:
				showExitMsg(MSG_TIME);
				break;
			case MSG_CLOSE_EXIT:
				closeExitMsg();
				break;
			}
		}

	};
	private ConnectionChangeReceiver myReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.updateOnlineConfig(HomeAct.this); 
		setContentView(R.layout.act_home);
		registerReceiver();
		rgpHomeTabs = (RadioGroup) findViewById(R.id.rgpHomeTabs);
		rgpHomeTabs.setOnCheckedChangeListener(this);
		parent = findViewById(R.id.rltHomeAct);
		isExit = false;
		initView();

        // 初始化百度云推送
        PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, API_KEY);
	}
	
	private  void registerReceiver(){
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);
    }
	private  void unregisterReceiver(){
        this.unregisterReceiver(myReceiver);
    }
	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/**
	 * 初始化提示信息
	 */
	private void initExitMsg() {
		if (popupExit == null) {
			View contentView = LayoutInflater.from(HomeAct.this).inflate(
					R.layout.popup_text_msg, null);
			TextView tvwMsg = (TextView) contentView.findViewById(R.id.tvwMsg);
			tvwMsg.setText("再按一次离开");
			popupExit = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
	}

	/**
	 * 显示退出提示
	 * 
	 * @param delayMillis
	 *            提示多长时间
	 */
	private void showExitMsg(long delayMillis) {
		isExit = true;
		initExitMsg();
		if (!popupExit.isShowing()) {
			popupExit.showAtLocation(parent, Gravity.BOTTOM, 0, 250);
			mHandler.sendEmptyMessageDelayed(MSG_CLOSE_EXIT, MSG_TIME);
		}
	}

	/**
	 * 关闭退出提示
	 */
	private void closeExitMsg() {
		isExit = false;
		if (popupExit.isShowing()) {
			popupExit.dismiss();
			
		}
		if (mHandler.hasMessages(MSG_CLOSE_EXIT)) {
			unregisterReceiver();
			mHandler.removeMessages(MSG_CLOSE_EXIT);
		}

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				LayoutInflater inflater = LayoutInflater.from(HomeAct.this);
				SignOut win_sign = new SignOut(HomeAct.this, inflater);
				win_sign.ShowDialog(parent, HomeAct.this);
				/*if (isExit) {
					mHandler.sendEmptyMessage(MSG_CLOSE_EXIT);
					if(Identify.currentUser!=null)
					{
						Identify.currentUser = null;
					}
					HomeAct.this.finish();
					return true;
				} else {
					mHandler.sendEmptyMessage(MSG_SHOW_EXIT);
					return false;
				}*/
				return true;
			}
		}
		return super.dispatchKeyEvent(event);  
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		tabHost = getTabHost();
		tabHost.addTab(buildTabSpec(TAB_TAG_HOME_RECOMMENT,
				R.string.home_recommendation,
				R.drawable.tab_recomment_logo_selector, new Intent(this,
						RecommentAct.class))); 
		tabHost.addTab(buildTabSpec(TAB_TAG_HOME_MOVIE, R.string.home_movie,
				R.drawable.tab_movie_logo_selector, new Intent(this,
						MovieAct.class)));
		tabHost.addTab(buildTabSpec(TAB_TAG_HOME_SEARCH, R.string.home_search,
				R.drawable.tab_search_logo_selector, new Intent(this,
						SearchAct.class)));
		tabHost.addTab(buildTabSpec(TAB_TAG_HOME_NEARBY, R.string.home_nearby,
				R.drawable.tab_nearby_logo_selector, new Intent(this,
						NearbyAct.class))); 
		tabHost.addTab(buildTabSpec(TAB_TAG_HOME_MINE, R.string.home_mine,
				R.drawable.tab_mine_logo_selector, new Intent(this,
						MineAct.class)));   
	}

	/**
	 * 构建TabHost的Tab页
	 * 
	 * @param tag
	 *            标记
	 * @param resLabel
	 *            标签
	 * @param resIcon
	 *            图标
	 * @param content
	 *            该tab展示的内容
	 * @return 一个tab
	 */
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return tabHost
				.newTabSpec(tag)
				.setIndicator(getString(resLabel),
						getResources().getDrawable(resIcon))
				.setContent(content);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radHomeRecomment:
			tabHost.setCurrentTabByTag(TAB_TAG_HOME_RECOMMENT);
			break;
		case R.id.radHomeMovie:
			tabHost.setCurrentTabByTag(TAB_TAG_HOME_MOVIE);
			break;
		case R.id.radHomeSearch:
			tabHost.setCurrentTabByTag(TAB_TAG_HOME_SEARCH);
			break;
		case R.id.radHomeNearby:
			tabHost.setCurrentTabByTag(TAB_TAG_HOME_NEARBY);
			break;
		case R.id.radHomeMine:
			tabHost.setCurrentTabByTag(TAB_TAG_HOME_MINE);
			break;
		}
	}

}
