package com.m1905.mobile.window;

import org.codehaus.jackson.map.ObjectMapper;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.activity.LoginAct;
import com.m1905.mobile.activity.NewLoginAct;
import com.m1905.mobile.bean.LoginBean;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.ui.LoginNameEditText;
import com.m1905.mobile.ui.LoginPassEditText;
import com.m1905.mobile.util.DialogUtil;
import com.m1905.mobile.util.StringUtils;
import com.telecomsdk.phpso.TysxOA;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.InputFilter.LengthFilter;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupWindow;

public class LoginWindow {
	
	private Context context;
	private LayoutInflater inflater;
	private View contentView;
	private PopupWindow window;
	private LoginNameEditText emailEt;
	private LoginPassEditText passwordEt;
	private LoginBean bean;
	private Button login_btn_register;
	private Button m1905Login;
	private ProgressDialog progressDialog;
	private Button bt_close;
	private Handler handler;
	public LoginWindow(final Context context,LayoutInflater inflater){
		this.context = context;
		this.inflater = inflater;
		contentView = inflater.inflate(R.layout.login_window, null);
		window = new PopupWindow(contentView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		
		bt_close = (Button) contentView.findViewById(R.id.bt_close);
		emailEt = (LoginNameEditText) contentView.findViewById(R.id.login_et_email);
		passwordEt = (LoginPassEditText) contentView.findViewById(R.id.login_et_password);
		/*emailEt.getEdt().addTextChangedListener(
				new FilterTextWatcher(1)); */
		
		m1905Login = (Button) contentView.findViewById(R.id.login_btn_login);
		
		login_btn_register = (Button) contentView.findViewById(R.id.login_btn_register);
		login_btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, LoginAct.class);
				context.startActivity(intent);
				//startActivityForResult(intent, 1);
			}
		});
		
		
	}
	
	
	private class GetM1905LoginTasks extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			TysxOA oa = new TysxOA(context);
			ObjectMapper mapper = new ObjectMapper();
			String logincontent = oa.login(TianyiContent.token, TianyiContent.devid, TianyiContent.appid, TianyiContent.appSecret, params[0],params[1]);
			System.out.println("登陆信息："+logincontent);
			//if (user != null&&user.getResult()==0) {
			try {
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
				return 2;
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
				StringUtils.showShortToast(context.getApplicationContext(),
						"登录成功");
				handler.sendEmptyMessage(14);
				window.dismiss();
				break;
			case 1:
				DialogUtil.dismissProgressDialog(progressDialog);
				StringUtils.showShortToast(context.getApplicationContext(),
						bean.getMsg());
				break;

			}
		}
	}
	public void showDialog(View parent,Handler handlers){
		handler = handlers;
		emailEt.getEdt().setHint("邮箱/手机号");
		InputFilter[] filters = {new LengthFilter(30)};   
		emailEt.getEdt().setFilters(filters);  
		emailEt.getEdt().setHeight(80);
		emailEt.getEdt().setCursorVisible(true);
		emailEt.getEdt().setTypeface(Typeface.SANS_SERIF);
		emailEt.getEdt().setHintTextColor(context.getResources().getColor(R.color.login_register_text));
		emailEt.getEdt().setTextColor(context.getResources().getColor(R.color.dark));
		emailEt.getEdt().setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String str_email = emailEt
						.getEditTextContent();
				if(hasFocus)
				{
					// 获得光标
					if (str_email != null && !str_email.equals("")) {
						emailEt.getUNameDel().setVisibility(View.VISIBLE);
						emailEt.getUNameDel().setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								emailEt.getEdt().setText("");
							}
						});
						emailEt.getTvwUname().setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								emailEt.getEdt().setText("");
							}
						});
					} else {
						emailEt.getUNameDel().setVisibility(View.GONE);
					}
				}else
				{
					emailEt.getUNameDel().setVisibility(View.GONE);
					if (str_email == null || str_email.equals("")) {
						return;
					}
					if (StringUtils.length(emailEt.getEdt().getText().toString().trim()) < 4) {
						StringUtils.showLongToast(context.getApplicationContext(), "用户名不能少于4个字符");
						return;
					}
					if (StringUtils.length(emailEt.getEdt().getText().toString().trim()) > 30) {
						StringUtils.showLongToast(context,"用户名不能超过30个字符");
						return;
					}
					// 验证用户名和邮箱
					/*if (!chkName(emailEt.getEdt().getText().toString().trim())) {
						//StringUtils.showLongToast(context.getApplicationContext(),"用户名只能使用数字或字符");
						return;
					}*/
				}
			}
		});
		//passwordEt.getEdt().addTextChangedListener(new FilterTextWatcher(2));
		passwordEt.getEdt().setTransformationMethod(PasswordTransformationMethod.getInstance()); 
		passwordEt.getEdt().setHint("密码");
		passwordEt.getEdt().setHeight(80);
		InputFilter[] filterPass = {new LengthFilter(20)};   
		passwordEt.getEdt().setFilters(filterPass);  
		passwordEt.getEdt().setCursorVisible(true);
		passwordEt.getEdt().setTypeface(Typeface.SANS_SERIF);
		passwordEt.getEdt().setHintTextColor(context.getResources().getColor(R.color.login_register_text));
		passwordEt.getEdt().setTextColor(context.getResources().getColor(R.color.dark));
		passwordEt.getEdt().setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String str_pass = passwordEt
						.getEditTextContent();
				if(hasFocus)
				{
					// 获得光标
					if (str_pass != null && !str_pass.equals("")) {
						passwordEt.getUNameDel().setVisibility(View.VISIBLE);
						passwordEt.getUNameDel().setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								passwordEt.getEdt().setText("");
							}
						});
						passwordEt.getTvwUname().setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								passwordEt.getEdt().setText("");
							}
						});
						
					} else {
						passwordEt.getUNameDel().setVisibility(View.GONE);
					}
				}else
				{
					passwordEt.getUNameDel().setVisibility(View.GONE);
					if (TextUtils.isEmpty(passwordEt.getEdt().getText().toString().trim())) {
						return;
					}
					if (StringUtils.length(passwordEt.getEdt().getText().toString().trim()) < 6) {
						StringUtils.showLongToast(context.getApplicationContext(), "密码不能少于6个字符");
						return;
					}
					/*if (StringUtils.length(passwordEt.getEdt().getText().toString().trim()) > 20) {
						StringUtils.showLongToast(NewLoginAct.this.getApplicationContext(),"密码不能超过20个字符");
						return;
					}
					// 验证密码
					if (!chkPassword(passwordEt.getEdt().getText().toString().trim())) {
						StringUtils.showLongToast(NewLoginAct.this.getApplicationContext(),"密码必须包括数字和字母");
						return;
					}*/
				}
			}
		});
		
		m1905Login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {	
				emailEt.getEdt().requestFocus();
				passwordEt.getEdt().requestFocus();
				emailEt.getEdt().setCursorVisible(false);
				passwordEt.getEdt().setCursorVisible(false);
				emailEt.getUNameDel().setVisibility(View.GONE);
				passwordEt.getUNameDel().setVisibility(View.GONE);
				// 隐藏软键盘
				/*((InputMethodManager) context.getApplicationContext().getSystemService(context.getApplicationContext().INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(context
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);*/
				String email = emailEt.getEdt().getText().toString().trim();
				String password = passwordEt.getEdt().getText().toString().trim();
				if (email.equals("") || password.equals("")) {
					StringUtils.showLongToast(context.getApplicationContext(),
							"请将信息填写完整");
				} else {
					if(Identify.currentUser==null)
					{
						if(email.length()>=4&&email.length()<=30)
						{
							if(password.length()>=6&&password.length()<=20)
							{
								progressDialog = DialogUtil.showProgressDialog(
										context, "登录中, 请稍候..");
								new GetM1905LoginTasks().execute(email, password);
							}else
							{
								StringUtils.showLongToast(context.getApplicationContext(),
										"密码6-20个字符");
							}

						}else
						{
							StringUtils.showLongToast(context.getApplicationContext(),
									"用户名4-30个字符");
						}

					}else
					{
						StringUtils.showLongToast(context.getApplicationContext(), "您已登录");
						emailEt.getEdt().setText("");
						passwordEt.getEdt().setText("");
					}
					
				}
			}
		});
		bt_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setFocusable(true);
		window.setOutsideTouchable(true);
		window.update();
		window.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}
}
