package com.ruiyun.comm.library.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.gyf.immersionbar.ImmersionBar;

import org.wcy.android.R;
import org.wcy.android.view.X5WebView;

/**
 * Created by wcy on 2017/8/2.
 */

public class WebFragment extends BaseMFragment {
    public static final String URL = "url";
    ImageView btn_close;
    TextView title_tv;
    X5WebView webView;
    protected ImmersionBar mImmersionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setView(inflater, R.layout.fragment_web);
    }

    @Override
    protected void initView() {
        btn_close = rootView.findViewById(R.id.btn_close);
        title_tv = rootView.findViewById(R.id.title_tv);
        webView = rootView.findViewById(R.id.webView);

        webView.loadUrl(getArguments().getString(URL));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        webView.setOntitle(new X5WebView.onTitle() {
            @Override
            public void UpdateTitle(String title) {
                if (title_tv != null) title_tv.setText(title);
            }

            @Override
            public void onProgressChanged(int newProgress) {
            }
        });
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

    @Override
    public boolean onBackPressedSupport() {
        goback();
        return true;
    }

    @Override
    public void onDestroy() {
        try {
            if (mImmersionBar != null) mImmersionBar.destroy(getThisFragment());
        } catch (Exception e) {
        }
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }

    private void goback() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            finishFramager();
        }
    }

}
