package com.example.administrator.positioning.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.positioning.R;
import com.example.administrator.positioning.bean.ClassCarSiteBean;

import java.util.List;

/**
 * Created by Sherlock on 2016/12/23.
 */

public class BusRouteDetailsAdapter extends BaseAdapter {
    private Context mContext;
    private List<ClassCarSiteBean> mlist;
    private ClassCarSiteBean ClassCarSiteBean;

    public BusRouteDetailsAdapter(Context context, List<ClassCarSiteBean> list) {
        mContext = context;
        mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ClassCarSiteBean = mlist.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_bus_route_station, null);
            viewHolder.number = (TextView) convertView.findViewById(R.id.tv_station_number);
            viewHolder.station_name = (TextView) convertView.findViewById(R.id.tv_station_name);
            viewHolder.station_time = (TextView) convertView.findViewById(R.id.station_time);
            viewHolder.station_bg = (ImageView) convertView.findViewById(R.id.iv_station_bg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.number.setText(String.valueOf(position + 1));
        viewHolder.station_name.setText(ClassCarSiteBean.getSitename());
        viewHolder.station_time.setText(ClassCarSiteBean.getTimes().replace(",", "   "));
        if (position==0){
            viewHolder.station_bg.setBackgroundResource(R.drawable.station_bg_up);
        }else if (position==mlist.size()-1){
            viewHolder.station_bg.setBackgroundResource(R.drawable.station_bg_down);
        }else {
            viewHolder.station_bg.setBackgroundResource(R.drawable.station_bg_center);
        }
        return convertView;
    }

    private static class ViewHolder {
        private TextView number, station_name, station_time;
        private ImageView station_bg;
    }
}
