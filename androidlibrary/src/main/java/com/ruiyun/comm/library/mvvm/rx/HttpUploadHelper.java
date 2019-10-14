package com.ruiyun.comm.library.mvvm.rx;


import androidx.annotation.NonNull;
import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.utils.HttpLogInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author：wcy
 */
public class HttpUploadHelper {
    private static volatile HttpUploadHelper mHttpHelper = null;

    private static Retrofit mRetrofit;

    private HttpUploadHelper() {
    }

    public static HttpUploadHelper getInstance() {
        if (mHttpHelper == null) {
            synchronized (HttpUploadHelper.class) {
                if (mHttpHelper == null) {
                    mHttpHelper = new HttpUploadHelper();
                }
            }
        }
        return mHttpHelper;
    }

    public static void init(String baseUrl) {
        new HttpUploadHelper.Builder().initOkHttp().createRetrofit(baseUrl).build();
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
                synchronized (HttpUploadHelper.class) {
                    if (mBuilder == null) {
                        HttpLoggingInterceptor loggingInterceptor = HttpLogInterceptor.getHttpLoggingInterceptor();
                        mBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).addNetworkInterceptor(loggingInterceptor).connectTimeout(JConstant.getUploadTime(), TimeUnit.MINUTES).writeTimeout(JConstant.getUploadTime(), TimeUnit.MINUTES).readTimeout(JConstant.getUploadTime(), TimeUnit.MINUTES);
                        if (JConstant.isIsHeaders()) {
                            mBuilder.addInterceptor(chain -> {
                                Request newRequest = chain.request().newBuilder().addHeader("headers", JConstant.getHeardsVal()).build();
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
            Retrofit.Builder builder = new Retrofit.Builder().addConverterFactory(FastJsonConverterFactory.create())
                    // .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl(baseUrl);
            this.mOkHttpClient = mBuilder.build();
            this.mRetrofit = builder.client(mOkHttpClient).build();
            return this;
        }

        public void build() {
            HttpUploadHelper.getInstance().build(this);
        }

    }

    private void build(Builder builder) {
        checkNotNull(builder);
        checkNotNull(builder.mBuilder);
        checkNotNull(builder.mOkHttpClient);
        checkNotNull(builder.mRetrofit);
        mRetrofit = builder.mRetrofit;
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
            return mRetrofit.create(clz);
        }
        return null;

    }

}
