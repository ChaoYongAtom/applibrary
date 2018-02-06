package com.wcy.ruiyun.mvp.app.model.impl.base;

import android.support.v7.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.wcy.ruiyun.mvp.app.api.entitys.BaseResult;
import com.wcy.ruiyun.mvp.app.presenter.BasePresenter;
import com.wcy.ruiyun.mvp.app.utils.HttpUtil;
import com.wcy.ruiyun.mvp.app.view.BaseView;

import org.wcy.android.retrofit.exception.ApiException;

/**
 * Created by admin on 2018/1/19.
 */

public class BaseModeImpl implements BaseView {
    private BaseView onListener;
    private AppCompatActivity appCompatActivity;

    public void attachPresenter(BasePresenter basePresenter) {
        onListener = basePresenter.getView();
        appCompatActivity = basePresenter.getAppCompatActivity();
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }


    public void sendPost(String method, JSONObject parameters, Class<?> cl) {
        sendPost(method, parameters, cl, false, true, null);
    }

    public void sendPost(String method, JSONObject parameters, Class<?> cl, boolean isList) {
        sendPost(method, parameters, cl, isList, true, null);
    }

    public void sendPost(String method, JSONObject parameters, Class<?> cl, boolean isList, boolean isShowProgress) {
        sendPost(method, parameters, cl, isList, isShowProgress, null);
    }

    public void sendPost(String method, JSONObject parameters, Class<?> cl, boolean isList, boolean isShowProgress, String toast) {
        HttpUtil httpUtil = new HttpUtil(appCompatActivity, this);
        httpUtil.send(method, parameters, cl, isList, isShowProgress, toast);
    }

    @Override
    public void onNext(BaseResult result) {
        onListener.onNext(result);
    }

    @Override
    public void onError(ApiException e, String mothead) {
        onListener.onError(e, mothead);
    }
}
