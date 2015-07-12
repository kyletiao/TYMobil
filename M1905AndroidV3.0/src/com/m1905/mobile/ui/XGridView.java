package com.m1905.mobile.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 不带滚动功能的GridView，在ScrollView中使用
 * 
 * @author forcetech
 * 
 */
public class XGridView extends GridView {
	public XGridView(Context context) {
		super(context);
	}

	public XGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public XGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
