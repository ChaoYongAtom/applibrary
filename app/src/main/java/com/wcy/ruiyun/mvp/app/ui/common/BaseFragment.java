package com.wcy.ruiyun.mvp.app.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.wcy.ruiyun.mvp.app.listerers.BackHandledInterface;
import com.wcy.ruiyun.mvp.app.presenter.BasePresenter;
import com.wcy.ruiyun.mvp.app.view.BaseView;

import org.wcy.android.utils.AppManager;

import butterknife.ButterKnife;

/**
 * Created by wcy on 2018/1/18.
 */

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {
    protected BackHandledInterface mBackHandledInterface;
    private BaseActivity activity;
    protected T presenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = createPresenter();
        presenter.setInit(activity);
    }

    protected abstract T createPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity) getActivity();
        if (!(getActivity() instanceof BackHandledInterface)) {
            // throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        } else {
            this.mBackHandledInterface = (BackHandledInterface) getActivity();
        }
    }

    public void finishFramager() {
        if (activity.getSupportFragmentManager().getBackStackEntryCount() == 1) {
            AppManager.getAppManager().finishActivity(activity);
        } else {
            AppManager.getAppManager().finishFragment(this, getActivity()
                    .getSupportFragmentManager().beginTransaction());
            activity.getSupportFragmentManager().popBackStack();
        }
        activity.finishInputWindow();
    }

    public void finishActivity() {
        AppManager.getAppManager().finishActivity(activity);
    }

    public BaseActivity getThisActivity() {
        return activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mBackHandledInterface != null) {
            //告诉FragmentActivity，当前Fragment在栈顶
            mBackHandledInterface.setSelectedFragment(this);
        }

    }

    public void toast(Object obj) {
        Toast.makeText(activity, obj.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    public View setView(LayoutInflater inflater, int layoutId) {
        View contentView = inflater.inflate(layoutId, null);
        ButterKnife.bind(this, contentView);
        return contentView;
    }

    protected abstract void initView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
