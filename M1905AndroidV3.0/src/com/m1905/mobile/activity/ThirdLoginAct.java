package com.m1905.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.common.Constant;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.dao.User;
import com.m1905.mobile.net.VipService;
import com.m1905.mobile.util.StringUtils;

public class ThirdLoginAct extends Activity implements OnClickListener {
	private final int WHAT_SHOW_LOADING_BAR = 0;
	private final int WHAT_HIDE_LOADING_BAR = -1;
	private ProgressBar pbrLoading = null;
	private WebView wvwThirdLogin;
	private String strUrl = "";
	private User user;
	Handler mh = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == WHAT_SHOW_LOADING_BAR) {
				// 显示加载图标
				if (pbrLoading.getVisibility() != View.VISIBLE)
					pbrLoading.setVisibility(View.VISIBLE);
			} else if (msg.what == WHAT_HIDE_LOADING_BAR) {
				// 隐藏加载图标
				if (pbrLoading.getVisibility() != View.GONE)
					pbrLoading.setVisibility(View.GONE);
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vip_third_login);
		findViewById(R.id.btnBack).setOnClickListener(this);
		pbrLoading = (ProgressBar) findViewById(R.id.pbrLoading);
		wvwThirdLogin = (WebView) findViewById(R.id.wvwThirdLogin);
		Intent it = getIntent();
		int urlKey = -1;
		if (!it.getExtras().containsKey("urlKey"))
			finish();
		urlKey = it.getExtras().getInt("urlKey");
		wvwThirdLogin.getSettings().setJavaScriptEnabled(true);
		wvwThirdLogin.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wvwThirdLogin.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				mh.sendEmptyMessage(WHAT_HIDE_LOADING_BAR);
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				if (Identify.currentUser == null) {
					view.loadUrl(url);
				} else {
					StringUtils.showLongToast(ThirdLoginAct.this, "您已登录");
				}

				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (url.equals(strUrl)) {
					mh.sendEmptyMessage(WHAT_SHOW_LOADING_BAR);
				}
				Log.e("msg", "url=" + url);
				if (url.contains("otherLogin?&mauth")) {
					// 获得第三方登录结果
					String newUrl = url.substring(url.indexOf("M"),
							url.length());
					// 清除cookie
					removeCookie();
					//new GetVIPDataTask().execute(newUrl);
					wvwThirdLogin.setVisibility(View.GONE);
				} else {
					super.onPageStarted(view, url, favicon);
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
		});
		wvwThirdLogin.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress < 100) {
					mh.sendEmptyMessage(WHAT_SHOW_LOADING_BAR);
				}
				if (newProgress == 100) {
					mh.sendEmptyMessage(WHAT_HIDE_LOADING_BAR);
				}
				Log.e("msg", "loading=" + newProgress + "--url" + view.getUrl());
				super.onProgressChanged(view, newProgress);
			}
		});
		switch (urlKey) {
		case 0:
			strUrl = Constant.SERVER_URL_SINA;
			break;
		case 1:
			strUrl = Constant.SERVER_URL_QQ;
			break;
		}
		wvwThirdLogin.loadUrl(strUrl);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;

		default:
			break;
		}
	}

	/*class GetVIPDataTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			user = VipService.jsonToThirdUser(ThirdLoginAct.this, "/"+params[0]);
			if (user != null&&user.getResult() == 0) {
				return 2;
			}else
			{
				return 1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case 2:
				Identify.currentUser = user;
				finish();
				break;
			case 1:
				StringUtils.showShortToast(ThirdLoginAct.this, user.getMessage());
				break;

			}
			super.onPostExecute(result);
		}
	}*/

	/**
	 * 清除cookie
	 */
	private void removeCookie() {
		CookieManager cm = CookieManager.getInstance();
		cm.removeExpiredCookie();
		cm.removeAllCookie();
		cm.removeSessionCookie();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}
}
