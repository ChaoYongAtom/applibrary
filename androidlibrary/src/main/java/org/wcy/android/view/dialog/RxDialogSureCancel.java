package org.wcy.android.view.dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import org.wcy.android.R;


/**
 * @author wcy
 * @date 2016/7/19
 * 确认 取消 Dialog
 */
public class RxDialogSureCancel extends RxDialogSure {
    private TextView mTvCancel;

    public RxDialogSureCancel(Context context, int themeResId) {
        super(context, themeResId);
    }

    public RxDialogSureCancel(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public RxDialogSureCancel(Context context) {
        super(context);
    }

    public RxDialogSureCancel(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
    }

    public void setCancel(String strCancel) {
        this.mTvCancel.setText(strCancel);
    }

    public TextView getCancelView() {
        return mTvCancel;
    }

    public void setCancelListener(View.OnClickListener cancelListener) {
        mTvCancel.setOnClickListener(cancelListener);
    }
    @Override
    protected void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure_false, null);
        setView(dialogView);
        mTvCancel = dialogView.findViewById(R.id.tv_cancel);
        setContentView(dialogView);
    }

}
