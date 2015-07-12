package com.m1905.mobile.content;

import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
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

import com.dianxin.mobilefree.R;
import com.m1905.mobile.activity.PosswordQuery;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.dao.Register;
import com.m1905.mobile.ui.LoginPassEditText;
import com.m1905.mobile.ui.RegisterNameEditText;
import com.m1905.mobile.ui.ScrollContent;
import com.m1905.mobile.ui.VerificationPassEditText;
import com.m1905.mobile.util.DialogUtil;
import com.m1905.mobile.util.StringUtils;
import com.telecomsdk.phpso.TysxOA;

public class PhoneContent extends ScrollContent {
	private Button m1905Register;
	private RegisterNameEditText emailEt;
	private LoginPassEditText passwordEt;
	private VerificationPassEditText passAgainEt;
	private ProgressDialog progressDialog;
	private Register register;
	private Button bt_verification;
	private TysxOA oa;
	public PhoneContent(final Activity activity, int resourceID) {
		super(activity, resourceID);
		oa = new TysxOA(activity);
		//instance = this;
		emailEt = (RegisterNameEditText) findViewById(R.id.register_et_email);
		emailEt.getEdt().setHint("手机号");
		emailEt.getEdt().setHeight(80);
		InputFilter[] filterName = { new LengthFilter(30) };
		emailEt.getEdt().setFilters(filterName);
		emailEt.getEdt().setCursorVisible(true);
		emailEt.getEdt()
				.setHintTextColor(activity.getResources().getColor(R.color.login_register_text));
		emailEt.getEdt().setTextColor(activity.getResources().getColor(R.color.dark));
		emailEt.getEdt().addTextChangedListener(new FilterTextWatcher(1));
		emailEt.getEdt().setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (!emailEt.getEdt().getText().toString().trim()
							.equals("")) {
						emailEt.getImg().setVisibility(View.VISIBLE);
						emailEt.getImg().setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										emailEt.getEdt().setText("");
									}
								});
						emailEt.getTvwUname().setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										emailEt.getEdt().setText("");
									}
								});
					} else {
						emailEt.getImg().setVisibility(View.GONE);
					}
				} else {
					emailEt.getImg().setVisibility(View.GONE);
					if (TextUtils.isEmpty(emailEt.getEdt().getText().toString()
							.trim())) {
						return;
					}
					if (StringUtils.length(emailEt.getEdt().getText()
							.toString().trim()) < 4) {
						StringUtils.showLongToast(activity.getApplicationContext(),
								"用户名不能少于4个字符");
						return;
					}
					if (StringUtils.length(emailEt.getEdt().getText()
							.toString().trim()) > 30) {
						StringUtils.showLongToast(activity.getApplicationContext(),
								"用户名不能超过30个字符");
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
		passwordEt = (LoginPassEditText) findViewById(R.id.register_et_password);
		passwordEt.getEdt().setHint("设置新密码");
		passwordEt.getEdt().setHeight(80);
		InputFilter[] filterPass = { new LengthFilter(20) };
		passwordEt.getEdt().setFilters(filterPass);
		passwordEt.getEdt().setCursorVisible(true);
		passwordEt.getEdt().setHintTextColor(
				activity.getResources().getColor(R.color.login_register_text));
		passwordEt.getEdt().setTextColor(activity.getResources().getColor(R.color.dark));
		passwordEt.getEdt().setTransformationMethod(
				PasswordTransformationMethod.getInstance());
		passwordEt.getEdt().addTextChangedListener(new FilterTextWatcher(2));
		passwordEt.getEdt().setOnFocusChangeListener(
				new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							if (!passwordEt.getEdt().getText().toString()
									.trim().equals("")) {
								passwordEt.getUNameDel().setVisibility(View.VISIBLE);
								passwordEt.getUNameDel().setOnClickListener(
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												passwordEt.getEdt().setText("");
											}
										});
								passwordEt.getTvwUname().setOnClickListener(
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												passwordEt.getEdt().setText("");
											}
										});
							} else {
								passwordEt.getUNameDel().setVisibility(View.GONE);
							}
						} else {
							passwordEt.getUNameDel().setVisibility(View.GONE);
							if (TextUtils.isEmpty(passwordEt.getEdt().getText()
									.toString().trim())) {
								return;
							}
							if (StringUtils.length(passwordEt.getEdt()
									.getText().toString().trim()) < 6) {
								StringUtils.showLongToast(activity.getApplicationContext(),
										"密码不能少于6个字符");
								return;
							}
							if (StringUtils.length(passwordEt.getEdt()
									.getText().toString().trim()) > 20) {
								StringUtils.showLongToast(activity.getApplicationContext(),
										"密码不能超过20个字符");
								return;
							}
							// 验证密码
							if (!chkPassword(passwordEt.getEdt().getText()
									.toString().trim())) {
								StringUtils.showLongToast(activity.getApplicationContext(),
										"密码必须包括数字和字母");
								return;
							}
						}
					}
				});
		passAgainEt = (VerificationPassEditText) findViewById(R.id.register_et_pass_again);
		passAgainEt.getEdt().setHint("输入验证码");
		passAgainEt.getEdt().setHeight(80);
		InputFilter[] filterPassAgain = { new LengthFilter(20) };
		passAgainEt.getEdt().setCursorVisible(true);
		passAgainEt.getEdt().setFilters(filterPassAgain);
		passAgainEt.getEdt().setHintTextColor(
				activity.getResources().getColor(R.color.login_register_text));
		passAgainEt.getEdt()
				.setTextColor(activity.getResources().getColor(R.color.dark));
		/*passAgainEt.getEdt().setTransformationMethod(
				PasswordTransformationMethod.getInstance());*/
		passAgainEt.getEdt().addTextChangedListener(new FilterTextWatcher(3));
		passAgainEt.getEdt().setOnFocusChangeListener(
				new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							if (!passAgainEt.getEdt().getText().toString()
									.trim().equals("")) {
								passAgainEt.getUNameDel()
										.setVisibility(View.VISIBLE);
								passAgainEt.getUNameDel().setOnClickListener(
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												passAgainEt.getEdt()
														.setText("");
											}
										});
								passAgainEt.getTvwUname().setOnClickListener(
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												passAgainEt.getEdt()
														.setText("");
											}
										});
							} else {
								passAgainEt.getUNameDel().setVisibility(View.GONE);
							}
						} else {
							passAgainEt.getUNameDel().setVisibility(View.GONE);
							/*if (TextUtils.isEmpty(passAgainEt.getEdt()
									.getText().toString().trim())) {
								return;
							}
							if (StringUtils.length(passAgainEt.getEdt()
									.getText().toString().trim()) < 6) {
								StringUtils.showLongToast(activity.getApplicationContext(),
										"密码不能少于6个字符");
								return;
							}
							if (StringUtils.length(passAgainEt.getEdt()
									.getText().toString().trim()) > 20) {
								StringUtils.showLongToast(activity.getApplicationContext(),
										"密码不能超过20个字符");
								return;
							}
							// 验证密码
							if (!chkPassword(passAgainEt.getEdt().getText()
									.toString().trim())) {
								StringUtils.showLongToast(activity.getApplicationContext(),
										"密码必须包括数字和字母");
								return;
							}
							if (!passAgainEt
									.getEdt()
									.getText()
									.toString()
									.trim()
									.equals(passwordEt.getEdt().getText()
											.toString().trim())) {
								StringUtils.showLongToast(activity.getApplicationContext(),
										"两次密码输入不一致");
								return;
							}*/
						}

					}
				});
		m1905Register = (Button)findViewById(R.id.register_btn);
		m1905Register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				register();
			}
		});
		
		bt_verification = (Button) findViewById(R.id.bt_verification);
		bt_verification.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(){

					@Override
					public void run() {
						super.run();
						if(!emailEt.getEdt().getText().toString().equals("")){
							if(emailEt.getEdt().getText().toString().length()==11){
								String init = oa.sendMsg(TianyiContent.token, emailEt.getEdt().getText().toString(), "message", TianyiContent.appid, TianyiContent.devid, TianyiContent.appSecret);
								if(init.contains("OK")){
									handler.sendEmptyMessage(4);
								}else{
									handler.sendEmptyMessage(3);
								}
							}else{
								handler.sendEmptyMessage(2);
							}
						}else{
							handler.sendEmptyMessage(1);
						}
					}
					
				}.start();
			}
		});
	}
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				StringUtils.showShortToast(activity.getApplicationContext(),
						"请填写手机号");
				break;
			case 2:
				StringUtils.showShortToast(activity.getApplicationContext(),
						"手机号不正确请重新填写");
				break;
			case 3:
				StringUtils.showShortToast(activity.getApplicationContext(),
						"获取验证码失败");
				break;
			case 4:
				StringUtils.showShortToast(activity.getApplicationContext(),
						"获取验证码成功，请稍等");
				break;
			default:
				break;
			}
		}
	};
	private void register() {
		// 隐藏软键盘
		((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(activity.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		String email = emailEt.getEdt().getText().toString();
		String password = passwordEt.getEdt().getText().toString();
		String passwordAgain = passAgainEt.getEdt().getText().toString();
		emailEt.getEdt().setCursorVisible(false);
		emailEt.getEdt().requestFocus();
		passwordEt.getEdt().setCursorVisible(false);
		passwordEt.getEdt().requestFocus();
		passAgainEt.getEdt().setCursorVisible(false);
		passAgainEt.getEdt().requestFocus();
		if (!email.equals("")) {
			if (!password.equals("")) {
				if (!passwordAgain.equals("")) {
			
							if (email.length() < 4 || email.length() > 30) {
								StringUtils.showShortToast(activity.getApplicationContext(),
										"用户名长度为4-30");
							}
							if (password.length() < 6 || password.length() > 20) {
								StringUtils.showShortToast(activity.getApplicationContext(),
										"密码长度为6-20");
							}
							
							if (email.length() >= 4 && email.length() <= 30
									&& password.length() >= 6
									&& password.length() <= 20
									) {
								progressDialog = DialogUtil.showProgressDialog(
										activity, "密码重置中, 请稍候..");
								new GetM1905RegisterTask().execute(email,
										password,passwordAgain);
							}

					

					
				} else {
					StringUtils.showShortToast(activity.getApplicationContext(), "请输入验证码");
				}
			} else {
				StringUtils.showLongToast(activity.getApplicationContext(), "请输入密码");
			}
		} else {
			StringUtils.showLongToast(activity.getApplicationContext(), "请输入手机号");
		}
	}

	private class GetM1905RegisterTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			//register = VipService.jsonToRegister(activity.getApplicationContext(), "/User/register", params[0], params[1]);
			TysxOA oa = new TysxOA(activity);
			String register = oa.resetUserPassword(TianyiContent.token, TianyiContent.devid, TianyiContent.appid, TianyiContent.appSecret,params[0] , params[1], params[2]);
			System.out.println("注册反馈信息"+register);
			/*if(register != null && register.getResult() == 0)
			{
				return 2;
			}else{
				return 1;
			}*/
					return 2;
		}
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case 2:
				DialogUtil.dismissProgressDialog(progressDialog);
				activity.finish();
				break;
			case 1:
				DialogUtil.dismissProgressDialog(progressDialog);
				StringUtils.showShortToast(activity.getApplicationContext(),
						register.getMessage());
				break;

			}
		}
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
//     
//    public static boolean isEmail(String email){       
//     String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";  
//        Pattern p = Pattern.compile(str);       
//        Matcher m = p.matcher(email);             
//        return m.matches();       
//    }   
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
					emailEt.getImg().setVisibility(View.VISIBLE);
					emailEt.getImg().setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							emailEt.getEdt().setText("");
						}
					});
					emailEt.getTvwUname().setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									emailEt.getEdt().setText("");
								}
							});
				} else {
					emailEt.getImg().setVisibility(View.GONE);
				}
				break;
			case 2:
				if (!passwordEt.getEdt().getText().toString().trim().equals("")) {
					passwordEt.getUNameDel().setVisibility(View.VISIBLE);
					passwordEt.getUNameDel().setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									passwordEt.getEdt().setText("");
								}
							});
					passwordEt.getTvwUname().setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									passwordEt.getEdt().setText("");
								}
							});
				} else {
					passwordEt.getUNameDel().setVisibility(View.GONE);
				}
				break;
			case 3:
				if (!passAgainEt.getEdt().getText().toString().trim()
						.equals("")) {
					passAgainEt.getUNameDel().setVisibility(View.VISIBLE);
					passAgainEt.getUNameDel().setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									passAgainEt.getEdt().setText("");
								}
							});
					passAgainEt.getTvwUname().setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									passAgainEt.getEdt().setText("");
								}
							});
				} else {
					passAgainEt.getUNameDel().setVisibility(View.GONE);
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
			if (filterFlag > 0) {
				try {
					String temp = edt.toString();
					String tem = temp.substring(temp.length() - 1,
							temp.length());
					char[] temC = tem.toCharArray();
					int mid = temC[0];
					if (filterFlag == 1 || filterFlag == 2 || filterFlag == 3) {
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
