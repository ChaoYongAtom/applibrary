package com.wcy.ruiyun.mvp.app.api;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 测试接口service-post相关
 * Created by wcy on 2017/3/14.
 */

public interface HttpPostService {

    String Login = "login";

    @GET("api/4/news/latest")
    Observable<String> login();

    //退出登录
    String loginOut = "loginOut";

    @POST("loginout")
    Observable<String> loginOut(@Query("params") String params, @Query("token") String token);

    //获取更新内容
    String update = "getUpdate";

    @POST("newestversion")
    Observable<String> getUpdate();

}
