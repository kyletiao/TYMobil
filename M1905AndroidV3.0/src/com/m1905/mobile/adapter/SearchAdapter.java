package com.m1905.mobile.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.lidroid.xutils.BitmapUtils;
import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.dao.SearchContent;
import com.m1905.mobile.util.AppUtils;
import com.m1905.mobile.util.StringUtils;

public class SearchAdapter extends BaseAdapter {
	private Context context;
	private List<SearchContent> lstSearch;
	private BitmapUtils bitmapUtil;

	public BitmapUtils getBitmapUtil() {
		return bitmapUtil;
	}

	public SearchAdapter(Context context, List<SearchContent> lstSearch) {
		this.context = context;
		this.lstSearch = lstSearch;
		bitmapUtil = new BitmapUtils(context, AppConfig.M1905_CACHE_PATH);
	}

	@Override
	public int getCount() {
		return lstSearch.size();
	}

	@Override
	public Object getItem(int position) {
		return lstSearch.get(position);
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
					R.layout.search_item, null);
			holder = new ViewHolder();
			holder.ivwVideoImg = (ImageView) convertView
					.findViewById(R.id.ivwVideoImg);
			holder.ivwVideoImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AppUtils.toastShowMsg(context, holder.tvwVideoName
							.getText().toString());
				}
			});
			holder.ivwVIPLogo = (ImageView) convertView
					.findViewById(R.id.ivwVIPLogo);
			holder.tvwVideoName = (TextView) convertView
					.findViewById(R.id.tvwVideoName);
			holder.tvwVideoScore = (TextView) convertView
					.findViewById(R.id.tvwVideoScore);
			holder.tvwVideoDirector = (TextView) convertView
					.findViewById(R.id.tvwVideoDirector);
			holder.tvwVideoActor = (TextView) convertView
					.findViewById(R.id.tvwVideoActor);
			holder.tvwVideoContent = (TextView) convertView
					.findViewById(R.id.tvwVideoContent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 封装数据
		SearchContent search = (SearchContent) getItem(position);
		if (search.getType() != 7
				|| (Identify.currentUser != null && Identify.currentUser
						.isM1905VIP())) {
			holder.ivwVIPLogo.setVisibility(View.GONE);
		} else {
			holder.ivwVIPLogo.setVisibility(View.VISIBLE);
		}
		getBitmapUtil().display(holder.ivwVideoImg, search.getImg());
		String unknown = this.context.getString(R.string.default_unknown);
		String title = search.getTitle();
		title = StringUtils.isEmpty(title) ? unknown : title;
		holder.tvwVideoName.setText(title);
		holder.tvwVideoScore
				.setText(StringUtils.formatScore(search.getSorce()));
		String director = search.getDircotor();
		director = StringUtils.isEmpty(director) ? unknown : director;
		holder.tvwVideoDirector.setText(director);
		String actor = search.getActor();
		actor = StringUtils.isEmpty(actor) ? unknown : actor;
		holder.tvwVideoActor.setText(actor);
		String desc = search.getDescription();
		desc = StringUtils.isEmpty(desc) ? unknown : desc;
		holder.tvwVideoContent.setText(desc);
		return convertView;
	}
	class ViewHolder {
		ImageView ivwVideoImg;
		ImageView ivwVIPLogo;
		TextView tvwVideoName;
		TextView tvwVideoScore;
		TextView tvwVideoDirector;
		TextView tvwVideoActor;
		TextView tvwVideoContent;
	}
}
