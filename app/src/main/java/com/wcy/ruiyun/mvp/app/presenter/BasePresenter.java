package com.wcy.ruiyun.mvp.app.presenter;

import android.support.v7.app.AppCompatActivity;

import com.wcy.ruiyun.mvp.app.model.impl.base.BaseModeImpl;
import com.wcy.ruiyun.mvp.app.model.impl.base.BaseModel;
import com.wcy.ruiyun.mvp.app.view.BaseView;


/**
 * Created by wcy on 2018/1/19.
 */

public abstract class BasePresenter<T extends BaseView> {
    public T view;
    AppCompatActivity appCompatActivity;

    private BasePresenter() {
    }

    public BasePresenter(T view) {
        super();
        this.view = view;
    }

    public void setInit(AppCompatActivity activity) {
        appCompatActivity = activity;
        BaseModeImpl baseMode = (BaseModeImpl) init();
        baseMode.attachPresenter(this);
    }

    public T getView() {
        return view;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void detachView() {
        this.view = null;
    }

    protected abstract BaseModel init();
}
