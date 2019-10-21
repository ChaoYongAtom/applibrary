package com.androidlibrary.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import com.gyf.immersionbar.ImmersionBar;
import com.ruiyun.comm.library.ui.BaseMFragment;
import com.wcy.app.lib.web.SuperWebX5;
import com.wcy.app.lib.web.client.ChromeClientCallbackManager;

/**
 * WebFragment
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/10/21
 * @description applibrary
 */
public class WebFragment extends BaseMFragment {
    protected SuperWebX5 mSuperWebX5;
    TextView title_tv;
    public static WebFragment getInstance(Bundle bundle) {

        WebFragment mSuperWebX5Fragment = new WebFragment();
        if (bundle != null) {
            mSuperWebX5Fragment.setArguments(bundle);
        }

        return mSuperWebX5Fragment;

    }

    @Override
    public int setCreatedLayoutViewId() {
        return R.layout.fragment_super_web;
    }

    @Override
    protected void initView() {
        super.initView();
        title_tv=rootView.findViewById(R.id.title_tv);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String url = "http://prwap.hejuzg.cn/ww/wap/h5/auth?h5ModuleNo=%7b%22moduleNo%22%3a1%7d&terminalId=33&token=67ba5cc83c1028082f19137d409d7a25";
        mSuperWebX5 = SuperWebX5.create(this, view, mCallback, url);
    }

    /**
     * 获取网页title
     */
    protected ChromeClientCallbackManager.ReceivedTitleCallback mCallback = (view, title) -> {
        title_tv.setText(title);
    };


    @Override
    public void onResume() {
        mSuperWebX5.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mSuperWebX5.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Info", "onActivityResult -- >callback:" + requestCode + "   0x254:" + 0x254);
        mSuperWebX5.uploadFileResult(requestCode, resultCode, data);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initImmersionBar();

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
        mImmersionBar.titleBar(R.id.title_layout).statusBarDarkFont(true, 0.2f).init();
    }

}
