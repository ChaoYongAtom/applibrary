package com.wcy.app.lib_network;

import com.rxjava.rxlife.RxLife;
import com.wcy.app.lib_network.exception.ApiException;
import com.wcy.app.lib_network.interfaces.CallBack;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import rxhttp.wrapper.annotation.DefaultDomain;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.ssl.SSLSocketFactoryImpl;
import rxhttp.wrapper.ssl.X509TrustManagerImpl;

/**
 * RxHttp
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/10/14
 * @description applibrary
 */
public class HttpUtils {
    @DefaultDomain() //设置为默认域名
    public static String baseUrl;

    private static HashMap<String, Disposable> disposableMap = new HashMap<>();

    public static void post(String method, String headers, Map<String, String> parameters, CallBack listener) {
        Map<String, String> map = new HashMap<>();
        map.put("headers", headers);
        post(method, map, parameters, listener);
    }

    public static void post(String method, Map<String, String> headers, Map<String, String> parameters, CallBack listener) {
        if (disposableMap.containsKey(method)) {
            dispose(method);
        }
        RxHttp rxHttp = RxHttp.postForm(method);
        rxHttp.addHeader("Connection", "close");
        if (headers != null) {
            for (String key : headers.keySet()) {
                rxHttp.addHeader(key, headers.get(key));
            }
        }
        if (parameters != null) {
            rxHttp.add(parameters);
        }
        Disposable disposable = rxHttp.asString().subscribe(s -> {
            listener.onNext(s.toString());
        }, throwable -> {
            dispose(method);
            listener.onError(new ApiException((Throwable) throwable));
        });
        disposableMap.put(method, disposable);
    }

    private static void dispose(String method) {
        Disposable disposable1 = disposableMap.get(method);
        disposable1.dispose();
        disposableMap.remove(disposable1);
    }

    public static void init(String url, int timeout, boolean isdebug) {
        baseUrl = url;
        RxHttp.init(getDefaultOkHttpClient(timeout), isdebug);
    }

    /**
     * 连接、读写超时均为10s、添加信任证书并忽略host验证
     *
     * @return 返回默认的OkHttpClient对象
     */
    private static OkHttpClient getDefaultOkHttpClient(int timeout) {
        X509TrustManager trustAllCert = new X509TrustManagerImpl();
        SSLSocketFactory sslSocketFactory = new SSLSocketFactoryImpl(trustAllCert);
        return new OkHttpClient.Builder().connectTimeout(timeout, TimeUnit.SECONDS).readTimeout(timeout, TimeUnit.SECONDS).writeTimeout(timeout, TimeUnit.SECONDS).sslSocketFactory(sslSocketFactory, trustAllCert) //添加信任证书
                .hostnameVerifier((hostname, session) -> true) //忽略host验证
                .build();
    }
}
