package org.wcy.android.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.wcy.android.R;


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
            String fragmentClazz = intent.getStringExtra(EXTRA_FRAGMENT);
            Fragment fragment = (Fragment) Class.forName(fragmentClazz).newInstance();
            fragment.setArguments(intent.getExtras());
            if (fragment instanceof BaseFragment) {
                loadRootFragment(R.id.common_frame, (BaseFragment) fragment);
            } else {
                //实例化碎片管理器对象
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                //选择fragment替换的部分
                ft.replace(R.id.common_frame, fragment);
                ft.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
