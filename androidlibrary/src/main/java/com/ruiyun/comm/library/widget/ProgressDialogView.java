package com.ruiyun.comm.library.widget;

import android.content.Context;
import android.view.Window;
import android.widget.TextView;


import org.wcy.android.R;
import org.wcy.android.retrofit.subscribers.ProgressDialogUtil;
import org.wcy.android.utils.RxDataTool;

/**
 * Created by wcy on 2018/1/18.
 */

public class ProgressDialogView extends ProgressDialogUtil {
    private String msg;

    /**
     * 默认是不能返回的
     *
     * @param context
     */
    public ProgressDialogView(Context context) {
        super(context);
    }

    /**
     * 设置提示信息
     *
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_progress;
    }

    @Override
    protected void setView(Window window) {
        TextView tv = window.findViewById(R.id.textViewMessage);
        if (!RxDataTool.isNullString(msg)) {
            tv.setText(msg);
        }
    }
}
