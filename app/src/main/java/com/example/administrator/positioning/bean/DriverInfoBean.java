package com.example.administrator.positioning.bean;

import java.io.Serializable;

/**
 * Created by zz on 2017/4/7.
 * 司机详情
 */

public class DriverInfoBean implements Serializable {
//    "carid": "京A1234",
//            "jxid": "00012",
//            "jxname": "北京时代风帆驾校",
//            "mobile": "13012345678",
//            "name": "刘世国",
//            "uid": "12345678910"

    private String carid;
    private String jxid;
    private String jxname;
    private String mobile;
    private String name;
    private String uid;

    @Override
    public String toString() {
        return "DriverInfoBean{" +
                "carid='" + carid + '\'' +
                ", jxid='" + jxid + '\'' +
                ", jxname='" + jxname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getJxid() {
        return jxid;
    }

    public void setJxid(String jxid) {
        this.jxid = jxid;
    }

    public String getJxname() {
        return jxname;
    }

    public void setJxname(String jxname) {
        this.jxname = jxname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
