package com.m1905.mobile.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.dao.Setting;
import com.m1905.mobile.ui.AlwaysMarqueeTextView;

public class OtherAdapter extends BaseAdapter {
	List<Setting> lstOtherSettings;
	Context context;

	public OtherAdapter(Context context, List<Setting> lstOtherSettings) {
		this.lstOtherSettings = lstOtherSettings;
		this.context = context;
	}

	@Override
	public int getCount() {
		return this.lstOtherSettings.size();
	}

	@Override
	public Object getItem(int position) {
		return this.lstOtherSettings.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.setting2_item, null);
			holder = new ViewHolder();
			holder.tvwFuncNotice = (TextView) convertView
					.findViewById(R.id.tvwFuncNotice);
			holder.tvwFuncDesc = (AlwaysMarqueeTextView) convertView
					.findViewById(R.id.tvwFuncDesc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Setting setting = (Setting) getItem(position);
		holder.tvwFuncNotice.setText(context.getString(setting
				.getFuncNoticeId()));
		String funcDesc = context.getString(setting.getFuncDescId());
		if (!TextUtils.isEmpty(funcDesc)) {
			holder.tvwFuncDesc.setText(funcDesc);
			holder.tvwFuncDesc.setVisibility(View.VISIBLE);
		} else {
			holder.tvwFuncDesc.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvwFuncNotice;
		AlwaysMarqueeTextView tvwFuncDesc;
	}
}
