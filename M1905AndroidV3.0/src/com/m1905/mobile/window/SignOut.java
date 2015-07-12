package com.m1905.mobile.window;

import com.dianxin.mobilefree.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class SignOut {
	
	private Context context;
	private LayoutInflater inflater;
	private View contentView;
	private PopupWindow window;
	private RelativeLayout rl_ok;
	private RelativeLayout rl_no;
	
	public SignOut(final Context context,LayoutInflater inflater){
		this.context = context;
		this.inflater = inflater;
		contentView = inflater.inflate(R.layout.win_sign_out, null);
		rl_ok = (RelativeLayout) contentView.findViewById(R.id.rl_ok);
		rl_no = (RelativeLayout) contentView.findViewById(R.id.rl_no);
		window = new PopupWindow(contentView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		
	}
	public void ShowDialog(View parent,final Activity activity){
		rl_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
		rl_no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.finish();
				
			}
		});
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setFocusable(true);
		window.setOutsideTouchable(true);
		window.update();
		window.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}
}
