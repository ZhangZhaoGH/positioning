package com.example.administrator.positioning.api;

/**
 * Created by zz on 2017/4/7.
 */

public class ApiConstants {

    private static final String URL="http://xy.1039.net:12345/drivingServcie/rest/driving_json/jjxc.ashx?";


    public static String getLoginUrl(String uid,String pwd) {
        return URL+"methodName=JJApp&prc=prc_app_driverlogin&parms=uid="+uid+"|pwd="+pwd+"&remark=test&sign=2E394E3900D5EA6A6BAC686B849A94B0";
    }

    public static String getCarNum(String jxid) {
        return URL+"methodName=JJApp&prc=prc_app_getbuslist&parms=jxid="+jxid+"&remark=test&sign=2E394E3900D5EA6A6BAC686B849A94B0";
    }

    public static String getUpdateCarNum(String uid,String carid) {
        return URL+"methodName=JJApp&prc=prc_app_updatedrivercar&parms=uid="+uid+"|carid="+carid+"|rid=0&remark=test&sign=2E394E3900D5EA6A6BAC686B849A94B0";
    }

    public static String getCarRoute(String jxid){
        return URL+"methodName=JJApp&prc=prc_app_routelist&parms=account="+jxid+"&remark=test&sign=2E394E3900D5EA6A6BAC686B849A94B0";
    }

    public static String getCarSite(String routeid){
        return URL+"methodName=JJApp&prc=prc_app_sitelist&parms=routeid="+routeid+"&remark=test&sign=2E394E3900D5EA6A6BAC686B849A94B0";
    }

    public static String getCarPos(String jxid){
        return URL+"methodName=JJApp&prc=prc_app_getbuspos&parms=jxid="+jxid+"&remark=test&sign=2E394E3900D5EA6A6BAC686B849A94B0";
    }

    public static String getUpdateDriverCar(String uid,String routeid){
        //methodName=JJApp&prc=prc_app_updatedrivercar&parms=uid=12345678910|carid=0|rid=3DE2532A-D1C3-47BA-B92C-B0EEEF3CD56E&remark=test&sign=2E394E3900D5EA6A6BAC686B849A94B0
        return URL+"methodName=JJApp&prc=prc_app_updatedrivercar&parms=uid="+uid+"|carid=0|rid="+routeid+"&remark=test&sign=2E394E3900D5EA6A6BAC686B849A94B0";
    }
}
