package com.ruiyun.comm.library.application;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.jeremyliao.liveeventbus.LiveEventBus;

import org.wcy.android.utils.RxTool;

public class MyApplication extends MultiDexApplication {
    private static Context mContext;
    private static MyApplication instance;
    private static boolean isUpdate=false;
    @Override
    public void onCreate() {
        super.onCreate();
        LiveEventBus.get()
                .config()
                .lifecycleObserverAlwaysActive(false);
        mContext = getApplicationContext();
        instance = this;
        RxTool.init(mContext);

    }
    //设置app字体不允许随系统调节而发生大小变化
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            //非默认值
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration newConfig = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (newConfig.fontScale != 1) {
            newConfig.fontScale = 1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Context configurationContext = createConfigurationContext(newConfig);
                resources = configurationContext.getResources();
                displayMetrics.scaledDensity = displayMetrics.density * newConfig.fontScale;
            } else {
                //updateConfiguration 在 API 25(7.0以上系统)之后，被方法 createConfigurationContext 替代
                resources.updateConfiguration(newConfig, displayMetrics);
            }
        }
        return resources;
    }

    public  boolean isUpdate() {
        return isUpdate;
    }

    public  void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public static Context getmContext() {
        return mContext;
    }
    public static MyApplication getThisApplication() {
        return instance;
    }
}
