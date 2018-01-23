package org.wcy.android.retrofit.subscribers;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import org.wcy.android.R;
import org.wcy.android.utils.StringUtil;

/**
 * dialog提示框（也包含圆形提示框ProgressDialogUtil）
 *
 * @author wangchaoyong
 */
public abstract class ProgressDialogUtil {
    private AlertDialog pgd;


    /**
     * 是否可以返回
     *
     * @param falg
     */
    public void setCanselable(boolean falg) {
        pgd.setCancelable(falg);
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

    protected abstract int getLayoutId();

    protected abstract void setView(Window window);

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
