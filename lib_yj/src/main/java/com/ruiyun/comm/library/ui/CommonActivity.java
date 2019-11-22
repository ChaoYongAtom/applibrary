package com.ruiyun.comm.library.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ruiyun.comm.library.common.JConstant;

import org.wcy.android.ui.BaseFragment;

/**
 * CommonActivity
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/11/9
 * @description applibrary
 */
public class CommonActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.wcy.android.R.layout.activity_common);
        initView();
    }

    protected void initView() {
        Intent intent = getIntent();
        try {
            String fragmentClazz = intent.getStringExtra(JConstant.EXTRA_FRAGMENT);
            Fragment fragment = (Fragment) Class.forName(fragmentClazz).newInstance();
            fragment.setArguments(intent.getExtras());
            if (fragment instanceof org.wcy.android.ui.BaseFragment) {
                loadRootFragment(org.wcy.android.R.id.common_frame, (BaseFragment) fragment);
            } else {
                //实例化碎片管理器对象
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                //选择fragment替换的部分
                ft.replace(org.wcy.android.R.id.common_frame, fragment);
                ft.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
