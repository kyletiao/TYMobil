package com.m1905.mobile.content;

import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.dao.User;
import com.m1905.mobile.net.VipService;
import com.m1905.mobile.ui.LoginNameEditText;
import com.m1905.mobile.ui.LoginPassEditText;
import com.m1905.mobile.ui.ScrollContent;
import com.m1905.mobile.util.DialogUtil;
import com.m1905.mobile.util.StringUtils;
import com.telecomsdk.phpso.TysxOA;

@SuppressWarnings("deprecation")
public class LoginContent extends ScrollContent {

	//private static LoginContent instance;
	private Button m1905Login;
	/*private LoginNameEditText emailEt;
	private LoginPassEditText passwordEt;
	private ProgressDialog progressDialog;
	private User user;*/

	public LoginContent(final Activity activity, int resourceID) {
		super(activity, resourceID);
		//instance = this;
	/*	emailEt = (LoginNameEditText) findViewById(R.id.login_et_email);
		passwordEt = (LoginPassEditText) findViewById(R.id.login_et_password);
		emailEt.getEdt().addTextChangedListener(
				new FilterTextWatcher(1)); 
		emailEt.getEdt().setHint("邮箱/手机号");
		InputFilter[] filters = {new LengthFilter(30)};   
		emailEt.getEdt().setFilters(filters);  
		emailEt.getEdt().setHeight(80);
		emailEt.getEdt().setCursorVisible(true);
		emailEt.getEdt().setTypeface(Typeface.SANS_SERIF);
		emailEt.getEdt().setHintTextColor(activity.getResources().getColor(R.color.login_register_text));
		emailEt.getEdt().setTextColor(activity.getResources().getColor(R.color.dark));
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
						StringUtils.showLongToast(activity.getApplicationContext(), "用户名不能少于4个字符");
						return;
					}
					if (StringUtils.length(emailEt.getEdt().getText().toString().trim()) > 30) {
						StringUtils.showLongToast(activity.getApplicationContext(),"用户名不能超过30个字符");
						return;
					}
					// 验证用户名和邮箱
					if (!chkName(emailEt.getEdt().getText().toString().trim())) {
						StringUtils.showLongToast(activity.getApplicationContext(),"用户名只能使用数字或字符");
						return;
					}
				}
			}
		});
		passwordEt.getEdt().addTextChangedListener(new FilterTextWatcher(2));
		passwordEt.getEdt().setTransformationMethod(PasswordTransformationMethod.getInstance()); 
		passwordEt.getEdt().setHint("密码");
		passwordEt.getEdt().setHeight(80);
		InputFilter[] filterPass = {new LengthFilter(20)};   
		passwordEt.getEdt().setFilters(filterPass);  
		passwordEt.getEdt().setCursorVisible(true);
		passwordEt.getEdt().setTypeface(Typeface.SANS_SERIF);
		passwordEt.getEdt().setHintTextColor(activity.getResources().getColor(R.color.login_register_text));
		passwordEt.getEdt().setTextColor(activity.getResources().getColor(R.color.dark));
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
						StringUtils.showLongToast(activity.getApplicationContext(), "密码不能少于6个字符");
						return;
					}
					if (StringUtils.length(passwordEt.getEdt().getText().toString().trim()) > 20) {
						StringUtils.showLongToast(activity.getApplicationContext(),"密码不能超过20个字符");
						return;
					}
					// 验证密码
					if (!chkPassword(passwordEt.getEdt().getText().toString().trim())) {
						StringUtils.showLongToast(activity.getApplicationContext(),"密码必须包括数字和字母");
						return;
					}
				}
			}
		});*/
		m1905Login = (Button) findViewById(R.id.login_btn_login);
		m1905Login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {	
				
				
				new GetM1905LoginTask().execute();
				/*emailEt.getEdt().requestFocus();
				passwordEt.getEdt().requestFocus();
				emailEt.getEdt().setCursorVisible(false);
				passwordEt.getEdt().setCursorVisible(false);
				emailEt.getUNameDel().setVisibility(View.GONE);
				passwordEt.getUNameDel().setVisibility(View.GONE);
				// 隐藏软键盘
				((InputMethodManager) activity.getApplicationContext().getSystemService(activity.getApplicationContext().INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(activity
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				String email = emailEt.getEdt().getText().toString().trim();
				String password = passwordEt.getEdt().getText().toString().trim();
				if (email.equals("") || password.equals("")) {
					StringUtils.showLongToast(activity.getApplicationContext(),
							"请将信息填写完整");
				} else {
					if(Identify.currentUser==null)
					{
						if(email.length()>=4&&email.length()<=30)
						{
							if(password.length()>=6&&password.length()<=20)
							{
								progressDialog = DialogUtil.showProgressDialog(
										activity, "登录中, 请稍候..");
								new GetM1905LoginTask().execute(email, password);
							}else
							{
								StringUtils.showLongToast(activity.getApplicationContext(),
										"密码6-20个字符");
							}

						}else
						{
							StringUtils.showLongToast(activity.getApplicationContext(),
									"用户名4-30个字符");
						}

					}else
					{
						StringUtils.showLongToast(activity.getApplicationContext(), "您已登录");
						emailEt.getEdt().setText("");
						passwordEt.getEdt().setText("");
					}
					
				}*/
			}
		});

	}
	
	/**
	 * 验证用户名只能为邮箱和电话号码
	 * 
	 * @param uname
	 * @return
	 */
	
//	public static boolean isMobileNO(String mobiles){       
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");       
//        Matcher m = p.matcher(mobiles);           
//        return m.matches();       
//    }   
//     //^[^0-9].*[\u4e00-\u9fa50-9a-zA-Z]+
//    public static boolean isEmail(String email){       
//    	String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";  
//        Pattern p = Pattern.compile(str);       
//        Matcher m = p.matcher(email);             
//        return m.matches();       
//    }   
	/*private boolean chkName(String uname) {
		String pattern = "^[a-zA-Z0-9_-]{4,30}$";//
		Pattern p = Pattern.compile(pattern);
		return p.matcher(uname).matches();
	}

    private boolean chkPassword(String upassword) {
		String pattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
		Pattern p = Pattern.compile(pattern);
		return p.matcher(upassword).matches();
	}

	private class GetM1905LoginTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			user = VipService.jsonToUser(activity.getApplicationContext(),
					"/User/login", params[0], params[1]);
			if (user != null&&user.getResult()==0) {
				return 2;
			}else
			{
				return 1;
			}

		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case 2:
				DialogUtil.dismissProgressDialog(progressDialog);
				Identify.currentUser = user;
				activity.finish();
				break;
			case 1:
				DialogUtil.dismissProgressDialog(progressDialog);
				StringUtils.showShortToast(activity.getApplicationContext(),
						user.getMessage());
				break;

			}
		}
	}
	
	class FilterTextWatcher implements TextWatcher {
		int filterFlag = 0;

		public FilterTextWatcher(int flag) {
			super();
			filterFlag = flag;
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			switch (filterFlag) {
			case 1:
				if (!emailEt.getEdt().getText().toString().trim().equals("")) {
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
				break;
			case 2:
				if (!passwordEt.getEdt().getText().toString().trim().equals("")) {
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
				break;
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable edt) {
			if (filterFlag>0) {
				try {
					String temp = edt.toString();
					String tem = temp.substring(temp.length() - 1,
							temp.length());
					char[] temC = tem.toCharArray();
					int mid = temC[0];
					if (filterFlag == 1 || filterFlag == 2) {
						if (mid >= 48 && mid <= 57) {// 数字
							return;
						}
						if (mid >= 65 && mid <= 90) {// 大写字母 和@
							return;
						}
						if (mid >= 97 && mid <= 122) {// 小写字母
							return;
						}
						if (mid == 46) { // .
							return;
						}
						if (mid == 64) { // @
							return;
						}
					} else {
						if (mid >= 33 && mid <= 126) {
							return;
						}
					}
					edt.delete(temp.length() - 1, temp.length());
				} catch (Exception e) {
				}
			}
		}
	};*/
	
	private class GetM1905LoginTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			TysxOA oa = new TysxOA(activity);
			String one_register = oa.register(activity, TianyiContent.token, TianyiContent.devid, TianyiContent.appid, TianyiContent.appSecret);
			System.out.println("一键注册短信是否发送成功："+one_register);
			return 2;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case 2:
				StringUtils.showShortToast(activity.getApplicationContext(),
						"短信发送成功请稍等");
				new Thread(){
					@Override
					public void run() {
						super.run();
						for(int i=60;i>0;i--){
							try {
								if(TianyiContent.user.equals("")){
									if(i==1){
										handler.sendEmptyMessage(2);
									}else{
										Thread.sleep(1000);
										Message message = new Message();
										message.arg1 = i;
										message.what = 1;
										handler.sendMessage(message);
									}
								}else{
									i=0;
									handler.sendEmptyMessage(3);
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}.start();
				//activity.finish();
				break;
			case 1:
				StringUtils.showShortToast(activity.getApplicationContext(),
						"");
				break;

			}
		}
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				m1905Login.setText(msg.arg1+"秒后未注册成功，请重新点击");
				break;
			case 2:
				m1905Login.setText("一键注册");
				break;
			case 3:
				StringUtils.showShortToast(activity.getApplicationContext(),
						"注册登录成功");
				activity.finish();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
}
