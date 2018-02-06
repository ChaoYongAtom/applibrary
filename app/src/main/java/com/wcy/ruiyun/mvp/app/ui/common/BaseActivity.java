package com.wcy.ruiyun.mvp.app.ui.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wcy.ruiyun.mvp.app.R;
import com.wcy.ruiyun.mvp.app.api.entitys.BaseResult;
import com.wcy.ruiyun.mvp.app.listerers.BackHandledInterface;
import com.wcy.ruiyun.mvp.app.view.BaseView;

import org.wcy.android.retrofit.exception.ApiException;
import org.wcy.android.utils.ActivityUtil;
import org.wcy.android.utils.AppManager;
import org.wcy.android.utils.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 公共baseactivity
 * Created by wcy on 2018/1/18.
 */

public class BaseActivity extends RxAppCompatActivity implements BackHandledInterface,BaseView {
    Unbinder unbinder;
    private BaseFragment mBackHandedFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ActivityUtil.setScreenVertical(this);//竖屏显示
        finishInputWindow();//隐藏输入法
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        init();
    }

    @Override
    public void setContentView(View view) {
        // TODO Auto-generated method stub
        super.setContentView(view);
        init();
    }

    private void init() {
        setStatusBar();
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null)
            unbinder.unbind();
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBackHandedFragment.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 关闭当前activity
     */
    public void finishActivity() {
        finishInputWindow();
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 关闭软键盘
     **/
    public void finishInputWindow() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    @Override
    public void onNext(BaseResult result) {

    }

    @Override
    public void onError(ApiException e, String mothead) {

    }
}
