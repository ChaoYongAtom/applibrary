package org.wcy.android.ui;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;

import org.wcy.android.live.AbsViewModel;
import org.wcy.android.live.BaseListVo;
import org.wcy.android.live.LoadInterface;
import org.wcy.android.live.LoadObserver;
import org.wcy.android.utils.ParameterizedTypeUtil;

/**
 * BaseMFragment
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/10/21
 * @description applibrary
 */
public class BaseMFragment<T extends AbsViewModel>  extends BaseFragment implements LoadInterface {
    protected T mViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
    }
    private void initViewModel() {
        mViewModel = ParameterizedTypeUtil.VMProviders(this);
        if (null != mViewModel && !mViewModel.getClass().getSimpleName().equals(AbsViewModel.class.getSimpleName())) {
            mViewModel.setFragmentName(getClassName());
            mViewModel.loadState.observe(this, new LoadObserver(this));
            dataObserver();
        }
    }

    @Override
    public void finishFramager() {
        if(mViewModel!=null)mViewModel.unSubscribe();
        mViewModel=null;
        super.finishFramager();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewModel == null) {
            initViewModel();
        }
    }
    protected <M> MutableLiveData<M> registerObserver(Class<M> tClass) {
        return registerObserver(tClass, "");
    }

    protected <M> MutableLiveData<M> registerObserver(Class<M> tClass, String tag) {
        String event = getClassName().concat(tClass.getSimpleName());
        event = event.concat(tag);
        return  mViewModel.putLiveBus(event);
    }

    protected <M> MutableLiveData<BaseListVo<M>> registerObservers(Class<M> tClass) {
        String event = getClassName().concat(tClass.getSimpleName()).concat("list");
        return  mViewModel.putLiveBus(event);
    }

    protected <M> MutableLiveData<BaseListVo<M>> registerObservers(Class<M> tClass, String tag) {
        String event = getClassName().concat(tClass.getSimpleName()).concat("list");
        event = event.concat(tag);
        return  mViewModel.putLiveBus(event);
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
        toast(msg);
    }
    @Override
    public String getClassName() {
        return getClass().getSimpleName() + getStateEventKey();
    }

    @Override
    public String getStateEventKey() {
        return "";
    }

}
