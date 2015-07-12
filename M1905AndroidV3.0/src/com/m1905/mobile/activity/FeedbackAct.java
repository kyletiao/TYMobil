package com.m1905.mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.dao.Feedback;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.net.MineService;
import com.m1905.mobile.util.AppUtils;

/**
 * 
 * @author leepan
 * @since 2013-12-25
 */
public class FeedbackAct extends Activity implements
		android.view.View.OnClickListener {
	private final int length = 50;
	private Button btnSubmit;
	private EditText edtFeedback;
	private Handler mHandler;
	private AlertDialog emailWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_feedback);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		mHandler = new Handler();
		btnSubmit = (Button) findViewById(R.id.btnFunc);
		btnSubmit.setOnClickListener(this);
		btnSubmit.setText(getString(R.string.btnSubmit));
        btnSubmit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        btnSubmit.setTextColor(Color.WHITE);
		((TextView) findViewById(R.id.tvwNaviNotice))
				.setText(R.string.func_feedback);
		edtFeedback = (EditText) findViewById(R.id.edtFeedback);
	}

	private void submit() {
		hideSoftInputFromWindow();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				String feed = edtFeedback.getText().toString().trim();
				if (!TextUtils.isEmpty(feed)) {
					//
					if (feed.length() > length) {
						edtFeedback.setSelection(length, feed.length());
						AppUtils.toastShowMsg(FeedbackAct.this, "亲，我们有字数上限");
					} else {
						popupEmailWindow();
					}
				} else {
					AppUtils.toastShowMsg(FeedbackAct.this, "请填写您的宝贵意见");
				}
			}
		}, 300);

	}

	/**
	 * 提示输入email
	 */
	private void popupEmailWindow() {
		if (emailWindow == null) {
			createInputEmailWindow();
		}
		emailWindow.show();
	}

	/**
	 * 创建输入email视图
	 */
	private void createInputEmailWindow() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.emailNoticeTitle));
		builder.setMessage(R.string.emailNoticeDesc);
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				submit(DialogInterface.BUTTON_POSITIVE == which);
			}
		};
		builder.setPositiveButton(R.string.btnAllowSubmit, listener);
		builder.setNegativeButton(R.string.btnNoEmial, listener);
		emailWindow = builder.create();
	}

	private void submit(boolean isAllowEmail) {
		new AsyncSubmit().execute(isAllowEmail);
	}

	/**
	 * 返回按钮操作
	 * 
	 * @param view
	 */
	public void back(View view) {
		hideSoftInputFromWindow();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				finish();
			}
		}, 300);

	}

	/**
	 * 关闭输入法
	 */
	private void hideSoftInputFromWindow() {
		InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(getCurrentFocus()
				.getApplicationWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnFunc:
			submit();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @author Victor.Yang
	 * 
	 */
	private class AsyncSubmit extends AsyncTask<Boolean, Void, Feedback> {

		@Override
		protected Feedback doInBackground(Boolean... params) {
			if (params.length <= 0)
				return null;
			boolean isAllowEmail = params[0].booleanValue();
			String email = null;
			if (isAllowEmail && Identify.currentUser != null
					&& !TextUtils.isEmpty(Identify.currentUser.getUserEmail()))
				email = Identify.currentUser.getUserEmail();
			/*Feedback feedback = MineService.submitFeedback(FeedbackAct.this,
					edtFeedback.getText().toString(), email);*/
			return null;
			//return feedback;
		}

		@Override
		protected void onPostExecute(Feedback result) {
			if (result != null && result.getStatus() == Feedback.STATUS_OK) {
				AppUtils.toastShowMsg(FeedbackAct.this,
						getString(R.string.submitSuccess));
				edtFeedback.setText("");
			} else {
				AppUtils.toastShowMsg(FeedbackAct.this,
						getString(R.string.submitError));
			}
			super.onPostExecute(result);
		}

	}

}
