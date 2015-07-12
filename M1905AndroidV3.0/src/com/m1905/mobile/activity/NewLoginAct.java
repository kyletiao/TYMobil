package com.m1905.mobile.activity;

import java.io.IOException;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.bean.LoginBean;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.content.LoginContent;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.dao.User;
import com.m1905.mobile.net.VipService;
import com.m1905.mobile.ui.LoginNameEditText;
import com.m1905.mobile.ui.LoginPassEditText;
import com.m1905.mobile.util.DialogUtil;
import com.m1905.mobile.util.StringUtils;
import com.telecomsdk.phpso.TysxOA;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

public class NewLoginAct extends Activity {
	private static LoginContent instance;
	private Button m1905Login;
	private LoginNameEditText emailEt;
	private LoginPassEditText passwordEt;
	private ProgressDialog progressDialog;
	//private User user;
	private LoginBean bean;
	private Button login_btn_register;
	private Button btnBack;
	private TextView tv_password_query;
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(NewLoginAct.this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(NewLoginAct.this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_login);
		tv_password_query = (TextView) findViewById(R.id.tv_password_query);
		tv_password_query.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewLoginAct.this, PosswordQuery.class);
				startActivity(intent);
			}
		});
		btnBack = (Button)(findViewById(R.id.btnBack));
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		emailEt = (LoginNameEditText) findViewById(R.id.login_et_email);
		passwordEt = (LoginPassEditText) findViewById(R.id.login_et_password);
		/*emailEt.getEdt().addTextChangedListener(
				new FilterTextWatcher(1)); */
		emailEt.getEdt().setHint("邮箱/手机号");
		InputFilter[] filters = {new LengthFilter(30)};   
		emailEt.getEdt().setFilters(filters);  
		emailEt.getEdt().setHeight(80);
		emailEt.getEdt().setCursorVisible(true);
		emailEt.getEdt().setTypeface(Typeface.SANS_SERIF);
		emailEt.getEdt().setHintTextColor(NewLoginAct.this.getResources().getColor(R.color.login_register_text));
		emailEt.getEdt().setTextColor(NewLoginAct.this.getResources().getColor(R.color.dark));
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
						StringUtils.showLongToast(NewLoginAct.this.getApplicationContext(), "用户名不能少于4个字符");
						return;
					}
					if (StringUtils.length(emailEt.getEdt().getText().toString().trim()) > 30) {
						StringUtils.showLongToast(NewLoginAct.this.getApplicationContext(),"用户名不能超过30个字符");
						return;
					}
					// 验证用户名和邮箱
					/*if (!chkName(emailEt.getEdt().getText().toString().trim())) {
						StringUtils.showLongToast(NewLoginAct.this.getApplicationContext(),"用户名只能使用数字或字符");
						return;
					}*/
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
		passwordEt.getEdt().setHintTextColor(NewLoginAct.this.getResources().getColor(R.color.login_register_text));
		passwordEt.getEdt().setTextColor(NewLoginAct.this.getResources().getColor(R.color.dark));
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
						StringUtils.showLongToast(NewLoginAct.this.getApplicationContext(), "密码不能少于6个字符");
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
		m1905Login = (Button) findViewById(R.id.login_btn_login);
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
				((InputMethodManager) NewLoginAct.this.getApplicationContext().getSystemService(NewLoginAct.this.getApplicationContext().INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(NewLoginAct.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				String email = emailEt.getEdt().getText().toString().trim();
				String password = passwordEt.getEdt().getText().toString().trim();
				if (email.equals("") || password.equals("")) {
					StringUtils.showLongToast(NewLoginAct.this.getApplicationContext(),
							"请将信息填写完整");
				} else {
					if(Identify.currentUser==null)
					{
						if(email.length()>=4&&email.length()<=30)
						{
							if(password.length()>=6&&password.length()<=20)
							{
								progressDialog = DialogUtil.showProgressDialog(
										NewLoginAct.this, "登录中, 请稍候..");
								new GetM1905LoginTask().execute(email, password);
							}else
							{
								StringUtils.showLongToast(NewLoginAct.this.getApplicationContext(),
										"密码6-20个字符");
							}

						}else
						{
							StringUtils.showLongToast(NewLoginAct.this.getApplicationContext(),
									"用户名4-30个字符");
						}

					}else
					{
						StringUtils.showLongToast(NewLoginAct.this.getApplicationContext(), "您已登录");
						emailEt.getEdt().setText("");
						passwordEt.getEdt().setText("");
					}
					
				}
			}
		});
		login_btn_register = (Button) findViewById(R.id.login_btn_register);
		login_btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewLoginAct.this, LoginAct.class);
				//startActivity(intent);
				startActivityForResult(intent, 1);
			}
		});
	}
	
	private boolean chkName(String uname) {
		String pattern = "^[a-zA-Z0-9_-]{4,30}$";//
		Pattern p = Pattern.compile(pattern);
		return p.matcher(uname).matches();
	}

    private boolean chkPassword(String upassword) {
		String pattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
		Pattern p = Pattern.compile(pattern);
		return p.matcher(upassword).matches();
	}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == 1) {
    		if(!TianyiContent.user.equals("")){
    			NewLoginAct.this.finish();
    		}
    	}
    };
    
	private class GetM1905LoginTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			TysxOA oa = new TysxOA(NewLoginAct.this);
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
				NewLoginAct.this.finish();
				break;
			case 1:
				DialogUtil.dismissProgressDialog(progressDialog);
				StringUtils.showShortToast(NewLoginAct.this.getApplicationContext(),
						bean.getMsg());
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
	};
	
}
