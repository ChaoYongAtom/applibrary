package com.ruiyun.comm.library.ui;

import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;
import com.gyf.barlibrary.ImmersionBar;
import com.ruiyun.comm.library.mvvm.BaseListVo;
import com.ruiyun.comm.library.mvvm.BaseViewModel;
import com.ruiyun.comm.library.mvvm.LoadObserver;
import com.ruiyun.comm.library.mvvm.event.LiveBus;
import com.ruiyun.comm.library.mvvm.interfaces.LoadInterface;
import com.ruiyun.comm.library.utils.ParameterizedTypeUtil;

import java.util.ArrayList;
import java.util.List;

public class BaseMFragment<T extends BaseViewModel> extends LibFragment implements LoadInterface {
    protected T mViewModel;
    protected ImmersionBar mImmersionBar;
    private List<String> eventKeys = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ParameterizedTypeUtil.VMProviders(this);
        if (null != mViewModel && !mViewModel.getClass().getSimpleName().equals(BaseViewModel.class.getSimpleName())) {
            mViewModel.setFragmentName(getClassName());
            mViewModel.loadState.observe(this, new LoadObserver(this));
            dataObserver();
        }
        StatService.onPageStart(getThisContext(), getClassName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setView(inflater, setCreatedLayoutViewId());
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initImmersionBar();

    }

    protected <M> MutableLiveData<M> registerObserver(Class<M> tClass) {
        return registerObserver(tClass, "");
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

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        try {
            mImmersionBar = ImmersionBar.with(this);
        } catch (IllegalArgumentException e) {
            (getThisActivity()).initImmersionBar();
            mImmersionBar = ImmersionBar.with(this);
        }
        try {
            if (getTitleId() != null) {
                if (getTitleId() instanceof View) {
                    mImmersionBar.titleBar((View) getTitleId());
                } else {
                    mImmersionBar.titleBar(Integer.parseInt(getTitleId().toString()));
                }
            } else if (getHeaderLayout() != null) {
                mImmersionBar.titleBar(getHeaderLayout());
            }
            if (isStatusBarDarkFont()) {
                mImmersionBar.statusBarDarkFont(true, 0.2f);
            }
            mImmersionBar.init();
        } catch (Exception e) {
        }
    }

    /**
     * 设置状态栏沉浸式 返回内容只能是id和view
     *
     * @return
     */
    public Object getTitleId() {
        return null;
    }

    /**
     * 状态栏字体深色或亮色，判断设备支不支持状态栏变色来设置状态栏透明度
     *
     * @return
     */
    public boolean isStatusBarDarkFont() {
        return false;
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
        toast(msg);
    }

    /**
     * Back Event
     *
     * @return false则继续向上传递, true则消费掉该事件
     */
    @Override
    public boolean onBackPressedSupport() {
        if (getParentFragment() != null) {
            setFragmentResult(0, null);
            return super.onBackPressedSupport();
        } else {
            finishFramager();
            return true;
        }
    }


    @Override
    public void finishFramager() {
        setFragmentResult(0, null);
        super.finishFramager();
    }

    @Override
    public void onDestroy() {
        try {
            if (mImmersionBar != null) mImmersionBar.destroy();
        } catch (Exception e) {
        }
        if (eventKeys != null && eventKeys.size() > 0) {
            for (int i = 0; i < eventKeys.size(); i++) {
                LiveBus.getDefault().clear(eventKeys.get(i));
            }
        }
        StatService.onPageEnd(getThisContext(), getClassName());
        super.onDestroy();
    }

    protected String getClassName() {
        return getClass().getSimpleName();
    }
}
