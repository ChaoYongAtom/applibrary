package com.ruiyun.comm.library.application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

import org.wcy.android.utils.RxTool;

public class MyApplication extends MultiDexApplication {

    private static Context mContext;
    public static MyApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        instance = this;
        RxTool.init(mContext);
        if (isX5Web()) {
            try{
                QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

                    @Override
                    public void onViewInitFinished(boolean arg0) {
                        //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                        Log.d("QbSdk", " onViewInitFinished is " + arg0);
                    }

                    @Override
                    public void onCoreInitFinished() {
                        Log.d("QbSdk", " onViewInitFinished onCoreInitFinished ");
                        // TODO Auto-generated method stub
                    }
                };
                //x5内核初始化接口
                QbSdk.initX5Environment(this, cb);
            }catch (Exception e){

            }
        }
    }
    public static Context getmContext() {
        return mContext;
    }
    protected boolean isX5Web() {
        return true;
    }
}
