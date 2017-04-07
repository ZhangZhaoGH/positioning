package com.example.administrator.positioning.api;

/**
 * Created by zz on 2017/4/7.
 */

public class ApiConstants {

    private static final String URL="http://xy.1039.net:12345/drivingServcie/rest/driving_json/jjxc.ashx?";


    public static String getLoginUrl(String uid,String pwd) {
        return URL+"methodName=JJApp&prc=prc_app_driverlogin&parms=uid="+uid+"|pwd="+pwd+"&remark=test&sign=2E394E3900D5EA6A6BAC686B849A94B0";
    }
}
