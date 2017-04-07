package com.example.administrator.positioning.bean;

import java.io.Serializable;

/**
 * Created by zz on 2017/4/7.
 */

public class CarRouteBean implements Serializable{
//    "carcode": "0",
//            "carimage": "&nbsp;",
//            "driver": "刘世国",
//            "endsite": "北京西站",
//            "endtime": "09:50,13:50,16:50",
//            "routeid": "3DE2532A-D1C3-47BA-B92C-B0EEEF3CD56E",
//            "routename": "北京西站专线(共5站)",
//            "startsite": "七侠镇第一站(老北京烤鸭门口)",
//            "starttime": "08:00,12:00,15:00",
//            "tel": "13012345678"
    private String carcode;
    private String carimage;
    private String driver;
    private String endsite;
    private String endtime;
    private String routeid;
    private String routename;
    private String startsite;
    private String starttime;

    @Override
    public String toString() {
        return "CarRouteBean{" +
                "carcode='" + carcode + '\'' +
                ", carimage='" + carimage + '\'' +
                ", driver='" + driver + '\'' +
                ", endsite='" + endsite + '\'' +
                ", endtime='" + endtime + '\'' +
                ", routeid='" + routeid + '\'' +
                ", routename='" + routename + '\'' +
                ", startsite='" + startsite + '\'' +
                ", starttime='" + starttime + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCarcode() {
        return carcode;
    }

    public void setCarcode(String carcode) {
        this.carcode = carcode;
    }

    public String getCarimage() {
        return carimage;
    }

    public void setCarimage(String carimage) {
        this.carimage = carimage;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getEndsite() {
        return endsite;
    }

    public void setEndsite(String endsite) {
        this.endsite = endsite;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getRouteid() {
        return routeid;
    }

    public void setRouteid(String routeid) {
        this.routeid = routeid;
    }

    public String getRoutename() {
        return routename;
    }

    public void setRoutename(String routename) {
        this.routename = routename;
    }

    public String getStartsite() {
        return startsite;
    }

    public void setStartsite(String startsite) {
        this.startsite = startsite;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    private String tel;

}
