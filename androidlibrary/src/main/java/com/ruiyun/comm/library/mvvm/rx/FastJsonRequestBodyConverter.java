package com.ruiyun.comm.library.mvvm.rx;

import com.alibaba.fastjson.JSON;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * FastJsonRequestBodyConverter
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/1/29
 * @description YjSales
 */
public class FastJsonRequestBodyConverter <T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    @Override
    public RequestBody convert(T value) {
        return RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value));
    }
}
