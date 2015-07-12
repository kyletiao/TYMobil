package com.m1905.mobile.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

public class MineViewFlipper extends ViewFlipper {

	private GestureDetector gestureDetector;

	public MineViewFlipper(Context context) {
		super(context);
	}

	public MineViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		super.dispatchTouchEvent(ev);
		gestureDetector.onTouchEvent(ev);
		return true;
	}

}
