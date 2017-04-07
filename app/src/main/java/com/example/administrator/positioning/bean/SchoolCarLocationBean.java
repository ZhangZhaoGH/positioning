package com.example.administrator.positioning.bean;

/**
 * Created by Administrator on 2017/4/7.
 */

public class SchoolCarLocationBean {
    //车牌号
    private String carcode;
    //司机
    private String drive;
    //纬度
    private String lat;
    //经度
    private String lon;
    //电话
    private String tel;

    @Override
    public String toString() {
        return "SchoolCarLocationBean{" +
                "carcode='" + carcode + '\'' +
                ", drive='" + drive + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }

    public String getCarcode() {
        return carcode;
    }

    public void setCarcode(String carcode) {
        this.carcode = carcode;
    }

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
