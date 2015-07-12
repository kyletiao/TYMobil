package com.m1905.mobile.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;


public abstract class ScrollContent {

	protected Activity activity;
	protected Context context;
	protected int resourceID;
	protected LayoutInflater inflater;
	protected View view;
	protected Resources mRes;

	public ScrollContent(Activity activity, int resourceID) {
		this.activity = activity;
		this.context = activity;
		this.resourceID = resourceID;
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(resourceID, null);
		mRes = context.getResources();
	}

	protected View findViewById(int id) {
		return view.findViewById(id);
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public void overridePendingTransition(int enterAnim, int exitAnim) {
		activity.overridePendingTransition(enterAnim, exitAnim);
	}

	public void startActivity(Intent intent) {
		activity.startActivity(intent);
	}

	public void startActivityForResult(Intent intent, int requestCode) {
		activity.startActivityForResult(intent, requestCode);
	}
}
