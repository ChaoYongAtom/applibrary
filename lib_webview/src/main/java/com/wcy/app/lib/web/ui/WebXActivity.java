package com.wcy.app.lib.web.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
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
@Route(path = "/webview/web_activity")
public class WebXActivity extends BaseActivity {
    public static final String URL = "url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_main);
        loadRootFragment(R.id.framelayout, WebXFragment.getInstance(getIntent().getStringExtra(URL)));
    }
}
