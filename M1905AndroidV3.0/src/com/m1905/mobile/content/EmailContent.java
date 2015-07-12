package com.m1905.mobile.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.bean.MsgBean;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.ui.ScrollContent;
import com.m1905.mobile.ui.VerificationPassEditText;
import com.m1905.mobile.util.NetHttpConnection;
import com.telecomsdk.phpso.TysxOA2;

public class EmailContent extends ScrollContent {

	private VerificationPassEditText register_et_pass_again;
	private Button register_btn;

	public EmailContent(final Activity activity, int resourceID) {
		super(activity, resourceID);
		register_et_pass_again = (VerificationPassEditText) findViewById(R.id.register_et_pass_again);
		register_et_pass_again.getEdt().setHint("邮箱账户");
		register_et_pass_again.getEdt().setHeight(80);
		register_et_pass_again.getEdt().setCursorVisible(true);
		register_et_pass_again.getEdt()
				.setHintTextColor(activity.getResources().getColor(R.color.login_register_text));
		register_et_pass_again.getEdt().setTextColor(activity.getResources().getColor(R.color.dark));
		register_btn = (Button) findViewById(R.id.register_btn);
		register_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(register_et_pass_again.getEdt().getText().toString().contains("@")){
					new Thread(){
						

						@Override
						public void run() {
							super.run();
							TysxOA2 oa = new TysxOA2();
							List mList = new ArrayList (); 
							mList.add(new BasicNameValuePair("token", TianyiContent.token)); 
							mList.add(new BasicNameValuePair("accountNo", register_et_pass_again.getEdt().getText().toString())); 
							mList.add(new BasicNameValuePair("channelID", TianyiContent.channelId)); 
							mList.add(new BasicNameValuePair("clienttype", "3")); 

							String url = oa.getUrl("https://api.tv189.com/v2/Live", "user", "resetUserPwd", TianyiContent.devid, TianyiContent.appid, TianyiContent.appSecret,mList );
							try {
								ObjectMapper mapper = new ObjectMapper();
								String econtent = NetHttpConnection.httpGet(url, null);
								bean = mapper.readValue(econtent, MsgBean.class);
								handler.sendEmptyMessage(1);
								System.out.println("邮箱找回反馈内容"+econtent);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();
				}else{
					Toast.makeText(activity, "请正确输入邮箱账号", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	private MsgBean bean;
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(activity, bean.getMsg(), Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};

}
