package com.wcy.app.lib.refreshlayout;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * QuickAdapter
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2020/1/20
 * @description applibrary
 */
public abstract class CommonRecyclerAdapter<T> extends BaseQuickAdapter<T, ViewRecyclerHolder> {
    public CommonRecyclerAdapter(int itemLayoutId, List<T> datas) {
        super(itemLayoutId);
        addData(datas);
    }
}
