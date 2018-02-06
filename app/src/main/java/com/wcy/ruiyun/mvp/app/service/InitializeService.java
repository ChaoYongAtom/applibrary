package com.wcy.ruiyun.mvp.app.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;
import com.wcy.ruiyun.mvp.app.api.HttpPostService;
import com.wcy.ruiyun.mvp.app.api.SubjectPostApi;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 初始数据服务
 * Created by wcy on 2017/6/12.
 */

public class InitializeService extends IntentService {

    private static final String ACTION_INIT = "initApplication";

    public InitializeService() {
        super(ACTION_INIT);
    }

    private static String httpUrl;

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT.equals(action)) {
                initApplication();
            }
        }
    }

    public static String getHttpUrl() {
        return httpUrl;
    }

    private void initApplication() {
        try {
            HashMap<String, Integer> mapMethods = SubjectPostApi.mapMethods;
            Method[] methods = HttpPostService.class.getMethods();
            for (Method method : methods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                mapMethods.put(method.getName(), parameterTypes.length);
            }
            ApplicationInfo appInfo = getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);
            Bundle bundle = appInfo.metaData;
            boolean isdebug = bundle.getBoolean("ENABLE_DEBUG", true);
            httpUrl = bundle.getString("HTTP_URL");
            //在debug模式下开启日志
            LogUtils.getLogConfig().configAllowLog(isdebug);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
