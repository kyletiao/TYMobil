package com.m1905.mobile.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.dao.Hot;

public class HotWordAdapter extends BaseAdapter {
	private Context context;
	private List<Hot> lstHotWord;

	public HotWordAdapter(Context context, List<Hot> lstHotWord) {
		this.context = context;
		this.lstHotWord = lstHotWord;
	}

	@Override
	public int getCount() {
		return lstHotWord.size();
	}

	@Override
	public Object getItem(int position) {
		return lstHotWord.get(position);
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
					R.layout.hot_item, null);
			holder = new ViewHolder();
			holder.tvwHotWord = (TextView) convertView
					.findViewById(R.id.tvwHotWord);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 封装数据
		Hot hot = (Hot) getItem(position);
		holder.tvwHotWord.setText(hot.getTitle());
		return convertView;
	}

	class ViewHolder {
		TextView tvwHotWord;
	}
}
