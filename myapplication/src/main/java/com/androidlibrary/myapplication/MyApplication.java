package com.androidlibrary.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;


/**
 * MyApplication
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/10/19
 * @description applibrary
 */
public class MyApplication extends MultiDexApplication {

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new MaterialHeader(context));
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new BallPulseFooter(context));
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();
        ARouter.init(this);
    }
}
