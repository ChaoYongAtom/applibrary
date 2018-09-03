package org.wcy.android.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gyf.barlibrary.ImmersionBar;

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
    ImmersionBar mImmersionBarDialog;
    private volatile static NiftyDialogBuilder instance;
    private Activity activity;

    public NiftyDialogBuilder(Context context) {
        super(context);

    }

    public NiftyDialogBuilder(AppCompatActivity context, int theme) {
        super(context, theme);
        this.activity = context;
        try {
            mImmersionBarDialog = ImmersionBar.with(context, this, "Bottom");
            mImmersionBarDialog.init();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static NiftyDialogBuilder getInstance(AppCompatActivity context) {
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
        try{
            if (mImmersionBarDialog != null) mImmersionBarDialog.destroy();
        }catch (Exception e){

        }
        if (isShowing())
            super.dismiss();

    }
}
