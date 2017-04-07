package com.example.administrator.positioning.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.positioning.R;
import com.example.administrator.positioning.bean.CarRouteBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sherlock on 2016/12/28.
 */

public class AllLineAdapter extends BaseAdapter {
    private Context mcontext;
    private List<CarRouteBean> list;
    private CarRouteBean bean;

    public AllLineAdapter(Context context, List<CarRouteBean> mlist) {
        this.mcontext = context;
        this.list = mlist;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        bean = list.get(position);
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.item_class_car,null);
            viewHolder.line_name = (TextView) convertView.findViewById(R.id.tv_line_name);
            viewHolder.start_name = (TextView) convertView.findViewById(R.id.tv_start_name);
            viewHolder.start_time = (TextView) convertView.findViewById(R.id.tv_start_time);
            viewHolder.end_name = (TextView) convertView.findViewById(R.id.tv_end_name);
            viewHolder.end_time = (TextView) convertView.findViewById(R.id.tv_end_time);
            viewHolder.station_image = (ImageView) convertView.findViewById(R.id.iv_station_image);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.station_image.setImageResource(R.mipmap.banche);
        viewHolder.line_name.setText(bean.getRoutename());
        viewHolder.start_name.setText(bean.getStartsite());
        viewHolder.start_time.setText(bean.getStarttime().replace(",","/"));
        viewHolder.end_name.setText(bean.getEndsite());
        viewHolder.end_time.setText(bean.getEndtime().replace(",","/"));
        return convertView;
    }
    private   class ViewHolder{
        @SuppressLint("StaticFieldLeak")
        TextView line_name,start_name,end_name,start_time,end_time;
        @SuppressLint("StaticFieldLeak")
        ImageView station_image;
    }

}
