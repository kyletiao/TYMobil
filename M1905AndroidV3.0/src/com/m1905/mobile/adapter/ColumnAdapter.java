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
import com.m1905.mobile.dao.Column;

public class ColumnAdapter extends BaseAdapter {
	private Context context;
	private List<Column> columnList;
	private BitmapUtils bitmapUtil;

	public ColumnAdapter(Context context, List<Column> columnList) {
		this.context = context;
		this.columnList = columnList;
		bitmapUtil = new BitmapUtils(context, AppConfig.M1905_CACHE_PATH);
	}

	@Override
	public int getCount() {
		return columnList.size();
	}

	@Override
	public Column getItem(int position) {
		return columnList.get(position);
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
					R.layout.column_item, null);
			holder = new ViewHolder();
			holder.ivwColumnImg = (ImageView) convertView
					.findViewById(R.id.ivwColumnImg);
			holder.tvwColumnName = (TextView) convertView
					.findViewById(R.id.tvwColumnName);
			holder.tvwColumnContent = (TextView) convertView
					.findViewById(R.id.tvwColumnContent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 封装数据
		bitmapUtil.display(holder.ivwColumnImg, getItem(position).getImg());
		holder.tvwColumnName.setText(getItem(position).getTitle());
		holder.tvwColumnContent.setText(getItem(position).getDescription());
		return convertView;
	}

	class ViewHolder {
		ImageView ivwColumnImg;
		TextView tvwColumnName;
		TextView tvwColumnContent;
	}
}
