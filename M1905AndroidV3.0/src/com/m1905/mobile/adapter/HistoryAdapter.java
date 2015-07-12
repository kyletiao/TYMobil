package com.m1905.mobile.adapter;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.activity.M1905VideoPlayerActivity;
import com.m1905.mobile.activity.RecommentAct;
import com.m1905.mobile.common.HistoryContent;

/**
 * Created by Eric on 14-1-21.
 */
public class HistoryAdapter extends BaseAdapter {

    boolean isDeleting = false;
    Context context;
    List<HistoryContent> lstHistories;
    LayoutInflater inflater;

    public HistoryAdapter(Context context, List<HistoryContent> lstHistories) {
        this.context = context;
        this.lstHistories = lstHistories;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lstHistories.size();
    }

    @Override
    public Object getItem(int position) {
        return lstHistories.get(position);
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.history_item, null);
            viewHolder = new ViewHolder();
            viewHolder.chkItem = (CheckBox) convertView.findViewById(R.id.chkItem);
            viewHolder.tvwTitle = (TextView) convertView.findViewById(R.id.tvwTitle);
            viewHolder.tvwSubTitle = (TextView) convertView.findViewById(R.id.tvwSubTitle);
            viewHolder.imgBtnPlay = (ImageButton) convertView.findViewById(R.id.imgBtnPlay);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final HistoryContent history = lstHistories.get(position);
        if(isDeleting){
            viewHolder.chkItem.setVisibility(View.VISIBLE);
            viewHolder.imgBtnPlay.setVisibility(View.GONE);
        } else {
            viewHolder.chkItem.setVisibility(View.GONE);
            viewHolder.imgBtnPlay.setVisibility(View.VISIBLE);
        }
        viewHolder.chkItem.setChecked(history.isChecked());
        viewHolder.tvwTitle.setText(history.getTitle());
        
        int time = history.getWatchTime()-TimeZone.getDefault().getRawOffset();
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String hms = formatter.format(date);
        viewHolder.tvwSubTitle.setText("播放至"+hms);
        viewHolder.imgBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 播放视频
            	Intent intent = new Intent(context,
						M1905VideoPlayerActivity.class);
				intent.putExtra("id", Integer.parseInt(history.getId()));
				intent.putExtra("type", -1);
				intent.putExtra("time", history.getWatchTime());
				context.startActivity(intent);
            	System.out.println("当前点击"+history.getTitle()+"--id:"+history.getId());
            }
        });

        return convertView;
    }

    class ViewHolder {
        CheckBox chkItem;
        TextView tvwTitle;
        TextView tvwSubTitle;
        ImageButton imgBtnPlay;
    }
}
