package com.wcy.ruiyun.mvp.app.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.wcy.ruiyun.mvp.app.R;
import com.wcy.ruiyun.mvp.app.ui.common.BaseActivity;
import com.wcy.ruiyun.mvp.app.ui.fragment.LoginFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, LoginFragment.newInstance());
        fragmentTransaction.commit();
    }
}
