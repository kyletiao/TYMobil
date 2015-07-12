package com.m1905.mobile.activity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.util.EncryptUtils;

/**
 * 推送图页面
 * 
 * @author forcetech
 * 
 */
public class SplashAct extends Activity {

	ImageView imageView;
	private long delay = 2000l;
	private Intent intent;
	private String splashUrl = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		if (!intent.getExtras().containsKey("splashUrl")) {
			goHome();
			return;
		} else {
			splashUrl = intent.getStringExtra("splashUrl");
			if (TextUtils.isEmpty(splashUrl)) {
				goHome();
				return;
			}
		}
		Drawable mBitmap = BitmapDrawable
				.createFromPath(AppConfig.M1905_CACHE_PATH
						+ EncryptUtils.MD5(splashUrl));
		if (mBitmap == null) {
			// 文件损坏，或者下载不完整
			File file = new File(AppConfig.M1905_CACHE_PATH,
					EncryptUtils.MD5(splashUrl));
			if (file.exists()) {
				file.delete();
			}
			goHome();
			return;
		}
		imageView = new ImageView(this);
		imageView.setBackgroundDrawable(mBitmap);
		setContentView(imageView);
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				goHome();
			}
		}, delay);

	}

	private void goHome() {
		startActivity(new Intent(SplashAct.this, HomeAct.class));
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}