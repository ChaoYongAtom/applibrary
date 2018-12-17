package org.wcy.android.retrofit.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.wcy.android.retrofit.Api.BaseApi;
import org.wcy.android.retrofit.exception.ApiException;
import org.wcy.android.retrofit.exception.CodeException;
import org.wcy.android.retrofit.exception.FactoryException;
import org.wcy.android.retrofit.exception.RetryWhenNetworkException;
import org.wcy.android.retrofit.listener.HttpOnNextListener;
import org.wcy.android.retrofit.subscribers.ProgressSubscriber;
import org.wcy.android.utils.RxDataTool;
import org.wcy.android.utils.RxLogTool;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * http交互处理类
 *
 * @version v1.0
 * @date 2017/3/14
 * @auth wcy
 * @company 重庆锐云科技有限公司
 */
public class HttpManager {

    private String headers = "";
    LifecycleProvider lifecycleProvider;
    /*软引用對象*/
    private HttpOnNextListener onNextListener;
    private Context contextSoftReference;

    public HttpManager(Context context, LifecycleProvider lifecycleProvider, HttpOnNextListener onNextListener, String headers) {
        this.onNextListener = onNextListener;
        this.contextSoftReference = context;
        this.headers = headers;
        this.lifecycleProvider = lifecycleProvider;
    }

    /**
     * 处理http请求
     *
     * @param basePar 封装的请求数据
     */
    @SuppressLint("WrongConstant")
    public void doHttpDeal(BaseApi basePar) {
        if (RxDataTool.isNullString(basePar.getBaseUrl())) {
            onNextListener.onError(new ApiException(null, CodeException.NOT_NETWORD, "服务器地址错误"), basePar.getMethod());
        } else {
            //手动创建一个OkHttpClient并设置超时时间缓存等设置
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("headers", headers)
                            .build();
                    return chain.proceed(newRequest);
                }
            });

            builder.connectTimeout(basePar.getConnectionTime(), TimeUnit.SECONDS);
            builder.readTimeout(basePar.getConnectionTime(), TimeUnit.SECONDS);
            builder.writeTimeout(basePar.getConnectionTime(), TimeUnit.SECONDS);
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    //打印retrofit日志
                    RxLogTool.d("RetrofitLog", "retrofitBack = " + message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
            /*创建retrofit对象*/
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(basePar.getBaseUrl())
                    .build();
            /*rx处理*/
            ProgressSubscriber subscriber = new ProgressSubscriber(basePar, onNextListener, contextSoftReference);
            Observable observable = basePar.getObservable(retrofit)
                    /*失败后的retry配置*/
                    .retryWhen(new RetryWhenNetworkException(basePar.getCount(), basePar.getConnectionTime()))
                    /*异常处理*/
                    .onErrorResumeNext(funcException)
                    //Note:手动设置在activity onDestroy的时候取消订阅
                    .compose(lifecycleProvider.bindUntilEvent(basePar.isActivity() ? ActivityEvent.DESTROY : FragmentEvent.DESTROY))
                    /*http请求线程*/
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    /*回调线程*/
                    .observeOn(AndroidSchedulers.mainThread());
            /*数据回调*/
            observable.subscribe(subscriber);
        }
    }


    /**
     * 异常处理
     */
    Func1 funcException = new Func1<Throwable, Observable>() {
        @Override
        public Observable call(Throwable throwable) {
            return Observable.error(FactoryException.analysisExcetpion(throwable));
        }
    };

}
