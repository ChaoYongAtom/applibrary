package com.androidlibrary.myapplication;


import android.os.Bundle;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ruiyun.comm.library.ui.BaseActivity;
import com.wcy.app.lib.refreshlayout.CommonRecyclerAdapter;
import com.wcy.app.lib.refreshlayout.EmptyLayout;
import com.wcy.app.lib.refreshlayout.MaterialRefreshListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RefreshLayoutListActivity extends BaseActivity {
    EmptyLayout emptyLayout;
    private CommonRecyclerAdapter mAdapter;
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_list);
        for (int i = 0; i < 20; i++) {
            list.add("asdfaf");
        }
        emptyLayout = findViewById(R.id.emptylayout);
        emptyLayout.setOnRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh() {
                emptyLayout.getPullToRefreshView().finishRefresh(2000/*,false*/);//传入false表示刷新失败
                emptyLayout.showEmpty("暂无数据");
            }

            @Override
            public void onRefreshLoadMore() {
                emptyLayout.getPullToRefreshView().finishLoadMore(2000);//传入false表示加载失败
                emptyLayout.showError("加载错误");
            }
        });
        mAdapter = new CommonRecyclerAdapter(R.layout.refresh_layout_item, list) {

            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, @Nullable Object o) {

            }
        };
        emptyLayout.setAdapter(mAdapter);
    }
}
