package com.ruiyun.comm.library.mvp;

import android.support.v7.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.ruiyun.comm.library.ui.BaseActivity;
import com.trello.rxlifecycle.LifecycleProvider;

import java.lang.ref.WeakReference;

/**
 * BasePresenter
 * friendscloud-android V1.0
 * 2018/2/28
 *
 * @auth wangchaoyong
 * 重庆锐云科技有限公司
 */

public class BasePresenter<V extends BaseView, M extends BaseModel> {
    public M mModel;
    public WeakReference<V> mViewRef;
    private BaseActivity appCompatActivity;
    private LifecycleProvider lifecycleProvider;
    BaseModeImpl baseMode;

    public void attachModelView(V pView, M pModel, BaseActivity appCompatActivity, LifecycleProvider lifecycleProvider) {
        mViewRef = new WeakReference<>(pView);
        this.appCompatActivity = appCompatActivity;
        this.lifecycleProvider = lifecycleProvider;
        this.mModel = pModel;
        if (pModel != null) {
            ((BaseModeImpl) mModel).attachPresenter(getView(), appCompatActivity, lifecycleProvider);
        }
    }


    public V getView() {
        if (isAttach()) {
            return mViewRef.get();
        } else {
            return null;
        }
    }

    public boolean isAttach() {
        return null != mViewRef && null != mViewRef.get();
    }

    /**
     * 无presenter下使用，请他情况下禁止调用
     *
     * @param method
     * @param parameters
     * @param cl
     * @param isList
     * @param isShowProgress
     * @param toast
     */
    public void sendPost(String method, JSONObject parameters, Class<?> cl, boolean isList, boolean isShowProgress, String toast, int connectionTime) {
        if (baseMode == null) baseMode = new BaseModeImpl();
        baseMode.attachPresenter(getView(), appCompatActivity, lifecycleProvider);
        baseMode.sendPost(method, parameters, cl, isList, isShowProgress, toast, connectionTime);
    }

    public void sendPost(String method, JSONObject parameters, Class<?> cl, boolean isList, boolean isShowProgress, String toast) {
        sendPost(method, parameters, cl, isList, isShowProgress, toast, 0);
    }

    public void sendPost(String method, JSONObject parameters, Class<?> cl, boolean isList, boolean isShowProgress) {
        sendPost(method, parameters, cl, isList, isShowProgress, null);
    }

    public void sendPost(String method, JSONObject parameters, Class<?> cl, boolean isList) {
        sendPost(method, parameters, cl, isList, true, null);
    }

    /**
     * 无presenter下使用，请他情况下禁止调用
     * 图片上传
     *
     * @param path
     */
    public void upload(String path) {
        if (baseMode == null) baseMode = new BaseModeImpl();
        baseMode.attachPresenter(getView(), appCompatActivity, lifecycleProvider);
        baseMode.upload(path);
    }

    public void upload(byte[] path) {
        if (baseMode == null) baseMode = new BaseModeImpl();
        baseMode.attachPresenter(getView(), appCompatActivity, lifecycleProvider);
        baseMode.upload(path);
    }

    public void onDettach() {
        if (null != mViewRef) {
            mViewRef.clear();
            baseMode = null;
            mModel = null;
            mViewRef = null;
        }
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }
}
