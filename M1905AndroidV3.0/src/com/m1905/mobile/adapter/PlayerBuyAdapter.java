package com.m1905.mobile.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.dao.Buy;

/**
 * Created by m1905 on 14-1-25.
 */
public class PlayerBuyAdapter extends BaseAdapter {

    ArrayList<Buy> buys;
    Context context;
    LayoutInflater inflater;

    boolean enabled = false;

    public PlayerBuyAdapter(Context context, ArrayList<Buy> buys) {
        this.context = context;
        this.buys = buys;
        inflater = LayoutInflater.from(context);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyDataSetChanged();
    }

    @Override
    public boolean isEnabled(int position) {
        return enabled;
    }

    @Override
    public int getCount() {
        return buys.size();
    }

    @Override
    public Object getItem(int position) {
        return buys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.player_buy_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ivwIcon = (ImageView) convertView.findViewById(R.id.ivwIcon);
            viewHolder.tvwTitle = (TextView) convertView.findViewById(R.id.tvwTitle);
            viewHolder.tvwSubTitle = (TextView) convertView.findViewById(R.id.tvwSubTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Buy buy = buys.get(position);
        viewHolder.ivwIcon.setImageResource(buy.getImgId());
        viewHolder.ivwIcon.setEnabled(enabled);
        viewHolder.tvwTitle.setText(buy.getTitle());
        viewHolder.tvwSubTitle.setText(buy.getSubTitle());
        if(enabled){
            // 根据图片的id来设置可用时字体的颜色

        } else {
            viewHolder.tvwTitle.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }


    class ViewHolder {
        ImageView ivwIcon;
        TextView tvwTitle, tvwSubTitle;
    }
}
