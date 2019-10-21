package com.androidlibrary.myapplication;


import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ruiyun.comm.library.ui.BaseActivity;

public class WebActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        loadRootFragment(R.id.frameLayout_view, WebFragment.getInstance(null));
    }
}
