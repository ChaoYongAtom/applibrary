package com.androidlibrary.myapplication;


import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ruiyun.comm.library.ui.BaseActivity;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.wcy.app.lib.refreshlayout.CommonRecyclerAdapter;
import com.wcy.app.lib.refreshlayout.EmptyLayout;
import com.wcy.app.lib.refreshlayout.MaterialRefreshListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RefreshLayoutActivity extends BaseActivity {
    EmptyLayout emptyLayout;
    private CommonRecyclerAdapter mAdapter;
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        for (int i = 0; i < 20; i++) {
            list.add("asdfaf");
        }
        mAdapter = new CommonRecyclerAdapter(R.layout.refresh_layout_item, list) {

            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, @Nullable Object o) {

            }
        };
        emptyLayout=findViewById(R.id.emptylayout);
       emptyLayout.setOnRefreshListener(new MaterialRefreshListener() {
           @Override
           public void onRefresh() {
               emptyLayout.getPullToRefreshView().finishRefresh(2000/*,false*/);//传入false表示刷新失败
           }

           @Override
           public void onRefreshLoadMore() {
               emptyLayout.getPullToRefreshView().finishLoadMore(2000);//传入false表示加载失败
           }
       });
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }
}
