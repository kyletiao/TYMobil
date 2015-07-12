package com.m1905.mobile.content;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.activity.LoginAct;
import com.m1905.mobile.activity.VIPMonthAct;
import com.m1905.mobile.adapter.PlayerBuyAdapter;
import com.m1905.mobile.dao.Buy;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.ui.ScrollContent;

public class VideoBuyContent extends ScrollContent {

	private static VideoBuyContent instance;
	private GridView player_buy;
	private PlayerBuyAdapter buyAdapter;
	private ArrayList<Buy> buys;
	private Button btn_goIn;

	public VideoBuyContent(final Activity activity, int resourceID) {
		super(activity, resourceID);
		instance = this;
		player_buy = (GridView) findViewById(R.id.player_buy);
		btn_goIn = (Button) findViewById(R.id.btn_goIn);

		buys = new ArrayList<Buy>();
		buyAdapter = new PlayerBuyAdapter(context, buys);
		player_buy.setAdapter(buyAdapter);

		loadBuyData();
		buyAdapter.notifyDataSetChanged();

		// if (Identify.currentUser != null) {
		// player_buy.setBackgroundResource(R.drawable.player_bug_h);
		// } else {
		// player_buy.setBackgroundResource(R.drawable.player_bug_n);
		// }
		buyAdapter.setEnabled(Identify.currentUser != null);

		btn_goIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Identify.currentUser != null) {
					// 购买处理
					// StringUtils.showLongToast(activity.getApplicationContext(),
					// "您已登录，稍候进入购买");
					Intent intent = new Intent(
							activity.getApplicationContext(), VIPMonthAct.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(
							activity.getApplicationContext(), LoginAct.class);
					startActivity(intent);
					// activity.finish();
				}
			}
		});

		player_buy
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

					}
				});

	}

	private void loadBuyData() {
		buys.add(new Buy(R.drawable.vip_last, "最新", "院线新片抢先看"));
		buys.add(new Buy(R.drawable.vip_complete, "最全", "3000部最全影库"));
		buys.add(new Buy(R.drawable.vip_hd, "超清", "超高清画质观影"));
		buys.add(new Buy(R.drawable.vip_exclusive, "专享", "会员享专属客服"));
	}
}
