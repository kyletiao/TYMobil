package com.m1905.mobile.util;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.dianxin.mobilefree.R;

public class DialogUtil {

	public static ProgressDialog showProgressDialog(Context context, String text) {
		return ProgressDialog.show(context, "", text, true);
	}

	public static void dismissProgressDialog(ProgressDialog dialog) {
		try {
		} finally {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		}
	}

	public static AlertDialog.Builder showDialog(Context context, String text) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setMessage(text);
		return dialog;
	}

	private static ProgressDialog progressDialog;

	public static void showProgressDialog(Context context) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(context.getResources().getString(
				R.string.waitForLoading));
		// progressDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
		progressDialog.setCanceledOnTouchOutside(false);// 设置点击进度对话框外的区域对话框不消失
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	/**
	 * @param context
	 * @WZL wzl200711402@163.com @2012-3-26@下午01:34:02
	 * @description 显示加载进度框
	 */
	public static void showProgressDialog(Context context, boolean cancelable) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(context.getResources().getString(
				R.string.waitForLoading));
		progressDialog.setCancelable(cancelable);// 设置进度条是否可以按退回键取消
		progressDialog.setCanceledOnTouchOutside(false);// 设置点击进度对话框外的区域对话框不消失
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	public static void showProgressDialog(Context context, String message,
			boolean cancelable) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(cancelable);// 设置进度条是否可以按退回键取消
		progressDialog.setCanceledOnTouchOutside(false);// 设置点击进度对话框外的区域对话框不消失
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	public static void dismisProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
}
