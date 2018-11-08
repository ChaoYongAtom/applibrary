package com.ruiyun.comm.library.ui;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.ruiyun.comm.library.mvvm.BaseViewModel;
import com.ruiyun.comm.library.mvvm.LoadObserver;
import com.ruiyun.comm.library.mvvm.interfaces.LoadInterface;
import com.ruiyun.comm.library.utils.ParameterizedTypeUtil;

public class BaseMActivity<T extends BaseViewModel> extends BaseActivity implements LoadInterface {
    protected T mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ParameterizedTypeUtil.VMProviders(this);
        if (null != mViewModel) {
            mViewModel.loadState.observe(this, new LoadObserver(this));
            dataObserver();
        }
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
        toastError(msg);
    }

}