package com.m1905.mobile.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.activity.M1905VideoPlayerActivity;
import com.m1905.mobile.common.FavouriteContent;

/**
 * Created by Eric on 14-1-22.
 */
public class FavouriteAdapter extends BaseAdapter {

    Context context;
    List<FavouriteContent> lstFavourites;
    LayoutInflater inflater;
    boolean isDeleting = false;

    public FavouriteAdapter(Context context, List<FavouriteContent> lstFavourites){
        this.context = context;
        this.lstFavourites = lstFavourites;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lstFavourites.size();
    }

    @Override
    public FavouriteContent getItem(int position) {
        return lstFavourites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public boolean isDeleting() {
        return isDeleting;
    }

    public void setDeleting(boolean isDeleting) {
        this.isDeleting = isDeleting;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.favourite_item, null);
            viewHolder = new ViewHolder();
            viewHolder.chkItem = (CheckBox) convertView.findViewById(R.id.chkItem);
            viewHolder.ivwVip = (ImageView) convertView.findViewById(R.id.ivwVip);
            viewHolder.tvwTitle = (TextView) convertView.findViewById(R.id.tvwTitle);
            viewHolder.imgBtnPlay = (ImageButton) convertView.findViewById(R.id.imgBtnPlay);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final FavouriteContent favouriteContent = lstFavourites.get(position);
        if(isDeleting){
            viewHolder.chkItem.setVisibility(View.VISIBLE);
            viewHolder.imgBtnPlay.setVisibility(View.GONE);
        } else {
            viewHolder.chkItem.setVisibility(View.GONE);
            viewHolder.imgBtnPlay.setVisibility(View.VISIBLE);
        }
        viewHolder.chkItem.setChecked(favouriteContent.isChecked());
        viewHolder.tvwTitle.setText(favouriteContent.getTitle());
        viewHolder.imgBtnPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						M1905VideoPlayerActivity.class);
				intent.putExtra("id", Integer.parseInt(favouriteContent.getId()));
				intent.putExtra("type", -1);
				context.startActivity(intent);
			}
        });
        return convertView;
    }

    class ViewHolder {
        CheckBox chkItem;
        ImageView ivwVip;
        TextView tvwTitle;
        ImageButton imgBtnPlay;
    }

}
