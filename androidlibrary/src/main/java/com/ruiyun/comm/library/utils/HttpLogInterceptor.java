package com.ruiyun.comm.library.utils;

import org.wcy.android.utils.RxLogTool;

import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLogInterceptor {

    public static HttpLoggingInterceptor getHttpLoggingInterceptor(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            //打印retrofit日志
            RxLogTool.d("RetrofitLog", "retrofitBack = " + message);
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }
}
