package com.ruiyun.comm.library.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.baidu.mobstat.StatService;
import com.gyf.immersionbar.ImmersionBar;
import com.ruiyun.comm.library.mvvm.BaseListVo;
import com.ruiyun.comm.library.mvvm.BaseViewModel;
import com.ruiyun.comm.library.mvvm.LoadObserver;
import com.ruiyun.comm.library.mvvm.event.LiveBus;
import com.ruiyun.comm.library.mvvm.interfaces.LoadInterface;
import com.ruiyun.comm.library.utils.ParameterizedTypeUtil;
import com.ruiyun.comm.library.utils.TurnFragmentUtil;

import org.wcy.android.utils.RxDataTool;

import java.util.ArrayList;
import java.util.List;

public class BaseMFragment<T extends BaseViewModel> extends LibFragment implements LoadInterface {
    protected T mViewModel;
    protected ImmersionBar mImmersionBar;
    private List<String> eventKeys = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        StatService.onPageStart(getThisContext(), getClassName() + (RxDataTool.isNullString(setTitle()) ? "" : setTitle()));
    }

    private void initViewModel() {
        mViewModel = ParameterizedTypeUtil.VMProviders(this);
        if (null != mViewModel && !mViewModel.getClass().getSimpleName().equals(BaseViewModel.class.getSimpleName())) {
            mViewModel.setFragmentName(getClassName());
            mViewModel.loadState.observe(this, new LoadObserver(this));
            dataObserver();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewModel == null) {
            initViewModel();
        }
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

    protected <M> MutableLiveData<BaseListVo<M>> registerObservers(Class<M> tClass, String tag) {
        String event = getClassName().concat(tClass.getSimpleName()).concat("list");
        event = event.concat(tag);
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
        return "";
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

    /**
     * 跳转到一个新的activiy的fragment
     *
     * @param cl
     * @param bundle
     */
    public void startActivityToFragment(Class cl, Bundle bundle) {
        TurnFragmentUtil.startFragment(getThisContext(), cl, bundle);
    }


    /**
     * 跳转到一个新的activiy带返回的fragment
     *
     * @param cl
     * @param bundle
     * @param requestCode
     */
    public void startActivityToFragmentForResult(Class cl, Bundle bundle, Integer requestCode) {
        TurnFragmentUtil.startFragmentForResult(getThisActivity(), cl, bundle, requestCode);
    }

    /**
     * fragment切换
     *
     * @param toFragment 目标fragment
     * @param bundle     参数
     */
    public void startFragment(BaseMFragment toFragment, Bundle bundle) {
        toFragment.setArguments(bundle);
        start(toFragment);
    }

    public void startFragmentForResult(BaseMFragment toFragment, Bundle bundle, int requestCode) {
        toFragment.setArguments(bundle);
        startForResult(toFragment, requestCode);
    }

    @Override
    public void finishFramager() {
        setFragmentResult(0, null);
        super.finishFramager();
    }

    /**
     * 得到当前fragment对象
     *
     * @return
     */
    public Fragment getThisFragment() {
        return this;
    }

    @Override
    public void onDestroy() {
        try {
            if (mImmersionBar != null) mImmersionBar.destroy(getThisFragment());
        } catch (Exception e) {
        }
        if (eventKeys != null && eventKeys.size() > 0) {
            for (int i = 0; i < eventKeys.size(); i++) {
                LiveBus.getDefault().clear(eventKeys.get(i));
            }
        }
        StatService.onPageEnd(getThisContext(), getClassName() + (RxDataTool.isNullString(setTitle()) ? "" : setTitle()));
        super.onDestroy();
    }

    protected String getClassName() {
        return getClass().getSimpleName() + getStateEventKey();
    }

    protected String getStateEventKey() {
        return "";
    }
}
