package com.androidlibrary.myapplication;


import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ruiyun.comm.library.ui.BaseActivity;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.wcy.app.lib.refreshlayout.CommonRecyclerAdapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RefreshLayoutActivity extends BaseActivity {
    RefreshLayout refreshLayout;
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

        recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout rl) {
                refreshLayout.finishLoadMore(2000);//传入false表示加载失败
            }
        });
    }
}
