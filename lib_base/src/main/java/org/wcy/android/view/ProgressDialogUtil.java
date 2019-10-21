package org.wcy.android.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import org.wcy.android.R;
import org.wcy.android.utils.RxDataTool;

/**
 * dialog提示框（也包含圆形提示框ProgressDialogUtil）
 *
 * @author wangchaoyong
 */
public class ProgressDialogUtil {
    private AlertDialog pgd;
    private String msg = "正在加载中......";

    /**
     * 是否可以返回
     *
     * @param falg
     */
    public void setCanselable(boolean falg) {
        pgd.setCancelable(falg);
    }

    /**
     * 设置提示信息
     *
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 默认是不能返回的
     *
     * @param context
     */
    public ProgressDialogUtil(Context context) {
        init(context);
        pgd.setCancelable(false);
    }

    private void init(Context context) {
        pgd = new AlertDialog.Builder(context, R.style.dialog_untran).create();
    }

    protected int getLayoutId() {
        return R.layout.view_progress;
    }


    protected void setView(Window window) {
        TextView tv = window.findViewById(R.id.textViewMessage);
        if (!RxDataTool.isNullString(msg)) {
            tv.setText(msg);
        }
    }

    /**
     * 显示提示
     */
    public void show() {
        if (pgd != null && !pgd.isShowing()) {
            pgd.show();
            Window window = pgd.getWindow();
            window.setContentView(getLayoutId());
            setView(window);
        }
    }

    /**
     * 隐藏提示
     */
    public void hide() {
        // 显示则隐藏
        if (pgd.isShowing()) {
            pgd.cancel();
        }
    }
}
