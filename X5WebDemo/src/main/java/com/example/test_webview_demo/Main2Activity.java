package com.example.test_webview_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.wcy.android.view.EmptyLayout;
import org.wcy.android.view.refresh.MaterialRefreshListener;

public class Main2Activity extends AppCompatActivity {
    EmptyLayout emptyLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        emptyLayout=findViewById(R.id.emptylayout);
        emptyLayout.setOnRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh() {
                emptyLayout.onRefreshComplete();
            }

            @Override
            public void onRefreshLoadMore() {
                emptyLayout.onRefreshComplete();
            }
        });
    }
}
