package com.m1905.mobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.util.SettingUtils;

public class ResolutionAct extends Activity {
	private ListView lvwFilmResolution;
	private ResoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_resoultion);
		findViewById(R.id.btnFunc).setVisibility(View.GONE);
		((TextView) findViewById(R.id.tvwNaviNotice))
				.setText(R.string.func_setting);
		lvwFilmResolution = (ListView) findViewById(R.id.lvwFilmResolution);
		adapter = new ResoAdapter();
		lvwFilmResolution.setAdapter(adapter);
		lvwFilmResolution.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SettingUtils
						.saveUseFilmResolution(ResolutionAct.this, position);
				adapter.notifyDataSetChanged();
				finish();
			}
		});

	}

	/**
	 * 返回按钮操作
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	class ViewHolder {
		TextView tvwResoValue;
		ImageView ivwSelected;
	}

	class ResoAdapter extends BaseAdapter {
		String[] film_reso = null;

		public ResoAdapter() {
			film_reso = getResources().getStringArray(R.array.film_resolution);
		}

		@Override
		public int getCount() {
			return film_reso.length;
		}

		@Override
		public Object getItem(int position) {
			return film_reso[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(ResolutionAct.this).inflate(
						R.layout.resolution_item, null);
				holder = new ViewHolder();
				holder.ivwSelected = (ImageView) convertView
						.findViewById(R.id.ivwSelected);
				holder.tvwResoValue = (TextView) convertView
						.findViewById(R.id.tvwResoValue);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvwResoValue.setText(getItem(position).toString());
			if (SettingUtils.loadUseFilmResolution(ResolutionAct.this) == position) {
				holder.ivwSelected.setVisibility(View.VISIBLE);
			} else {
				holder.ivwSelected.setVisibility(View.GONE);
			}
			return convertView;
		}

	}

}
