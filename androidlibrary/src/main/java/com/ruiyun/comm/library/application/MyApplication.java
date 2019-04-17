package com.ruiyun.comm.library.application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import org.wcy.android.utils.RxTool;

public class MyApplication extends MultiDexApplication {
    private static Context mContext;
    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        instance = this;
        RxTool.init(mContext);

    }
    public static Context getmContext() {
        return mContext;
    }
    public static MyApplication getThisApplication() {
        return instance;
    }
}
