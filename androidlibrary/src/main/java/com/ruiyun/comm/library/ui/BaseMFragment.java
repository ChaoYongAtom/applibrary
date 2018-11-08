package com.ruiyun.comm.library.ui;

import android.os.Bundle;

import com.ruiyun.comm.library.mvvm.BaseViewModel;
import com.ruiyun.comm.library.mvvm.LoadObserver;
import com.ruiyun.comm.library.mvvm.interfaces.LoadInterface;
import com.ruiyun.comm.library.utils.ParameterizedTypeUtil;

public class BaseMFragment<T extends BaseViewModel> extends LibFragment implements LoadInterface {
    protected T mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ParameterizedTypeUtil.VMProviders(this);
        if (null != mViewModel) {
            mViewModel.loadState.observe(this, new LoadObserver(this));
            dataObserver();
        }
    }

    @Override
    public int setCreatedLayoutViewId() {
        return 0;
    }

    @Override
    public String setTitle() {
        return null;
    }

    @Override
    protected void initTitle(String title) {

    }

    @Override
    protected void initView() {

    }

    @Override
    public void dataObserver() {

    }

    @Override
    public void showSuccess(int state, String msg) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(int state, String msg) {

    }

}
