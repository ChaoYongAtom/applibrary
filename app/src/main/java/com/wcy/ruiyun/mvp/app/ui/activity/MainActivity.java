package com.wcy.ruiyun.mvp.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.lzh.compiler.parceler.BundleFactory;
import com.lzh.compiler.parceler.Parceler;
import com.wcy.ruiyun.mvp.app.R;
import com.wcy.ruiyun.mvp.app.model.entity.User;
import com.wcy.ruiyun.mvp.app.ui.common.BaseActivity;
import com.wcy.ruiyun.mvp.app.ui.fragment.LoginFragment;

import org.wcy.android.view.toast.ToastUtils;

public class MainActivity extends BaseActivity {

    Button btn_login;
    BundleFactory factory;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login=findViewById(R.id.btn_login);
        factory = Parceler.createFactory(new Bundle());
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(MainActivity.this, "数据异常", ToastUtils.NO);
//                factory.put("user",new User("测试数据",66));
//                Intent intent=new Intent(MainActivity.this,ParcelerActivity.class);
//                intent.putExtras(factory.getBundle());
//                startActivity(intent);

            }
        });
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.frameLayout, LoginFragment.newInstance());
//        fragmentTransaction.commit();
    }
}
