package com.ruiyun.comm.library.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.ruiyun.comm.library.listener.BackHandledInterface;

import org.wcy.android.R;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.view.HeaderLayout;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wcy on 2018/1/18.
 */

public abstract class LibFragment extends SupportFragment  {
    private BaseActivity activity;
    private HeaderLayout headerLayout;
    private Unbinder unbinder;
    protected View rootView;
    protected BackHandledInterface mBackHandledInterface;

    public abstract int setCreatedLayoutViewId();

    public abstract String setTitle();

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

    public void finishFramager() {
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

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    public boolean isSupportSwipeBack() {
        return false;
    }

    public String WatermarkStr() {
        return "";
    }

    protected abstract void initTitle(String title);

    protected abstract void initView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) unbinder.unbind();
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
