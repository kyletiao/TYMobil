package com.m1905.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.bean.MsgBean;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.ui.VerificationPassEditText;
import com.m1905.mobile.util.NetHttpConnection;
import com.telecomsdk.phpso.TysxOA2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class KbuyAct extends Activity {
	private VerificationPassEditText kauser;
	private VerificationPassEditText kapassword;
	private Button register_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_kbuy);
		findViewById(R.id.btnFunc).setVisibility(View.GONE);
		((TextView) findViewById(R.id.tvwNaviNotice)).setText("卡支付");
		Button btnBack = (Button) (findViewById(R.id.btnBack));
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		kauser = (VerificationPassEditText) findViewById(R.id.ka_user);
		kauser.getEdt().setHint("卡号");
		kauser.getEdt().setHeight(80);
		kauser.getEdt().setHintTextColor(KbuyAct.this.getResources().getColor(R.color.login_register_text));
		kauser.getEdt().setTextColor(KbuyAct.this.getResources().getColor(R.color.dark));
		kapassword = (VerificationPassEditText) findViewById(R.id.ka_password);
		kapassword.getEdt().setHint("密码");
		kapassword.getEdt().setHeight(80);
		kapassword.getEdt().setHintTextColor(KbuyAct.this.getResources().getColor(R.color.login_register_text));
		kapassword.getEdt().setTextColor(KbuyAct.this.getResources().getColor(R.color.dark));
		register_btn = (Button) findViewById(R.id.register_btn);
		register_btn.setOnClickListener(new OnClickListener() {
			
			private String url;
			@Override
			public void onClick(View v) {
				TysxOA2 oa = new TysxOA2();
				List mlist = new ArrayList (); 
				mlist.add(new BasicNameValuePair("clienttype", "3")); 
				mlist.add(new BasicNameValuePair("token", TianyiContent.token)); 
				mlist.add(new BasicNameValuePair("productid", "1000000054")); 
				mlist.add(new BasicNameValuePair("carPass",kapassword.getEdt().getText().toString()));
				mlist.add(new BasicNameValuePair("carNum",kauser.getEdt().getText().toString()));

				url = oa.getUrl("https://api.tv189.com/v2/Internet", "user", "carActive", TianyiContent.devid, TianyiContent.appid, TianyiContent.appSecret, mlist);
				new Thread(){

					@Override
					public void run() {
						super.run();
						try {
							ObjectMapper mapper = new ObjectMapper();
							String kcontent = NetHttpConnection.httpGet(url, null);
							bean = mapper.readValue(kcontent, MsgBean.class);
							handler.sendEmptyMessage(1);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		});
	}
	private MsgBean bean;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Toast.makeText(KbuyAct.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};
}
