package org.wcy.android.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.wcy.android.R;
import org.wcy.android.view.dialog.effects.BaseEffects;
import org.wcy.android.view.dialog.effects.Effectstype;

/**
 * Created by wcy on 2014/7/30.
 */
public class NiftyDialogBuilder extends Dialog implements DialogInterface {

    private Effectstype type = null;
    private View mDialogView;
    private int mDuration = -1;

    private volatile static NiftyDialogBuilder instance;

    public NiftyDialogBuilder(Context context) {
        super(context);
    }

    public NiftyDialogBuilder(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static NiftyDialogBuilder getInstance(Context context) {
        instance = new NiftyDialogBuilder(context, R.style.dialog_untran);
        return instance;

    }

    public NiftyDialogBuilder setView(View view) {
        mDialogView = ((ViewGroup) view).getChildAt(0);
        setContentView(view);
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                if (type == null) {
                    type = Effectstype.Slidetop;
                }
                start(type);

            }
        });
        return this;
    }


    public NiftyDialogBuilder withDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    public NiftyDialogBuilder withEffect(Effectstype type) {
        this.type = type;
        return this;
    }

    public NiftyDialogBuilder isCancelableOnTouchOutside(boolean cancelable) {
        this.setCanceledOnTouchOutside(cancelable);
        return this;
    }

    public NiftyDialogBuilder isCancelable(boolean cancelable) {
        this.setCancelable(cancelable);
        return this;
    }

    @Override
    public void show() {
        if (!isShowing())
            super.show();
    }

    private void start(Effectstype type) {
        BaseEffects animator = type.getAnimator();
        if (mDuration != -1) {
            animator.setDuration(Math.abs(mDuration));
        }
        animator.start(mDialogView);
    }

    @Override
    public void dismiss() {
        if (isShowing())
            super.dismiss();
    }
}
