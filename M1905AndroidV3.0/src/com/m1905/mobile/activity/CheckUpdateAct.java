package com.m1905.mobile.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.dao.Update;
import com.m1905.mobile.net.InitService;
import com.m1905.mobile.util.StringUtils;

/**
 * 
 * @author leepan
 * @since 2013-12-25
 */
public class CheckUpdateAct extends Activity {
	private Update update;
	private RelativeLayout ileNetError;
	private RelativeLayout ileLoading;
	private ScrollView svwUpdate;
	private ImageView ivwUpdateLogo;
	private TextView tvwAppName, tvwUpdateTime, tvwUpdateInfo, tvwUpdateState;
	private LinearLayout btnUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_check_update);
		init();
		loadData();
	}

	private void init() {
		// 导航
		findViewById(R.id.btnFunc).setVisibility(View.GONE);
		((TextView) findViewById(R.id.tvwNaviNotice))
				.setText(R.string.func_update);
		ileNetError = (RelativeLayout) findViewById(R.id.ileNetError);
		ileNetError.findViewById(R.id.btnRefresh).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						loadData();
					}
				});
		tvwUpdateState = (TextView) findViewById(R.id.tvwUpdateState);
		ileLoading = (RelativeLayout) findViewById(R.id.ileLoading);
		((TextView) ileLoading.findViewById(R.id.tvwLoadingDesc))
				.setText(R.string.updateState_ing);
		svwUpdate = (ScrollView) findViewById(R.id.svwUpdate);
		ivwUpdateLogo = (ImageView) findViewById(R.id.ivwUpdateLogo);
		btnUpdate = (LinearLayout) findViewById(R.id.btnUpdate);
		btnUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		tvwAppName = (TextView) findViewById(R.id.tvwAppName);
		tvwUpdateTime = (TextView) findViewById(R.id.tvwUpdateTime);
		tvwUpdateInfo = (TextView) findViewById(R.id.tvwUpdateInfo);
	}

	/**
	 * 返回按钮操作
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	private void loadData() {
		svwUpdate.setVisibility(View.GONE);
		ileNetError.setVisibility(View.GONE);
		ileLoading.setVisibility(View.VISIBLE);
		new AsyncLoader().execute();
	}

	class AsyncLoader extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			//update = InitService.checkVersion(CheckUpdateAct.this);
			return 100;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (ileLoading.getVisibility() != View.GONE)
				ileLoading.setVisibility(View.GONE);
			if (update == null) {
				svwUpdate.setVisibility(View.GONE);
				ileNetError.setVisibility(View.VISIBLE);
			} else {
				ileNetError.setVisibility(View.GONE);
				switch (update.getNeedUpdate()) {
				case Update.UPDATE_STATE_NO:
					// 没有更新
					svwUpdate.setVisibility(View.GONE);
					tvwUpdateState.setVisibility(View.VISIBLE);
					break;
				case Update.UPDATE_STATE_PART:
					// 部分更新
				case Update.UPDATE_STATE_ALL:
					// 完整更新
					String appName = getString(R.string.app_name) + " "
							+ update.getVersionCode();
					String updateTime = getString(R.string.updateTimeDesc)
							+ " "
							+ StringUtils.formatTimeToDateTime(update
									.getVersionMini());
					String updateInfo = update.getInfo();
					tvwAppName.setText(appName);
					tvwUpdateTime.setText(updateTime);
					tvwUpdateInfo.setText(updateInfo);
					svwUpdate.setVisibility(View.VISIBLE);
					break;
				}

			}
			super.onPostExecute(result);
		}
	}

}
