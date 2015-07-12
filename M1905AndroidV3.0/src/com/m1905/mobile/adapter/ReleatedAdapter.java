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
import com.m1905.mobile.adapter.VideoAdapter.ViewHolder;
import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.dao.Content;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.dao.Releated;

public class ReleatedAdapter extends BaseAdapter {
	private Context context;
	private List<Releated> releatedList;
	private BitmapUtils bitmapUtil;

	public ReleatedAdapter(Context context, List<Releated> releatedList) {
		this.context = context;
		this.releatedList = releatedList;
		bitmapUtil = new BitmapUtils(context, AppConfig.M1905_CACHE_PATH);
	}

	@Override
	public int getCount() {
		return releatedList.size();
	}

	@Override
	public Object getItem(int position) {
		return releatedList.get(position);
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
					R.layout.video_items, null);
			holder = new ViewHolder();
			holder.ivwVideoImg = (ImageView) convertView
					.findViewById(R.id.ivwVideoImg);
			holder.ivwVIPLogo = (ImageView) convertView
					.findViewById(R.id.ivwVIPLogo);
			holder.tvwVideoName = (TextView) convertView
					.findViewById(R.id.tvwVideoName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 封装数据
		Releated Releated = (Releated) getItem(position);
		
				holder.ivwVIPLogo.setVisibility(View.GONE);
			
		
		bitmapUtil.display(holder.ivwVideoImg, Releated.getImg());
		holder.tvwVideoName.setText(Releated.getTitle());
		return convertView;
	}

	class ViewHolder {
		ImageView ivwVideoImg;
		ImageView ivwVIPLogo;
		TextView tvwVideoName;
	}
}
