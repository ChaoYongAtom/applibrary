package com.ruiyun.comm.library.mvvm.rx;

import com.ruiyun.comm.library.common.JConstant;

import java.io.IOException;
import java.lang.reflect.Field;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * DynamicConnectTimeout
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/2/27
 * @description YjSales
 */
public class DynamicConnectTimeout implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        if (HttpHelper.getmRetrofit() != null) {
            String questUrl = oldRequest.url().url().toString();
            questUrl = questUrl.substring(questUrl.lastIndexOf("/") + 1, questUrl.length());
            if (questUrl.indexOf("upload") >= 0 && !HttpHelper.isUpload) {
                setDynamicConnectTimeout(true, HttpHelper.getmRetrofit());
                HttpHelper.isUpload = true;
            } else if (questUrl.indexOf("upload") < 0 && HttpHelper.isUpload) {
                setDynamicConnectTimeout(false, HttpHelper.getmRetrofit());
                HttpHelper.isUpload = false;
            }
        }
        Request newRequest = oldRequest.newBuilder().method(oldRequest.method(), oldRequest.body()).url(oldRequest.url()).build();
        return chain.proceed(newRequest);
    }

    /**
     * 根据所需接口、进行动态设置网络超时时间
     *
     * @param isUpload
     * @param retrofit
     */
    private void setDynamicConnectTimeout(boolean isUpload, Retrofit retrofit) {
        //动态设置超时时间
        try {
            //1、private final okhttp3.Call.Factory callFactory;   Retrofit 的源码 构造方法中
            Field callFactoryField = retrofit.getClass().getDeclaredField("callFactory");
            callFactoryField.setAccessible(true);
            //2、callFactory = new OkHttpClient();   Retrofit 的源码 build()中
            OkHttpClient client = (OkHttpClient) callFactoryField.get(retrofit);
            //3、OkHttpClient(Builder builder)     OkHttpClient 的源码 构造方法中
            Field connectTimeoutField = client.getClass().getDeclaredField("connectTimeout");
            connectTimeoutField.setAccessible(true);
            Field readTimeout = client.getClass().getDeclaredField("readTimeout");
            readTimeout.setAccessible(true);
            Field writeTimeout = client.getClass().getDeclaredField("writeTimeout");
            writeTimeout.setAccessible(true);
            //4、根据所需要的时间进行动态设置超时时间
            if (isUpload) {
                connectTimeoutField.setInt(client, JConstant.getUploadTime() * 1000);
                readTimeout.setInt(client, JConstant.getUploadTime() * 1000);
                writeTimeout.setInt(client, JConstant.getUploadTime() * 1000);
            } else {
                connectTimeoutField.setInt(client, JConstant.getConnectionTime() * 1000);
                readTimeout.setInt(client, JConstant.getConnectionTime() * 1000);
                writeTimeout.setInt(client, JConstant.getConnectionTime() * 1000);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
