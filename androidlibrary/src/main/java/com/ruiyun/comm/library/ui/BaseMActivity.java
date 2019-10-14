package com.ruiyun.comm.library.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.ruiyun.comm.library.mvvm.BaseListVo;
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
        try{
            mViewModel = ParameterizedTypeUtil.VMProviders(this);
            if (null != mViewModel && !mViewModel.getClass().getSimpleName().equals(BaseViewModel.class.getSimpleName())) {
                mViewModel.setFragmentName(getClassName());
                mViewModel.loadState.observe(this, new LoadObserver(this));
                dataObserver();
            }
        }catch (Exception E){

        }
    }

    @Override
    public void dataObserver() {

    }

    protected <M> MutableLiveData<M> registerObserver(Class<M> tClass) {
        String event = getClassName().concat(tClass.getSimpleName());
        eventKeys.add(event);
        return LiveBus.getDefault().subscribe(event);
    }

    protected <M> MutableLiveData<M> registerObserver(Class<M> tClass, String tag) {
        String event = getClassName().concat(tClass.getSimpleName());
        event = event.concat(tag);
        eventKeys.add(event);
        return LiveBus.getDefault().subscribe(event);
    }

    protected <M> MutableLiveData<BaseListVo<M>> registerObservers(Class<M> tClass) {
        String event = getClassName().concat(tClass.getSimpleName()).concat("list");
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
            eventKeys = null;
        }
        super.onDestroy();
    }

    @Override
    public void showLoading() {

    }

    protected String getClassName() {
        return getClass().getSimpleName();
    }

    @Override
    public void showError(int state, String msg) {
        toastError(msg);
    }

}