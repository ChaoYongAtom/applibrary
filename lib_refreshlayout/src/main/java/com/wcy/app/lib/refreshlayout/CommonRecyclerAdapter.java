package com.wcy.app.lib.refreshlayout;

import android.content.Context;

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
    List<T> tList = null;

    public CommonRecyclerAdapter(int itemLayoutId, List<T> datas) {
        super(itemLayoutId);
        init(datas);
    }

    public CommonRecyclerAdapter(List<T> datas, int itemLayoutId) {
        super(itemLayoutId);
        init(datas);
    }

    public CommonRecyclerAdapter(Context context, List<T> datas, int itemLayoutId) {
        super(itemLayoutId);
        init(datas);
    }

    private void init(List<T> datas) {
        tList = datas;
    }

    /**
     *
     */
    public void adaperNotifyDataSetChanged(){
        setNewData(tList);
        super.notifyDataSetChanged();
    }
}
