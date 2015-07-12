package com.m1905.mobile.adapter;

import java.util.List;

import com.dianxin.mobilefree.R;


import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ViewPagerAdapter extends PagerAdapter {
	// 界面列表
		private List<View> views;
		private Handler mhandler;
		public ViewPagerAdapter(List<View> views , Handler handler) {
			this.views = views;
			this.mhandler = handler;
		}

		// 销毁arg1位置的界面
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager)arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		// 获得当前界面数
		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}

			return 0;
		}

		// 初始化arg1位置的界面
		@Override
		public Object instantiateItem(View arg0, int arg1) {

			((ViewPager)arg0).addView(views.get(arg1), 0);
			if(arg1 == views.size()-1){
				Button bt_main = (Button) arg0.findViewById(R.id.bt_main);
				bt_main.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mhandler.sendEmptyMessage(1);
					}
				});
			}
			return views.get(arg1);
		}

		// 判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

	}
