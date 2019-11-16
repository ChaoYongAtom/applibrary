package com.wcy.app.lib.network;

import com.wcy.app.lib.network.exception.ApiException;
import com.wcy.app.lib.network.interfaces.DownLoadResult;
import com.wcy.app.lib.network.interfaces.NetWorkResult;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import rxhttp.wrapper.annotation.DefaultDomain;
import rxhttp.wrapper.annotation.Domain;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.param.RxHttp$FormParam;
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
    @Domain(name = "domainUrl") //设置非默认域名，name 可不传，不传默认为变量的名称
    public static String mDomainUrl;
    private static int mTimeout = 6;
    private static HashMap<String, Disposable> disposableMap = new HashMap<>();

    public static void postBody(HttpBuilder builder, String parameters, NetWorkResult listener) {
        RxHttp rxHttp = RxHttp.postJson(builder.getUrl()).setJsonParams(parameters);
        if (builder.getDomainUrl() != null && !"".equals(builder.getDomainUrl())) {
            mDomainUrl = builder.getDomainUrl();
            rxHttp.setDomainTodomainUrlIfAbsent();
        }
        rxHttp.addHeader("Connection", "close");
        if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
            for (String keys : builder.getHeaders().keySet()) {
                rxHttp.addHeader(keys, builder.getHeaders().get(keys));
            }
        }
        Observable<String> observable = rxHttp.asString();
        //动态设置超时时间
        if (builder.getTimeout() > 0) {
            observable.timeout(builder.getTimeout(), TimeUnit.SECONDS);
        }
        observable.subscribe(s -> {
            listener.onNext(s);
        }, throwable -> {
        });
    }

    public static Disposable postBodyDisposable(HttpBuilder builder, String parameters, NetWorkResult listener) {
        //生成一个根据地址和tab组合的key,来判断是否同一请求发起了多次请求，则取消之前的请求，使用最后一次
        String key = (builder.getTag() == null ? builder.getUrl() : builder.getTag().concat(builder.getUrl()));
        if (disposableMap.containsKey(key)) dispose(key);
        RxHttp rxHttp = RxHttp.postJson(builder.getUrl()).setJsonParams(parameters);
        if (builder.getDomainUrl() != null && !"".equals(builder.getDomainUrl())) {
            mDomainUrl = builder.getDomainUrl();
            rxHttp.setDomainTodomainUrlIfAbsent();
        }
        rxHttp.addHeader("Connection", "close");
        if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
            for (String keys : builder.getHeaders().keySet()) {
                rxHttp.addHeader(keys, builder.getHeaders().get(keys));
            }
        }
        Observable<String> observable = rxHttp.asString();
        //动态设置超时时间
        if (builder.getTimeout() > 0) {
            observable.timeout(builder.getTimeout(), TimeUnit.SECONDS);
        }
        Disposable disposable = observable.subscribe(s -> {
            listener.onNext(s);
        }, throwable -> {
            dispose(key);
            listener.onError(new ApiException(throwable));
        });
        disposableMap.put(key, disposable);
        return disposable;
    }


    public static void download(String url, String savePath, DownLoadResult listener) {
        RxHttp.get(url).asDownload(savePath, progress -> {
            listener.Progress(progress.getProgress(), progress.getCurrentSize(), progress.getTotalSize());
        }, AndroidSchedulers.mainThread()).subscribe(s -> listener.onNext(s), throwable -> listener.onError(throwable));
    }

    /**
     * @param builder  是否使用默认地址，如果不使用默认地址则使用第二默认地址
     * @param listener 请求成功与失败的回调函数
     * @return
     */
    public static Disposable post(HttpBuilder builder, NetWorkResult listener) {
        //生成一个根据地址和tab组合的key,来判断是否同一请求发起了多次请求，则取消之前的请求，使用最后一次
        String key = (builder.getTag() == null ? builder.getUrl() : builder.getTag().concat(builder.getUrl()));
        if (disposableMap.containsKey(key)) dispose(key);
        RxHttp$FormParam rxHttp = RxHttp.postForm(builder.getUrl());
        //判断使用默认地址还是动态地址
        if (builder.getDomainUrl() != null && !"".equals(builder.getDomainUrl())) {
            mDomainUrl = builder.getDomainUrl();
            rxHttp.setDomainTodomainUrlIfAbsent();
        }
        rxHttp.addHeader("Connection", "close");
        if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
            for (String keys : builder.getHeaders().keySet()) {
                rxHttp.addHeader(keys, builder.getHeaders().get(keys));
            }
        }
        //设置参数
        if (builder.getParameters() != null && builder.getParameters().size() > 0) {
            rxHttp.add(builder.getParameters());
        }
        //设置文件参数
        if (builder.getFiles() != null && builder.getFiles().size() > 0) {
            for (String file : builder.getFiles().keySet()) {
                rxHttp.addFile(file, new File(builder.getFiles().get(file)));
            }
        }
        Observable<String> observable = rxHttp.asString();
        //动态设置超时时间
        if (builder.getTimeout() > 0) {
            observable.timeout(builder.getTimeout(), TimeUnit.SECONDS);
        }
        Disposable disposable = observable.subscribe(s -> {
            listener.onNext(s);
        }, throwable -> {
            dispose(key);
            listener.onError(new ApiException(throwable));
        });
        disposableMap.put(key, disposable);
        return disposable;
    }

    private static void dispose(String method) {
        Disposable disposable1 = disposableMap.get(method);
        disposable1.dispose();
        disposableMap.remove(disposable1);
    }

    /**
     * 初始化http请求
     *
     * @param url     默认地址
     * @param timeout 连接超时时间（秒）
     * @param isdebug 是否dubug模式 dubug模式下会输入日志
     */
    public static void init(String url, int timeout, boolean isdebug) {
        baseUrl = url;
        mTimeout = timeout;
        if (timeout > 0) {
            try {
                RxHttp.init(getDefaultOkHttpClient(timeout), isdebug);
            } catch (IllegalArgumentException e) {
            }
        } else {
            RxHttp.setDebug(isdebug);
        }

    }

    public static void init(String url, boolean isdebug) {
        init(url, mTimeout, isdebug);
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
