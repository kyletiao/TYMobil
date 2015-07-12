package com.m1905.mobile.adapter;

import org.apache.commons.lang.StringUtils;

import com.dianxin.mobilefree.R;
import com.lidroid.xutils.BitmapUtils;
import com.m1905.mobile.bean.TopicsBean;
import com.m1905.mobile.common.AppConfig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TopicsAdapter extends BaseAdapter {
	private Context context;
	private TopicsBean bean;
	private BitmapUtils bitmapUtil;
	
	public TopicsAdapter(Context context,TopicsBean bean){
		this.context = context;
		this.bean = bean;
		System.out.println("专题个数"+bean.getData().length);
		bitmapUtil = new BitmapUtils(context,AppConfig.M1905_CACHE_PATH);
	}
	
	@Override
	public int getCount() {
		return bean.getData().length;
	}

	@Override
	public Object getItem(int position) {
		return bean.getData()[position]; 
	}

	@Override
	public long getItemId(int position) {
		return position; 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("当前个数："+position);
		final ViewHolder holder;
		String unknown = this.context.getString(R.string.default_unknown); 
		if(convertView ==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.topics_item, null);
			holder = new ViewHolder();
			holder.tv_topics_title = (TextView) convertView.findViewById(R.id.tv_topics_title);
			holder.tv_topics_description = (TextView) convertView.findViewById(R.id.tv_topics_description);
			holder.iv_topics = (ImageView) convertView.findViewById(R.id.iv_topics);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		System.out.println("添加内容："+bean.getData()[position].getTitle());
		String title = bean.getData()[position].getTitle();
		title = StringUtils.isEmpty(title)? unknown:title;
		holder.tv_topics_title.setText(title);
		String desc = bean.getData()[position].getDescription();
		desc = StringUtils.isEmpty(desc)? unknown:desc;
		holder.tv_topics_description.setText(desc);
		bitmapUtil.display(holder.iv_topics, "http://img3.tv189.cn/"+bean.getData()[position].getCover());
		return convertView;
	}
	
	class ViewHolder {
		ImageView iv_topics;
		TextView tv_topics_title;
		TextView tv_topics_description;
	}

}
