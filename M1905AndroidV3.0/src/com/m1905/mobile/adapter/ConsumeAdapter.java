package com.m1905.mobile.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.bean.PayContentBean;
import com.m1905.mobile.bean.PayContentBean.Info;
import com.m1905.mobile.dao.Consume;

/**
 * Created by Eric on 14-1-25.
 */
@SuppressLint("SimpleDateFormat")
public class ConsumeAdapter extends BaseAdapter {

    Context context;
    PayContentBean consumes;
    LayoutInflater inflater;

    public ConsumeAdapter(Context context,PayContentBean consumes) {
        this.context = context;
        this.consumes = consumes;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return consumes.getInfo().length;
    }

    @Override
    public Info getItem(int position) {
        return consumes.getInfo()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.consume_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvwTitle = (TextView) convertView.findViewById(R.id.tvwTitle);
            viewHolder.tvwTime = (TextView) convertView.findViewById(R.id.tvwTime);
            viewHolder.tvwPrice = (TextView) convertView.findViewById(R.id.tvwPrice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Info consume = consumes.getInfo()[position];
        viewHolder.tvwTitle.setText(consume.getProductName());
        viewHolder.tvwTime.setText(getDate(consume.getPstart()));
        //viewHolder.tvwTime.setText("");
        viewHolder.tvwPrice.setText(String.valueOf((Integer.parseInt(consume.getFee())/100)) + "元");

        return convertView;
    }

    class ViewHolder {
        TextView tvwTitle;
        TextView tvwTime;
        TextView tvwPrice;
    }
    
    public String getDate(String unixDate) {
    	  
    	   SimpleDateFormat fm1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	   SimpleDateFormat fm2 = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
    	   long unixLong = 0;
    	   String date = "";
    	   try {
    	   unixLong = Long.parseLong(unixDate);
    	   } catch(Exception ex) {
    	   System.out.println("String转换Long错误，请确认数据可以转换！");
    	   }
    	   try {
    	   date = fm1.format(unixLong);
    	   date = fm2.format(new Date(date));
    	   } catch(Exception ex) {
    	   System.out.println("String转换Date错误，请确认数据可以转换！");
    	   }
    	   System.out.println(date);
    	   return date;
    	   }
}
