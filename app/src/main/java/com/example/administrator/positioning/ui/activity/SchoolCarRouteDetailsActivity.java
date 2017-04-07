package com.example.administrator.positioning.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.positioning.R;
import com.example.administrator.positioning.api.ApiConstants;
import com.example.administrator.positioning.base.BaseActivity;
import com.example.administrator.positioning.bean.CarRouteBean;
import com.example.administrator.positioning.bean.DriverInfoBean;
import com.example.administrator.positioning.ui.adapter.AllLineAdapter;
import com.example.administrator.positioning.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class SchoolCarRouteDetailsActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private TextView title;
    private Toolbar mToolbar;
    private Context mContext;
    private List<CarRouteBean> carRouteList;
    private ListView near_station_list;
    @Override
    public void doBusiness(Context mContext) {
        getCarRoute();
    }

    private void getCarRoute() {
        String jxid=getIntent().getStringExtra("jxid");
        String url= ApiConstants.getCarRoute(jxid);
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToast("获取班车路线出错，请重试！");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    LogUtil("班车路线response="+response);
                    JSONObject object = JSON.parseObject(response);
                    if (object.getJSONObject("head").getString("stateinfo").equals("有数据")) {
                        JSONArray body = JSON.parseArray(object.getString("body"));
                        CarRouteBean bean;
                        for (int i = 0; i < body.size(); i++) {
                            bean = JSON.parseObject(body.getJSONObject(i).toString(), CarRouteBean.class);
                            carRouteList.add(bean);
                        }
                        AllLineAdapter adapter=new AllLineAdapter(mContext,carRouteList);
                        near_station_list.setAdapter(adapter);

                    }else {
                        ToastUtils.showToast("获取班车路线出错，请重试！");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void setInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_school_car_route_details;
    }

    @Override
    public void setPresenter() {

    }

    @Override
    public void initView(View view) {
        mContext=this;
        mToolbar= (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title= (TextView) view.findViewById(R.id.tv_toolbar_title);
        title.setText("班车路线");
        near_station_list = (ListView) view.findViewById(R.id.near_station_list);
        near_station_list.setOnItemClickListener(this);
        carRouteList=new ArrayList<>();
    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CarRouteBean bean = carRouteList.get(i);
        Intent intent=new Intent();
        intent.putExtra("bean",bean);
        setResult(33,intent);
        finish();
    }
}
