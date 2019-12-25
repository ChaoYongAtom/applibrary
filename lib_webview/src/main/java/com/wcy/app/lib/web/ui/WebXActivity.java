package com.wcy.app.lib.web.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.wcy.app.lib.web.R;

import org.wcy.android.ui.BaseActivity;

/**
 * WebXActivity
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/11/15
 * @description applibrary
 */
public class WebXActivity extends BaseActivity {
    public static final String URL = "url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_main);
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            loadRootFragment(R.id.framelayout, WebFragment.getInstance(getIntent().getStringExtra(URL)));
//        } else {
//            loadRootFragment(R.id.framelayout, WebXFragment.getInstance(getIntent().getStringExtra(URL)));
//        }
        loadRootFragment(R.id.framelayout, WebXFragment.getInstance(getIntent().getStringExtra(URL)));

    }
}
