package com.ruiyun.comm.library.ui;

import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ruiyun.comm.library.mvvm.BaseViewModel;
import com.ruiyun.comm.library.mvvm.LoadObserver;
import com.ruiyun.comm.library.mvvm.event.LiveBus;
import com.ruiyun.comm.library.mvvm.interfaces.LoadInterface;
import com.ruiyun.comm.library.utils.ParameterizedTypeUtil;

import java.util.ArrayList;
import java.util.List;

public class BaseMActivity<T extends BaseViewModel> extends BaseActivity implements LoadInterface {
    protected T mViewModel;
    private List<String> eventKeys = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ParameterizedTypeUtil.VMProviders(this);
        if (null != mViewModel) {
            mViewModel.setFragmentName(getClassName());
            mViewModel.loadState.observe(this, new LoadObserver(this));
            dataObserver();
        }
    }

    @Override
    public void dataObserver() {

    }

    protected <T> MutableLiveData<T> registerObserver(Class<T> tClass) {
        String event = getClassName().concat(tClass.getName());
        eventKeys.add(event);
        return LiveBus.getDefault().subscribe(event);
    }


    @Override
    public void showSuccess(int state, String msg) {

    }

    @Override
    protected void onDestroy() {
        if (eventKeys != null && eventKeys.size() > 0) {
            for (int i = 0; i < eventKeys.size(); i++) {
                LiveBus.getDefault().clear(eventKeys.get(i));
            }
            eventKeys=null;
        }
        super.onDestroy();
    }

    @Override
    public void showLoading() {

    }

    protected String getClassName() {
        return getClass().getName();
    }

    @Override
    public void showError(int state, String msg) {
        toastError(msg);
    }

}