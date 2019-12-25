package com.wcy.app.lib.web.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.tencent.smtt.sdk.QbSdk;
import com.wcy.app.lib.web.R;
import com.wcy.app.lib.web.SuperWebX5;

import org.wcy.android.ui.BaseFragment;

import me.yokeyword.fragmentation.anim.DefaultNoAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * WebXFragment
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/10/22
 * @description applibrary
 */
public class WebXFragment extends BaseFragment {
    TextView title_tv;
    ImageView btn_close;
    private static final String URL = "url";
    protected SuperWebX5 mSuperWebX5;

    public static WebXFragment getInstance(String url) {
        WebXFragment mSuperWebX5Fragment = new WebXFragment();
        if (url != null) {
            Bundle bundle = new Bundle();
            bundle.putString(URL, url);
            mSuperWebX5Fragment.setArguments(bundle);
        }
        return mSuperWebX5Fragment;
    }

    @Override
    public int setCreatedLayoutViewId() {
        return R.layout.fragment_webx;
    }
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultNoAnimator();
    }
    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        btn_close = rootView.findViewById(R.id.btn_close);
        title_tv = rootView.findViewById(R.id.title_tv);
        btn_close.setOnClickListener(v -> {
            finishFramager();
            setFragmentResult(RESULT_OK, null);
        });
        initImmersionBar();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            QbSdk.forceSysWebView();
        }
        mSuperWebX5 = SuperWebX5.create(this, rootView, title_tv, getArguments().getString(URL));
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this).titleBar(R.id.title_layout).statusBarDarkFont(true, 0.2f);
        mImmersionBar.init();
    }

    @Override
    public boolean onBackPressedSupport() {
        goback();
        return true;
    }

    private void goback() {
        if (mSuperWebX5 != null && mSuperWebX5.back()) {
            mSuperWebX5.back();
        } else {
            mSuperWebX5.clearWebCache();
            finishFramager();
            setFragmentResult(RESULT_OK, null);
        }
    }

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
    public void onDestroy() {
        mSuperWebX5.destroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSuperWebX5.uploadFileResult(requestCode, resultCode, data);
    }
}
