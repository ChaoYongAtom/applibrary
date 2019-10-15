package com.ruiyun.comm.library.mvvm.rx;


import androidx.annotation.NonNull;

import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.utils.HttpLogInterceptor;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author：wcy
 */
public class HttpHelper {
    private static volatile HttpHelper mHttpHelper = null;
    public static HashMap<String, Disposable> disposableMap = new HashMap<>();
    private static Retrofit mRetrofit;
    public static boolean isUpload = false;

    private HttpHelper() {
    }

    public static HttpHelper getInstance() {
        if (mHttpHelper == null) {
            synchronized (HttpHelper.class) {
                if (mHttpHelper == null) {
                    mHttpHelper = new HttpHelper();
                }
            }
        }
        return mHttpHelper;
    }

    public static void init(String baseUrl) {
        new HttpHelper.Builder().initOkHttp().createRetrofit(baseUrl).build();
    }


    public static class Builder {
        private OkHttpClient mOkHttpClient;

        private OkHttpClient.Builder mBuilder;

        private Retrofit mRetrofit;

        public Builder() {
        }

        /**
         * create OkHttp 初始化OKHttpClient,设置缓存,设置超时时间,设置打印日志
         *
         * @return Builder
         */
        public Builder initOkHttp() {
            if (mBuilder == null) {
                synchronized (HttpHelper.class) {
                    if (mBuilder == null) {
                        HttpLoggingInterceptor loggingInterceptor = HttpLogInterceptor.getHttpLoggingInterceptor();
                        mBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).addInterceptor(new DynamicConnectTimeout()).addNetworkInterceptor(loggingInterceptor).connectTimeout(JConstant.getConnectionTime(), TimeUnit.SECONDS).writeTimeout(JConstant.getConnectionTime(), TimeUnit.SECONDS).readTimeout(JConstant.getConnectionTime(), TimeUnit.SECONDS);
                        mBuilder.addInterceptor(chain -> {
                            Request newRequest = chain.request().newBuilder().addHeader("Connection", "close").addHeader("headers", JConstant.getHeardsVal()).build();
                            return chain.proceed(newRequest);
                        });

                    }
                }
            }

            return this;
        }

        /**
         * create retrofit
         *
         * @param baseUrl baseUrl
         * @return Builder
         */
        public Builder createRetrofit(String baseUrl) {
            Retrofit.Builder builder = new Retrofit.Builder().addConverterFactory(FastJsonConverterFactory.create())
                    // .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl(baseUrl);
            this.mOkHttpClient = mBuilder.build();
            this.mRetrofit = builder.client(mOkHttpClient).build();
            return this;
        }

        public void build() {
            HttpHelper.getInstance().build(this);
        }

    }

    private void build(Builder builder) {
        checkNotNull(builder);
        checkNotNull(builder.mBuilder);
        checkNotNull(builder.mOkHttpClient);
        checkNotNull(builder.mRetrofit);
        mRetrofit = builder.mRetrofit;
    }

    public static Retrofit getmRetrofit() {
        return mRetrofit;
    }

    public static @NonNull
    <T> T checkNotNull(final T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public <T> T create(Class<T> clz) {
        if (clz != null) {
            if (mRetrofit == null) {
                init(JConstant.getHttpUrl());
            }
            return mRetrofit.create(clz);
        }
        return null;

    }

}
