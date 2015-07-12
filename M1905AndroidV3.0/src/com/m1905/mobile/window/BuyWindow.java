package com.m1905.mobile.window;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.bean.LoginBean;
import com.m1905.mobile.bean.ScreeningBean;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.content.VideoInfoContent;
import com.m1905.mobile.util.DialogUtil;
import com.m1905.mobile.util.StringUtils;
import com.telecomsdk.phpso.TysxOA;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;

public class BuyWindow {
	private Context context;
	private LayoutInflater inflater;
	private View contentView;
	private Button login_btn_login;
	private PopupWindow window;
	private ProgressDialog progressDialog;
	private VideoInfoContent videoInfoContents;
	
	public BuyWindow(final Context context,LayoutInflater inflater){
		this.context = context;
		this.inflater = inflater;
		contentView = inflater.inflate(R.layout.buy_window, null);
		login_btn_login = (Button) contentView.findViewById(R.id.login_btn_login);
		window = new PopupWindow(contentView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		Button bt_close = (Button) contentView.findViewById(R.id.bt_close);
		bt_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
	}
	
	public void ShowDialog(View parent,final String contentid,VideoInfoContent videoInfoContent){
		this.videoInfoContents = videoInfoContent;
		login_btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				progressDialog = DialogUtil.showProgressDialog(
						context, "登录中, 请稍候..");
				new BuyContentTasks().execute(contentid);
			}
		});
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setFocusable(true);
		window.setOutsideTouchable(true);
		window.update();
		window.showAtLocation(parent, Gravity.CENTER, 0, 0);
		
	}
	
	private class BuyContentTasks extends AsyncTask<String, Void, Integer> {

		private ScreeningBean bean;

		@Override
		protected Integer doInBackground(String... params) {
			TysxOA oa = new TysxOA(context);
			ObjectMapper mapper = new ObjectMapper();
			String logincontent = oa.subscribe(TianyiContent.token,"1000000054", params[0],"0", TianyiContent.appid,TianyiContent.devid, TianyiContent.appSecret);
			System.out.println("订购信息："+logincontent);
			try {
				bean = mapper.readValue(logincontent, ScreeningBean.class);
				if(bean.getCode()==0){
					return 1;
				}else{
					return 2;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				return 3;
			}
			//if (user != null&&user.getResult()==0) {
			/*try {
				bean = mapper.readValue(logincontent, LoginBean.class);
				if (bean.getCode()==0) {
					TianyiContent.user = bean.getInfo().getNickName();
					return 2;
				}else{
					return 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				return 2;*/
			//}else
			//{
			//	return 1;
			//}

		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case 2:
				DialogUtil.dismissProgressDialog(progressDialog);
				//Identify.currentUser = user;
				//context.finish();
				System.out.println("code:"+bean.getCode());
				StringUtils.showShortToast(context.getApplicationContext(),
						bean.getMsg());
				window.dismiss();
				break;
			case 1:
				DialogUtil.dismissProgressDialog(progressDialog);
				StringUtils.showShortToast(context.getApplicationContext(),
						"订购成功");
				videoInfoContents.playurl="917";
				window.dismiss();
				break;
			case 3:
				StringUtils.showShortToast(context.getApplicationContext(),
						"订购失败，请稍后再试");
				break;

			}
		}
	}
}
