package com.wcy.ruiyun.mvp.app.application;

import android.app.Application;
import android.content.Context;

import com.wcy.ruiyun.mvp.app.service.InitializeService;

/**
 * Created by wcy on 2017/3/14.
 */

public class MyApplication extends Application {
    public static MyApplication instance;
    private static Context mContext;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        InitializeService.start(this);
    }

    public static Context getmContext() {
        return mContext;
    }
}
