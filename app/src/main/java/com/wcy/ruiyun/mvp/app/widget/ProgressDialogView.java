package com.wcy.ruiyun.mvp.app.widget;

import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.wcy.ruiyun.mvp.app.R;

import org.wcy.android.retrofit.subscribers.ProgressDialogUtil;
import org.wcy.android.utils.StringUtil;

/**
 * Created by admin on 2018/1/18.
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
        if (StringUtil.hasText(msg)) {
            tv.setText(msg);
        }
    }
}
