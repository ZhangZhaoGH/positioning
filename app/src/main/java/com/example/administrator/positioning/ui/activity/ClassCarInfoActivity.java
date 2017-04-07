package com.example.administrator.positioning.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.administrator.positioning.R;
import com.example.administrator.positioning.api.ApiConstants;
import com.example.administrator.positioning.base.BaseActivity;
import com.example.administrator.positioning.bean.CarRouteBean;
import com.example.administrator.positioning.bean.ClassCarSiteBean;
import com.example.administrator.positioning.bean.DriverInfoBean;
import com.example.administrator.positioning.bean.SchoolCarLocationBean;
import com.example.administrator.positioning.ui.adapter.BusRouteDetailsAdapter;
import com.example.administrator.positioning.ui.view.ActionSheetDialog;
import com.example.administrator.positioning.ui.view.MyRouteOverlay;
import com.example.administrator.positioning.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class ClassCarInfoActivity extends BaseActivity implements LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, AMap.OnMapLoadedListener{
    private DriverInfoBean bean;
    private TextView title,carNum,drive,route,title_right,site,map;
    private Toolbar mToolbar;
    private Context mContext;
    private List<String> carNumList;
    private ArrayList<CarRouteBean> carRouteBeanList;
    private LinearLayout llRoute;
    private CarRouteBean routeBean;
    private ListView listView;
    private MapView mapView;
    private List<ClassCarSiteBean> mList;
    private BusRouteDetailsAdapter adapter;
    private AMap aMap;
    private UiSettings uiSettings;
    private RouteSearch routeSearch;
    private LatLonPoint mStartPoint;//起点
    private LatLonPoint mEndPoint;//终点
    private List<LatLonPoint> coordList = new ArrayList<>();
    private LatLonPoint coord;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Marker sc_location;
    private List<Marker> carLocationList = new ArrayList<>();
    private List<SchoolCarLocationBean> scl_list = new ArrayList<>();
    private DriveRouteResult mDriveRouteResult;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            getSchoolCarLocation();
            handler.sendEmptyMessageDelayed(1, 10000);
            super.handleMessage(msg);
        }
    };

    @Override
    public void doBusiness(Context mContext) {
        getCarRoute();
        getCarNum();
    }

    private void setMap() {
        uiSettings = aMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        routeSearch.setRouteSearchListener(this);
        ClassCarSiteBean startBean = mList.get(0);
        ClassCarSiteBean endBean = mList.get(mList.size() - 1);
        mStartPoint = new LatLonPoint(Double.parseDouble(startBean.getLat()), Double.parseDouble(startBean.getLon()));
        mEndPoint = new LatLonPoint(Double.parseDouble(endBean.getLat()), Double.parseDouble(endBean.getLon()));
        for (int i = 1; i < mList.size() - 1; i++) {
            ClassCarSiteBean ClassCarSiteBean = mList.get(i);
            coord = new LatLonPoint(Double.parseDouble(ClassCarSiteBean.getLat()), Double.parseDouble(ClassCarSiteBean.getLon()));
            coordList.add(coord);
        }
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMapLoadedListener(this);
    }

    private void getCarRoute() {
        String url=ApiConstants.getCarRoute(bean.getJxid());
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
                            carRouteBeanList.add(bean);
                        }
                        if (carRouteBeanList.size()>0){
                            route.setText(carRouteBeanList.get(0).getRoutename());
                            getBusRouteStation(carRouteBeanList.get(0).getRouteid());
                        }else {
                            route.setText("暂无路线");
                        }
                    }else {
                        ToastUtils.showToast("获取班车路线出错，请重试！");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void getCarNum() {
        String url= ApiConstants.getCarNum(bean.getJxid());
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToast("获取车号数据失败，请重试！");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    LogUtil("车号response="+response);
                    JSONObject jsonObject= JSON.parseObject(response);
                    JSONObject head=jsonObject.getJSONObject("head");
                    String issuccess=head.getString("issuccess");
                    if ("false".equals(issuccess)){
                        ToastUtils.showToast("获取车号数据失败，请重试！");
                        return;
                    }
                    JSONArray body=jsonObject.getJSONArray("body");
                    for (int i = 0; i < body.size(); i++) {
                        JSONObject jsonObject1=body.getJSONObject(i);
                        String carid=jsonObject1.getString("carid");
                        carNumList.add(carid);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    ToastUtils.showToast("获取车号数据失败，请重试！");
                }

            }
        });
    }

    @Override
    public void initParms(Bundle parms) {
        if (parms!=null){
            bean= (DriverInfoBean) parms.getSerializable("bean");
        }else {
            ToastUtils.showToast("获取数据失败");
        }
    }

    @Override
    public void setInstanceState(Bundle savedInstanceState) {
        mapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        routeSearch = new RouteSearch(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_class_car_info;
    }

    @Override
    public void setPresenter() {

    }

    @Override
    public void initView(View view) {
        mContext=this;
        mList=new ArrayList<>();
        carNumList=new ArrayList<>();
        carRouteBeanList=new ArrayList<>();
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
        title_right= (TextView) view.findViewById(R.id.toolbar_right);
        title_right.setVisibility(View.VISIBLE);
        carNum= (TextView) view.findViewById(R.id.tv_cci_car_num);
        drive= (TextView) view.findViewById(R.id.tv_cci_drive);
        route= (TextView) view.findViewById(R.id.tv_cci_route);
        map= (TextView) view.findViewById(R.id.tv_cci_map);
        site= (TextView) view.findViewById(R.id.tv_cci_site);
        title_right.setText("退出");
        title.setText(bean.getJxname());
        carNum.setText(bean.getCarid());
        drive.setText(bean.getName()+"( "+bean.getMobile()+" )");
        site.setOnClickListener(this);
        map.setOnClickListener(this);
        carNum.setOnClickListener(this);
        llRoute= (LinearLayout) view.findViewById(R.id.ll_cci_route);
        llRoute.setOnClickListener(this);
        listView = (ListView) view.findViewById(R.id.lv_bus_route);
    }


    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.tv_cci_map:
                //显示地图
                map.setTextColor(ContextCompat.getColor(mContext,R.color.title_bg));
                site.setTextColor(ContextCompat.getColor(mContext,R.color.color_999999));
                mapView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                break;
            case R.id.tv_cci_site:
                //在下面显示站台
                map.setTextColor(ContextCompat.getColor(mContext,R.color.color_999999));
                site.setTextColor(ContextCompat.getColor(mContext,R.color.title_bg));
                mapView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_cci_car_num:
                showCarNum();
                break;
            case R.id.ll_cci_route:
                //跳转路线
                jumpRoute();
                break;
            default:
                break;
        }
    }

    private void jumpRoute() {
        Intent intent=new Intent(mContext,SchoolCarRouteDetailsActivity.class);
        intent.putExtra("jxid",bean.getJxid());
        startActivityForResult(intent,33);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==33){
            if (data!=null){
                 routeBean= (CarRouteBean) data.getSerializableExtra("bean");
                route.setText(routeBean.getRoutename());
                LogUtil("选中回退"+routeBean.toString());
                //刷新地图
                //刷新站点
                getBusRouteStation(routeBean.getRouteid());
                //更新路线接口
                updateDriverCar(bean.getUid(),routeBean.getRouteid());


            }
        }
    }

    private void updateDriverCar(String uid, String routeid) {
        String url=ApiConstants.getUpdateDriverCar(uid, routeid);
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToast("更新路线失败，请重试！");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    LogUtil("更新路线response="+response);
                    JSONObject jsonObject= JSON.parseObject(response);
                    JSONObject head=jsonObject.getJSONObject("head");
                    String issuccess=head.getString("issuccess");
                    if ("false".equals(issuccess)){
                        ToastUtils.showToast("更新路线失败，请重试！");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void showCarNum() {
        ActionSheetDialog a = new ActionSheetDialog(mContext).builder().setCancelable(false).setCanceledOnTouchOutside(false);
        for (int i = 0; i < carNumList.size(); i++) {
            a.addSheetItem(carNumList.get(i), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                @Override
                public void onClick(int which) {
                    String car = carNumList.get(which-1);
                    carNum.setText(car);
                    updateCarNum(car);
                }
            });
        }
        a.show();
    }

    private void updateCarNum(String car) {
        String url=ApiConstants.getUpdateCarNum(bean.getUid(),car);
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToast("更新车号失败，请重试！");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    LogUtil("更新车号response="+response);
                    JSONObject jsonObject= JSON.parseObject(response);
                    JSONObject head=jsonObject.getJSONObject("head");
                    String issuccess=head.getString("issuccess");
                    if ("false".equals(issuccess)){
                        ToastUtils.showToast("更新车号失败，请重试！");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void getBusRouteStation(String routeId) {
        String url= ApiConstants.getCarSite(routeId);
        mList.clear();
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToast("获取站台信息失败，请重试！");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    LogUtil("获取站台response="+response);
                    JSONObject object = JSON.parseObject(response);
                    if (object.getJSONObject("head").getString("stateinfo").equals("有数据")) {
                        JSONArray body = JSON.parseArray(object.getString("body"));
                        ClassCarSiteBean bean;
                        for (int i = 0; i < body.size(); i++) {
                            bean = JSON.parseObject(body.getJSONObject(i).toString(), ClassCarSiteBean.class);
                            mList.add(bean);
                        }
                        if (adapter==null){
                            adapter = new BusRouteDetailsAdapter(mContext, mList);
                            listView.setAdapter(adapter);
                        }else {
                            adapter.notifyDataSetChanged();
                        }

                        setMap();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view;
        if (marker.equals(sc_location)) {
            view = View.inflate(mContext, R.layout.map_school_location_layout, null);
            renderLocation(marker, view);
        } else {
            view = View.inflate(mContext, R.layout.map_station_layout, null);
            render(marker, view);
        }
        return view;
    }

    private void renderLocation(Marker marker, View view) {
        SchoolCarLocationBean scl_bean = null;
        for (int i = 0; i < carLocationList.size(); i++) {
            if (marker.equals(carLocationList.get(i))){
                scl_bean =  scl_list.get(i);
                break;
            }
        }
        TextView name = (TextView) view.findViewById(R.id.tv_car_id);
        name.setText(scl_bean.getCarcode());
        TextView driver = (TextView) view.findViewById(R.id.tv_driver);
        driver.setText(scl_bean.getDrive() + " " + scl_bean.getTel());
        ImageView call = (ImageView) view.findViewById(R.id.iv_call_dirver);
        final SchoolCarLocationBean finalScl_bean = scl_bean;
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + finalScl_bean.getTel()));
                startActivity(intent);
            }
        });
    }

    private void render(Marker marker, View view) {
        TextView name = (TextView) view.findViewById(R.id.tv_station_name);
        name.setText(marker.getTitle());
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onMapLoaded() {
        searchRouteResult();
    }

    private void searchRouteResult() {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, coordList,
                null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除

            mlocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        getSchoolCarLocation();
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    MyRouteOverlay myRouteOverlay = new MyRouteOverlay(
                            mContext, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), coordList);
                    myRouteOverlay.setRouteList(mList);
                    myRouteOverlay.setRouteWidth(10);
                    myRouteOverlay.getRouteWidth();
                    myRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    myRouteOverlay.setIsColorfulline(false);
                    myRouteOverlay.setThroughPointIconVisibility(true);
                    myRouteOverlay.addToMap();
                    myRouteOverlay.zoomToSpan();
                    myRouteOverlay.showMarker(getIntent().getIntExtra("index", -1));
                } else if (result != null && result.getPaths() == null) {
                    ToastUtils.showToast("路线获取失败");
                }

            } else {
                ToastUtils.showToast("路线获取失败");
            }
            handler.sendEmptyMessageDelayed(1, 10000);
        } else {
            Log.e("aMap", "errorCode" + errorCode);
        }
    }

    private void getSchoolCarLocation() {
        if (sc_location != null) {
            sc_location.destroy();
        }
        String url=ApiConstants.getCarPos(bean.getJxid());
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject object = JSON.parseObject(response);
                    if (object.getJSONObject("head").getString("stateinfo").equals("有数据")) {
                        JSONArray body = JSON.parseArray(object.getString("body"));
                        SchoolCarLocationBean locationbean;
                        for (int i = 0; i < body.size(); i++) {
                            locationbean = JSON.parseObject(body.getJSONObject(i).toString(), SchoolCarLocationBean.class);
                            scl_list.add(locationbean);
                            LatLng latLng = new LatLng(Double.parseDouble(locationbean.getLat()), Double.parseDouble(locationbean.getLon()));
                            MarkerOptions options = new MarkerOptions();
                            options.position(latLng).title("").icon(BitmapDescriptorFactory.fromResource(R.mipmap.banche));
                            sc_location = aMap.addMarker(options);
                            carLocationList.add(sc_location);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
