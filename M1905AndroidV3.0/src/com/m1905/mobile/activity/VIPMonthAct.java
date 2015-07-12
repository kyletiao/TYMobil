package com.m1905.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.dao.EVipProduct;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.dao.User;
import com.m1905.mobile.dao.VipPayment;
import com.m1905.mobile.dao.VipProduct;
import com.m1905.mobile.net.VipService;
import com.m1905.mobile.ui.XListView;
import com.m1905.mobile.util.AppUtils;
import com.m1905.mobile.util.DialogUtil;
import com.m1905.mobile.util.StringUtils;


public class VIPMonthAct extends Activity implements OnClickListener {
	// 导航
	private TextView tvwNaviNotice;
	// 网络、加载
	private RelativeLayout ileLoading, ileNetError;
	// 数据视图
	private RelativeLayout ileVipMonth, ileVipOrder;
	private List<VipProduct> lstVipProduct;
	private VipMonthAdapter mAdapter;
	private XListView lvwVipMonth;
	private int selectIndex = 0;
	//
	private TextView tvwAccountValue, tvwMonthValue, tvwPayValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vip_shop);
		init();
		//loadVipMonth();
	}

	private void init() {
		initNavi();
		initDataView();
		initOther();
	}

	private void initNavi() {
		tvwNaviNotice = (TextView) findViewById(R.id.tvwNaviNotice);
		findViewById(R.id.btnFunc).setVisibility(View.GONE);
		findViewById(R.id.btnBack).setOnClickListener(this);
	}

	private void initOther() {
		ileLoading = (RelativeLayout) findViewById(R.id.ileLoading);
		ileNetError = (RelativeLayout) findViewById(R.id.ileNetError);
		ileNetError.findViewById(R.id.btnRefresh).setOnClickListener(this);
	}

	private void initDataView() {
		lstVipProduct = new ArrayList<VipProduct>();
		mAdapter = new VipMonthAdapter(lstVipProduct);
		ileVipMonth = (RelativeLayout) findViewById(R.id.ileVipMonth);
		lvwVipMonth = (XListView) findViewById(R.id.lvwVipMonth);
		lvwVipMonth.setAdapter(mAdapter);
		findViewById(R.id.btnVipMonth).setOnClickListener(this);
		ileVipOrder = (RelativeLayout) findViewById(R.id.ileVipOrder);
		tvwAccountValue = (TextView) findViewById(R.id.tvwAccountValue);
		tvwMonthValue = (TextView) findViewById(R.id.tvwMonthValue);
		tvwPayValue = (TextView) findViewById(R.id.tvwPayValue);
		findViewById(R.id.btnConfirmPay).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			onBackPressed();
			break;
		case R.id.btnRefresh:
			//loadVipMonth();
			break;
		case R.id.btnVipMonth:
			showVipOrderView();
			break;
		case R.id.btnConfirmPay:
			//confirmPay();
			break;
		}
	}

	/*private AsyncPayLoader payLoader = null;

	private void confirmPay() {
		if (payLoader != null) {
			payLoader.cancel(true);
		}
		DialogUtil.showProgressDialog(VIPMonthAct.this, false);
		payLoader = new AsyncPayLoader();
		payLoader.execute();
	}*/

	/*private class AsyncPayLoader extends AsyncTask<Void, Void, VipPayment> {

		@Override
		protected VipPayment doInBackground(Void... params) {
			VipProduct vipProduct = (lstVipProduct.isEmpty() || lstVipProduct
					.size() < selectIndex) ? null : lstVipProduct
					.get(selectIndex);
			User user = Identify.currentUser;
			if (vipProduct != null && user != null) {
				return VipService.getVipPaymentUrl(VIPMonthAct.this,
						vipProduct.getProCode(),
						String.valueOf(user.getUserCode()), user.getUserName(),
						user.getUserToken());
			}
			return null;
		}

		protected void onPostExecute(VipPayment result) {
			DialogUtil.dismisProgressDialog();
			if (result != null && !StringUtils.isEmpty(result.getPaymentUrl())) {
				Intent intent = new Intent(VIPMonthAct.this, WebAct.class);
				intent.putExtra("address", result.getPaymentUrl());
				startActivity(intent);
			} else {
				AppUtils.toastShowMsg(VIPMonthAct.this, "支付地址，请求失败");
			}
			super.onPostExecute(result);
		}

	}*/

	/*private AsyncLoader loader = null;

	private void loadVipMonth() {
		if (loader != null) {
			loader.cancel(true);
		}
		showLoading();
		loader = new AsyncLoader();
		loader.execute();
	}*/

	/*class AsyncLoader extends AsyncTask<Void, Void, EVipProduct> {

		@Override
		protected EVipProduct doInBackground(Void... params) {
			return VipService.getVipMonthProduct(VIPMonthAct.this);
		}

		@Override
		protected void onPostExecute(EVipProduct result) {
			if (result == null || result.getLstVipProduct().isEmpty()) {
				showNetError();
			} else {
				lstVipProduct.clear();
				lstVipProduct.addAll(result.getLstVipProduct());
				showVipMonthView();
			}
			super.onPostExecute(result);
		}

	}*/

	private void instanceOrderData() {
		String default_unknown = getString(R.string.default_unknown);
		tvwAccountValue
				.setText(Identify.currentUser != null ? Identify.currentUser
						.getUserName() : default_unknown);
		VipProduct vipProduct = (lstVipProduct.isEmpty() || lstVipProduct
				.size() < selectIndex) ? null : lstVipProduct.get(selectIndex);
		if (vipProduct != null) {
			tvwMonthValue
					.setText(StringUtils.isEmpty(vipProduct.getTitle()) ? default_unknown
							: vipProduct.getTitle());
			tvwPayValue.setText(StringUtils.formatPrice(vipProduct.getPrice())
					+ getString(R.string.tvwVipMonthUnit));
		} else {
			tvwMonthValue.setText(default_unknown);
			tvwPayValue.setText(default_unknown);
		}
	}

	private void showLoading() {
		ileNetError.setVisibility(View.GONE);
		ileVipMonth.setVisibility(View.INVISIBLE);
		ileVipOrder.setVisibility(View.GONE);
		tvwNaviNotice.setText(R.string.buy_vip);
		ileLoading.setVisibility(View.VISIBLE);
	}

	private void showNetError() {
		ileLoading.setVisibility(View.GONE);
		ileVipMonth.setVisibility(View.INVISIBLE);
		ileVipOrder.setVisibility(View.GONE);
		tvwNaviNotice.setText(R.string.buy_vip);
		ileNetError.setVisibility(View.VISIBLE);
	}

	private void showVipMonthView() {
		ileLoading.setVisibility(View.GONE);
		ileNetError.setVisibility(View.GONE);
		ileVipOrder.setVisibility(View.GONE);
		tvwNaviNotice.setText(R.string.buy_vip);
		mAdapter.notifyDataSetChanged();
		ileVipMonth.setVisibility(View.VISIBLE);
	}

	private void showVipOrderView() {
		instanceOrderData();
		ileLoading.setVisibility(View.GONE);
		ileNetError.setVisibility(View.GONE);
		ileVipMonth.setVisibility(View.GONE);
		tvwNaviNotice.setText(R.string.confirm_pay);
		ileVipOrder.setVisibility(View.VISIBLE);
	}

	class VipMonthAdapter extends BaseAdapter {
		private List<VipProduct> lstVipProduct;
		private int selectId = -1;

		public VipMonthAdapter(List<VipProduct> lstVipProduct) {
			this.lstVipProduct = lstVipProduct;
		}

		@Override
		public int getCount() {
			return lstVipProduct.size();
		}

		@Override
		public Object getItem(int position) {
			return lstVipProduct.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(VIPMonthAct.this).inflate(
						R.layout.vipmonthitem, null);
				holder = new ViewHolder();
				holder.rbnVipMonth = (RadioButton) convertView
						.findViewById(R.id.rbnVipMonth);
				holder.rbnVipMonth.setId(position);
				if (position == selectIndex) {
					holder.rbnVipMonth.setChecked(true);
					selectId = position;
				}
				holder.rbnVipMonth
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									if (selectId != -1) {
										RadioButton tmpBtn = (RadioButton) VIPMonthAct.this
												.findViewById(selectId);
										if (tmpBtn != null)
											tmpBtn.setChecked(!isChecked);
									}
									selectId = buttonView.getId();
									selectIndex = selectId;
								}
							}
						});
				holder.tvwVipMonthDesc = (TextView) convertView
						.findViewById(R.id.tvwVipMonthDesc);
				holder.tvwVipMonthUnit = (TextView) convertView
						.findViewById(R.id.tvwVipMonthUnit);
				holder.tvwVipMonthPrice = (TextView) convertView
						.findViewById(R.id.tvwVipMonthPrice);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 封装数据
			VipProduct vipProduct = (VipProduct) getItem(position);
			holder.rbnVipMonth.setText(vipProduct.getTitle());
			holder.tvwVipMonthPrice.setText(StringUtils.formatPrice(vipProduct
					.getPrice()));
			return convertView;
		}

		class ViewHolder {
			RadioButton rbnVipMonth;
			TextView tvwVipMonthDesc;
			TextView tvwVipMonthUnit;
			TextView tvwVipMonthPrice;
		}
	}

	@Override
	public void onBackPressed() {
		if (ileVipOrder.getVisibility() != View.VISIBLE) {
			//loader.cancel(true);
			super.onBackPressed();
		} else {
			showVipMonthView();
		}
	}

}
