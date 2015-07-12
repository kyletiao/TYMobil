package com.m1905.mobile.window;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.alipay.android.app.sdk.AliPay;
import com.dianxin.mobilefree.R;
import com.m1905.mobile.activity.M1905VideoPlayerActivity;
import com.m1905.mobile.bean.ZhiPayBean;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.util.DialogUtil;
import com.m1905.mobile.util.StringUtils;
import com.telecomsdk.phpso.TysxOA;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;

public class ZhiBuyWindow {
	private Context context;
	private LayoutInflater inflater;
	private View contentView;
	private PopupWindow window;
	private ProgressDialog progressDialog;
	private RadioButton cb_15;
	private RadioButton cb_90;
	private RadioButton cb_180;
	private RadioButton cb_zfb;
	private Button login_btn_login;
	private String contentid;
	private Handler mhandler;

	public ZhiBuyWindow(final Context context, LayoutInflater inflater) {
		this.context = context;
		this.inflater = inflater;
		contentView = inflater.inflate(R.layout.zhi_buy_window, null);
		window = new PopupWindow(contentView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		Button bt_close = (Button) contentView.findViewById(R.id.bt_close);
		bt_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
		cb_15 = (RadioButton) contentView.findViewById(R.id.cb_15);
		cb_15.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cb_15.setChecked(true);
				cb_90.setChecked(false);
				cb_180.setChecked(false);
			}
		});
		cb_90 = (RadioButton) contentView.findViewById(R.id.cb_90);
		cb_90.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cb_90.setChecked(true);
				cb_15.setChecked(false);
				cb_180.setChecked(false);
			}
		});
		cb_180 = (RadioButton) contentView.findViewById(R.id.cb_180);
		cb_180.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cb_180.setChecked(true);
				cb_90.setChecked(false);
				cb_15.setChecked(false);
			}
		});
		cb_zfb = (RadioButton) contentView.findViewById(R.id.cb_zfb);
		cb_zfb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cb_zfb.setChecked(true);
			}
		});
		login_btn_login = (Button) contentView.findViewById(R.id.login_btn_login);
	}

	public void ShowDialog(View parent, final String contentid,Handler handler) {
		this.contentid = contentid;
		this.mhandler = handler;
		login_btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(cb_15.isChecked()){
					zhiPay(15);
				}else if(cb_90.isChecked()){
					zhiPay(90);
				}else{
					zhiPay(180);
				}
			}
		});
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setFocusable(true);
		window.setOutsideTouchable(true);
		window.update();
		window.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}
	
	private void zhiPay(final int fee){
		final TysxOA oa = new TysxOA(context);
		progressDialog = DialogUtil.showProgressDialog(
				context, "请稍候..");
		new Thread(){
			@Override
			public void run() {
				super.run();
				System.out.println("订购项目："+fee);
				int num = fee/15;
				String paycontent = oa.alipayPay(TianyiContent.token, TianyiContent.devid, TianyiContent.appid, TianyiContent.appSecret, "com.dianxin.mobilefree", "1000000054", num, 1500, "TV189院线", "TV189院线", contentid, 0, null);
				System.out.println("支付宝接口反馈数据："+paycontent);
				ObjectMapper mapper = new ObjectMapper();
				try {
					nhandler.sendEmptyMessage(2);
					
					ZhiPayBean bean = mapper.readValue(paycontent, ZhiPayBean.class);
					String encodeSign = URLEncoder.encode(bean.getSign(), "UTF-8");
					String orderInfo = getOrderInfo(bean.getOut_trade_no(),"TV189院线","TV189院线",fee*100);
					String info =orderInfo + "&sign=" + "\"" + encodeSign + "\"" + "&sign_type=" + "\"RSA\"";
					System.out.println("支付内容："+info);
					AliPay alipay = new AliPay((Activity) context, mhandler);
					
					String result = alipay.pay(info);
					Message msg = new Message();
					msg.what = 15;
					msg.obj = result;
					mhandler.sendMessage(msg);
					nhandler.sendEmptyMessage(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	Handler nhandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				window.dismiss();
				break;
			case 2:
				progressDialog.dismiss();
				break;
			default:
				break;
			}
		}
		
	};
	private String getNewOrderInfo(String oid,int fee) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(TianyiContent.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(oid);
		sb.append("\"&subject=\"");
		sb.append("TV189院线");
		sb.append("\"&body=\"");
		sb.append("TV189院线");
		sb.append("\"&total_fee=\"");
		int num = fee;
		sb.append(num+"");
		//sb.append("0.01");
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode("http://notify.java.jpxx.org/index.jsp"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(TianyiContent.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		 sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");
		return sb.toString();
	}
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	 private String getOrderInfo(String tradeNo, String pName, String pDesc, int fee) {
	        String sb = "partner=\"" + TianyiContent.DEFAULT_PARTNER + "\"";
	        sb += "&seller=\"" + TianyiContent.DEFAULT_SELLER + "\"";
	        sb += "&out_trade_no=\"" + tradeNo + "\"";
	        sb += "&subject=\"" + pName + "\"";
	        sb += "&body=\"" + pDesc + "\"";
	        sb += "&total_fee=\"" + new DecimalFormat("0.00").format(fee * 0.01) + "\"";
	        sb += "&notify_url=\"" + "http://paymentgw.tv189.com/bmspay/msp/alipay/alipayMspNotify.action" + "\"";
	        return sb;
	    }

}
