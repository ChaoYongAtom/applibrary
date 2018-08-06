package com.ruiyun.comm.library.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruiyun.comm.library.api.entitys.BaseResult;
import com.ruiyun.comm.library.mvp.BaseView;

import org.wcy.android.R;
import org.wcy.android.retrofit.exception.ApiException;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.view.HeaderLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by wcy on 2018/1/18.
 */

public abstract class LibFragment extends SwipeBackFragment implements BaseView {
    private BaseActivity activity;
    private HeaderLayout headerLayout;
    private Unbinder unbinder;
    protected View rootView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.finishInputWindow();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity) _mActivity;
    }

    public void finishFramager() {
        if (activity.getSupportFragmentManager().getBackStackEntryCount() == 1) {
            RxActivityTool.finishActivity(activity);
        } else {
            pop();
        }
        activity.finishInputWindow();
    }

    public void finishActivity() {
        RxActivityTool.finishActivity(getActivity());
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
            headerLayout.getNavigationView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishFramager();
                }
            });
        }
        initView();
    }

    public View setView(LayoutInflater inflater, int layoutId) {
        return setView(inflater, layoutId, null);
    }

    public View setView(LayoutInflater inflater, int layoutId, String title) {
        return setView(inflater, null, layoutId, title);
    }

    public View setView(LayoutInflater inflater, ViewGroup container, int layoutId, String title) {
        if (rootView == null) {
            rootView = inflater.inflate(layoutId, container, false);
            headerLayout = rootView.findViewById(R.id.headerlayout);
            unbinder = ButterKnife.bind(this, rootView);
            initTitle(title);
        }

        return attachToSwipeBack(rootView);
    }

    protected abstract void initTitle(String title);

    protected abstract void initView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) unbinder.unbind();
    }

    @Override
    public void onNext(BaseResult result) {
        toastSuccess(result.getMsg());
    }

    @Override
    public void onError(ApiException e, String mothead) {
        toastError(e.getDisplayMessage());

    }

    public void toastError(Object obj) {
        activity.toastError(obj);
    }

    public void toastSuccess(Object obj) {
        activity.toastSuccess(obj);
    }

    @Override
    public Context getThisContext() {
        return getThisActivity().getThisContext();
    }

    public HeaderLayout getHeaderLayout() {
        return headerLayout;
    }

}
