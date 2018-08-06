package com.ruiyun.comm.library.ui;

import android.content.Intent;
import android.os.Bundle;

import org.wcy.android.R;

import me.yokeyword.fragmentation.SupportFragment;


/**
 * CommonActivity
 * 通用activity 展示fragment
 * 2017-06-05
 * wcy
 * 重庆锐云科技有限公司
 * 友商云V1.0
 */
public class CommonActivity extends BaseActivity {
    public static final String EXTRA_FRAGMENT = "fragmentName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        initView();
    }

    protected void initView() {
        Intent intent = getIntent();
        try {
            String fragmentClazz = intent
                    .getStringExtra(EXTRA_FRAGMENT);
            SupportFragment fragment = (SupportFragment) Class.forName(fragmentClazz)
                    .newInstance();
            loadRootFragment(R.id.common_frame, fragment);
        } catch (Exception e) {
        }
    }

}
