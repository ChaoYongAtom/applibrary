package com.ruiyun.comm.library.mvvm.rx;


import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.utils.HttpLogInterceptor;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author：wcy
 */
public class HttpHelper {
    private static volatile HttpHelper mHttpHelper = null;

    private static Retrofit mRetrofit;

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
        new HttpHelper.Builder()
                .initOkHttp()
                .createRetrofit(baseUrl)
                .build();
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
                        mBuilder = new OkHttpClient.Builder()
                                .addInterceptor(HttpLogInterceptor.getHttpLoggingInterceptor())
                                .connectTimeout(JConstant.getConnectionTime(), TimeUnit.SECONDS)
                                .writeTimeout(JConstant.getConnectionTime(), TimeUnit.SECONDS)
                                .readTimeout(JConstant.getConnectionTime(), TimeUnit.SECONDS);
                        if (JConstant.isIsHeaders()) {
                            mBuilder.addInterceptor(chain -> {
                                Request newRequest = chain.request().newBuilder()
                                        .addHeader("headers", JConstant.getHeardsVal())
                                        .build();
                                return chain.proceed(newRequest);
                            });
                        }
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
            Retrofit.Builder builder = new Retrofit.Builder()
                   .addConverterFactory(FastJsonConverterFactory.create())
                   // .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(baseUrl);
            this.mOkHttpClient = mBuilder.build();
            this.mRetrofit = builder.client(mOkHttpClient)
                    .build();
            return this;
        }

        public void build() {
            HttpHelper.getInstance().build(this);
        }

    }

    private void build(Builder builder) {
        mRetrofit = builder.mRetrofit;
    }

    public <T> T create(Class<T> clz) {
        if (clz != null) {
            return mRetrofit.create(clz);
        }
        return null;

    }

}
