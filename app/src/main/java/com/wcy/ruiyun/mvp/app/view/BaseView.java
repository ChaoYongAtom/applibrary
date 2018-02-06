package com.wcy.ruiyun.mvp.app.view;

import com.wcy.ruiyun.mvp.app.api.entitys.BaseResult;

import org.wcy.android.retrofit.exception.ApiException;

/**
 * Created by admin on 2018/1/19.
 */

public interface BaseView {
    /**
     * 成功后回调方法
     */
    void onNext(BaseResult result);

    /**
     * 失败
     * 失败或者错误方法
     * 自定义异常处理
     *
     * @param e
     */
    void onError(ApiException e, String mothead);

}
