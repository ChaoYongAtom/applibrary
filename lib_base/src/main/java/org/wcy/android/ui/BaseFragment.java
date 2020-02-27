package org.wcy.android.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gyf.immersionbar.ImmersionBar;

import org.wcy.android.interfaces.BackHandledInterface;

import org.wcy.android.R;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.view.HeaderLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by wcy on 2018/1/18.
 */

public abstract class BaseFragment extends SupportFragment {
    private BaseActivity activity;
    private HeaderLayout headerLayout;
    private Unbinder unbinder;
    protected View rootView;
    protected BackHandledInterface mBackHandledInterface;
    protected ImmersionBar mImmersionBar;

    public int setCreatedLayoutViewId() {
        return 0;
    }

    public String setTitle() {
        return "";
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.finishInputWindow();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity) _mActivity;
        if (!(getActivity() instanceof BackHandledInterface)) {
            // throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        } else {
            this.mBackHandledInterface = (BackHandledInterface) getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setView(inflater, setCreatedLayoutViewId());
    }

    public void finishFramager() {
        setFragmentResult(0, null);
        if (activity.getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            RxActivityTool.finishActivity(activity);
        } else {
            pop();
        }
        activity.finishInputWindow();
    }

    public void finishActivity() {
        RxActivityTool.finishActivity(getActivity());
    }

    /**
     * fragment切换
     *
     * @param toFragment 目标fragment
     * @param bundle     参数
     */
    public void startFragment(BaseFragment toFragment, Bundle bundle) {
        toFragment.setArguments(bundle);
        start(toFragment);
    }

    public void startFragmentForResult(BaseFragment toFragment, Bundle bundle, int requestCode) {
        toFragment.setArguments(bundle);
        startForResult(toFragment, requestCode);
    }

    public BaseActivity getThisActivity() {
        return activity;
    }

    public void toast(Object obj) {
        activity.toast(obj);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (headerLayout != null) {
            headerLayout.getNavigationView().setOnClickListener(v -> finishFramager());
        }
        initImmersionBar();
        initView();
    }

    public View setView(LayoutInflater inflater, int layoutId) {
        return setView(inflater, layoutId, null);
    }

    public View setView(LayoutInflater inflater, int layoutId, String title) {
        return setView(inflater, null, layoutId, title);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mBackHandledInterface != null) {
            //告诉FragmentActivity，当前Fragment在栈顶
            mBackHandledInterface.setSelectedFragment(this);
        }

    }

    public View setView(LayoutInflater inflater, ViewGroup container, int layoutId, String title) {
        if (rootView == null) {
            rootView = inflater.inflate(layoutId, container, false);
            headerLayout = rootView.findViewById(R.id.headerlayout);
            if (headerLayout != null) headerLayout.setTitleText(setTitle());
            unbinder = ButterKnife.bind(this, rootView);
            initTitle(title);
        }
        return rootView;
    }


    protected void initTitle(String title) {

    }

    protected void initView() {

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
    public void onDestroy() {
        try {
            if (mImmersionBar != null) mImmersionBar.destroy(getThisFragment());
        } catch (Exception e) {
        }
        if (unbinder != null) unbinder.unbind();
        super.onDestroy();
    }


    /**
     * 得到当前fragment对象
     *
     * @return
     */
    public Fragment getThisFragment() {
        return this;
    }


    public void toastError(Object obj) {
        activity.toastError(obj);
    }

    public void toastSuccess(Object obj) {
        activity.toastSuccess(obj);
    }

    public Context getThisContext() {
        return getThisActivity().getThisContext();
    }

    public HeaderLayout getHeaderLayout() {
        return headerLayout;
    }

}
