package com.m1905.mobile.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

public class XViewFlipper extends ViewFlipper {

	private GestureDetector gestureDetector;
	private ScrollView scrollView;

	public XViewFlipper(Context context) {
		super(context);
	}

	public XViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}

	public void setScrollView(ScrollView scrollView) {
		this.scrollView = scrollView;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		super.dispatchTouchEvent(ev);
		if (scrollView != null)
			scrollView.requestDisallowInterceptTouchEvent(true);
		gestureDetector.onTouchEvent(ev);
		return true;
	}

}
