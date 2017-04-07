package com.example.administrator.positioning.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/7.
 */

public class ClassCarSiteBean implements Serializable{
    //    "lat": "39.9037198085",
//            "lon": "116.4232939323",
//            "remark": "西客站旁边",
//            "sitename": "七侠镇第一站(老北京烤鸭门口)",
//            "times": "08:00,12:00,15:00"
//纬度
    private String lat;
    //经度
    private String lon;
    //备注
    private String remark;
    //名称
    private String sitename;
    //时间
    private String times;

    @Override
    public String toString() {
        return "ClassCarSiteBean{" +
                "lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", remark='" + remark + '\'' +
                ", sitename='" + sitename + '\'' +
                ", times='" + times + '\'' +
                '}';
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
