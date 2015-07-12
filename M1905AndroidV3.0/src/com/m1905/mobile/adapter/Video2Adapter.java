package com.m1905.mobile.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.lidroid.xutils.BitmapUtils;
import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.dao.Top;

public class Video2Adapter extends BaseAdapter {
	private Context context;
	private List<Top> lstVideos;
	private BitmapUtils bitmapUtil;

	public Video2Adapter(Context context, List<Top> lstVideos) {
		this.context = context;
		this.lstVideos = lstVideos;
		bitmapUtil = new BitmapUtils(context, AppConfig.M1905_CACHE_PATH);
	}

	@Override
	public int getCount() {
		return lstVideos.size();
	}

	@Override
	public Object getItem(int position) {
		return lstVideos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.video_item, null);
			holder = new ViewHolder();
			holder.ivwVideoImg = (ImageView) convertView
					.findViewById(R.id.ivwVideoImg);
//			holder.ivwVideoImg.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					AppUtils.toastShowMsg(context, holder.tvwVideoName
//							.getText().toString());
//				}
//			});
			holder.ivwVIPLogo = (ImageView) convertView
					.findViewById(R.id.ivwVIPLogo);
			holder.tvwVideoName = (TextView) convertView
					.findViewById(R.id.tvwVideoName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 封装数据
		Top top = (Top) getItem(position);
		holder.ivwVIPLogo.setVisibility(View.GONE);
		bitmapUtil.display(holder.ivwVideoImg, top.getImg());
		holder.tvwVideoName.setText(top.getTitle());
		return convertView;
	}

	class ViewHolder {
		ImageView ivwVideoImg;
		ImageView ivwVIPLogo;
		TextView tvwVideoName;
	}
}
