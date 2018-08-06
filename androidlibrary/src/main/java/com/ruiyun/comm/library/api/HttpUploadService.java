package com.ruiyun.comm.library.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * 测试接口service-上传相关
 * Created by wcy on 2017/3/14.
 */

public interface HttpUploadService {
    /*上传文件*/
    @Multipart
    @POST("uploadimage")
    Observable<String> uploadImage(@Part("token") RequestBody uid, @Part MultipartBody.Part file);

}
