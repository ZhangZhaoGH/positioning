package com.example.administrator.positioning;

import android.app.Application;
import android.content.Context;


/**
 * Created by zz on 2017/2/20.
 */

public class MyApplication extends Application {
    private static Context mContext;
    /** 是否输出日志信息 **/
    public final static boolean isDebug=true;
    /**
     * 是否展示删除确认提示
     */
    public static boolean isShowDel=true;
    public final static String APP_NAME="ZNotes";
    public final static String HEAD="head";
    public final static String BGIMG="bgimg";
    public static String appVersion;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

    }

    public static Context getContext(){
        return mContext;
    }
}
