package com.ruiyun.comm.library.api;

import com.ruiyun.comm.library.mvvm.RxResult;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * 测试接口service-上传相关
 * Created by wcy on 2017/3/14.
 */

public interface HttpUploadService {
    /*上传文件*/
    @Multipart
    @POST
    Flowable<RxResult> upload(@Url String url,@Part("token") RequestBody uid, @Part MultipartBody.Part file);
    @POST
    @FormUrlEncoded
    Flowable<RxResult> sendPost(@Url String url, @Field("params") String params, @Field("token") String token);

}
