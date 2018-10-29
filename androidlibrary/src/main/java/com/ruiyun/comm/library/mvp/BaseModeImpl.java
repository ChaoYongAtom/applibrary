package com.ruiyun.comm.library.mvp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.ruiyun.comm.library.api.entitys.BaseResult;
import com.ruiyun.comm.library.ui.BaseActivity;
import com.ruiyun.comm.library.utils.HttpUtil;
import com.trello.rxlifecycle.LifecycleProvider;

import org.wcy.android.retrofit.exception.ApiException;
import org.wcy.android.utils.RxKeyboardTool;

/**
 * Created by wcy on 2018/1/19.
 */

public class BaseModeImpl implements BaseView {
    private BaseView onListener;
    private BaseActivity appCompatActivity;
    LifecycleProvider lifecycleProvider;
    HttpUtil httpUtil;
    private boolean isActivity;
    public void attachPresenter(BaseView view, BaseActivity activity, LifecycleProvider lifecycleProvider,boolean isActivity) {
        onListener = view;
        appCompatActivity = activity;
        this.isActivity=isActivity;
        this.lifecycleProvider = lifecycleProvider;
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
        sendPost(method, parameters, cl, isList, isShowProgress, toast, 0);
    }

    public void sendPost(String method, JSONObject parameters, Class<?> cl, boolean isList, boolean isShowProgress, String toast, int connectionTime) {
        httpUtil = new HttpUtil(appCompatActivity.getThisContext(), lifecycleProvider, this,isActivity);
        RxKeyboardTool.hideSoftInput(appCompatActivity);
        if (connectionTime > 0) httpUtil.setConnectionTime(connectionTime);
        httpUtil.send(method, parameters, cl, isList, isShowProgress, toast);
    }

    public void upload(String path) {
        httpUtil = new HttpUtil(appCompatActivity.getThisContext(), lifecycleProvider, this,isActivity);
        httpUtil.upload(path);
    }

    public void upload(byte[] path) {
        httpUtil = new HttpUtil(appCompatActivity.getThisContext(), lifecycleProvider, this,isActivity);
        httpUtil.upload(path);
    }

    @Override
    public void onNext(BaseResult result) {
        onListener.onNext(result);
    }

    @Override
    public void onError(ApiException e, String mothead) {
        onListener.onError(e, mothead);
    }

    @Override
    public Context getThisContext() {
        return appCompatActivity.getApplicationContext();
    }
}
