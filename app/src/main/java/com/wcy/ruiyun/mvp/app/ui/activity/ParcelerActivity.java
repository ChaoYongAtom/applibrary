package com.wcy.ruiyun.mvp.app.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.lzh.compiler.parceler.BundleFactory;
import com.lzh.compiler.parceler.Parceler;
import com.wcy.ruiyun.mvp.app.R;
import com.wcy.ruiyun.mvp.app.model.entity.User;

public class ParcelerActivity extends AppCompatActivity {

    TextView tv_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parceler);
        tv_msg = findViewById(R.id.tv_msg);
        BundleFactory factory = Parceler.createFactory(getIntent().getExtras());
        User user = factory.get("user", User.class);
        tv_msg.setText(user.name);
        tv_msg.append(user.id + "");
    }
}
