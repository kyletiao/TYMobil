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
import com.m1905.mobile.dao.Content;
import com.m1905.mobile.dao.Identify;

public class VideoAdapter extends BaseAdapter {
	private Context context;
	private List<Content> lstVideos;
	private BitmapUtils bitmapUtil;

	public VideoAdapter(Context context, List<Content> lstVideos) { 
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
			holder.ivwVIPLogo = (ImageView) convertView
					.findViewById(R.id.ivwVIPLogo);
			holder.tvwVideoName = (TextView) convertView
					.findViewById(R.id.tvwVideoName);
			holder.tvwVideoXJ = (TextView) convertView.findViewById(R.id.tvwVideoXJ);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 封装数据
		Content content = (Content) getItem(position);
		if(content.getType() == 7)
		{
			if(Identify.isM1905VIP())
			{
				holder.ivwVIPLogo.setVisibility(View.GONE);
			}else
			{
				holder.ivwVIPLogo.setVisibility(View.VISIBLE);
			}
		}else{
			holder.ivwVIPLogo.setVisibility(View.GONE);
		}
		bitmapUtil.display(holder.ivwVideoImg, content.getImg());
		holder.tvwVideoName.setText(content.getTitle());
		holder.tvwVideoXJ.setText(content.getUrl());
		return convertView;
	}

	class ViewHolder {
		ImageView ivwVideoImg;
		ImageView ivwVIPLogo;
		TextView tvwVideoName;
		TextView tvwVideoXJ;
	}
}
