package com.m1905.mobile.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.dao.Setting;
import com.m1905.mobile.ui.AlwaysMarqueeTextView;
import com.m1905.mobile.util.SettingUtils;

public class SettingAdapter extends BaseAdapter {
	List<Setting> lstSettings;
	Context context;

	public SettingAdapter(Context context, List<Setting> lstSettings) {
		this.lstSettings = lstSettings;
		this.context = context;
	}

	@Override
	public int getCount() {
		return this.lstSettings.size();
	}

	@Override
	public Object getItem(int position) {
		return this.lstSettings.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final Setting setting = (Setting) getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.setting_item, null);
			holder = new ViewHolder();
			holder.tvwFuncNotice = (TextView) convertView
					.findViewById(R.id.tvwFuncNotice);
			holder.tvwFuncDesc = (AlwaysMarqueeTextView) convertView
					.findViewById(R.id.tvwFuncDesc);
			holder.btnSwitch = (ToggleButton) convertView
					.findViewById(R.id.btnSwitch);
			holder.btnSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							ToggleButton btnSwitch = (ToggleButton) buttonView;
							int id = Integer.parseInt(btnSwitch.getTag().toString());
							if (setting.getFuncNoticeId() == id
									&& (setting.isOn() != isChecked))
								setFuncValue(id, isChecked);
						}
					});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvwFuncNotice.setText(context.getString(setting
				.getFuncNoticeId()));
		String funcDesc = context.getString(setting.getFuncDescId());
		if (!TextUtils.isEmpty(funcDesc)) {
			holder.tvwFuncDesc.setText(funcDesc);
			holder.tvwFuncDesc.setVisibility(View.VISIBLE);
		} else {
			holder.tvwFuncDesc.setVisibility(View.GONE);
		}
		holder.btnSwitch.setTag(setting.getFuncNoticeId());
		holder.btnSwitch.setChecked(setting.isOn());
		return convertView;
	}

	/**
	 * 
	 * @param id
	 * @param bValue
	 */
	private void setFuncValue(int id, boolean bValue) {
		switch (id) {
		case R.string.funcPushNotice:
			SettingUtils.savePush(context, bValue);
			// 关闭推送服务
			break;
		case R.string.funcMoblieNetNotice:
			SettingUtils.savePlayOrDownloadByMoblie(context, bValue);
			break;
		}
	}

	class ViewHolder {
		TextView tvwFuncNotice;
		AlwaysMarqueeTextView tvwFuncDesc;
		ToggleButton btnSwitch;
	}
}
