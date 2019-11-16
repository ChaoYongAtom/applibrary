package com.wcy.app.lib.web.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.wcy.app.lib.web.R;
import com.wcy.app.lib.web.MyWebView;

import org.wcy.android.ui.BaseFragment;

/**
 * WebXFragment
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/10/22
 * @description applibrary
 */
public class WebFragment extends BaseFragment {
    TextView title_tv;
    ImageView btn_close;
    private static final String URL = "url";
    protected MyWebView mSuperWebX5;

    public static WebFragment getInstance(String url) {
        WebFragment mSuperWebX5Fragment = new WebFragment();
        if (url != null) {
            Bundle bundle = new Bundle();
            bundle.putString(URL, url);
            mSuperWebX5Fragment.setArguments(bundle);
        }
        return mSuperWebX5Fragment;
    }

    @Override
    public int setCreatedLayoutViewId() {
        return R.layout.fragment_web;
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        btn_close = rootView.findViewById(R.id.btn_close);
        title_tv = rootView.findViewById(R.id.title_tv);
        mSuperWebX5 = rootView.findViewById(R.id.web_view);
        mSuperWebX5.setOntitle(new MyWebView.onTitle() {
            @Override
            public void UpdateTitle(String title) {
                title_tv.setText(title);
            }

            @Override
            public void onProgressChanged(int newProgress) {

            }
        });
        mSuperWebX5.loadUrl(getArguments().getString(URL));
        btn_close.setOnClickListener(v -> {
            finishFramager();
            setFragmentResult(RESULT_OK, null);
        });
        initImmersionBar();
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
        if (mSuperWebX5 != null && mSuperWebX5.canGoBack()) {
            mSuperWebX5.goBack();
        } else {
            finishFramager();
            setFragmentResult(RESULT_OK, null);
        }
    }


    @Override
    public void onDestroy() {
        mSuperWebX5.destroy();
        super.onDestroy();
    }

}
