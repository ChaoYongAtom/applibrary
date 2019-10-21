package com.ruiyun.comm.library.ui;

import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.widget.WaterMarkBg;

import org.wcy.android.utils.RxDataTool;

/**
 * 公共baseactivity
 * Created by wcy on 2018/1/18.
 */

public abstract class BaseActivity extends org.wcy.android.ui.BaseActivity {

    private boolean isAdd;
    private FrameLayout waterLayout;

    /**
     * 水印处理
     */
    @Override
    public void onStart() {
        if (isShowWatermark() && !isAdd && !RxDataTool.isNullString(JConstant.getWatermarkStr())) {
            boolean isWaterMark = getIntent().getBooleanExtra(JConstant.isWaterMark, true);
            if (isWaterMark) {
                ViewGroup viewGroup = findViewById(android.R.id.content);
                waterLayout = new FrameLayout(getThisContext());
                ViewGroup.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                waterLayout.setLayoutParams(layoutParams);
                WaterMarkBg waterMarkBg = new WaterMarkBg(getThisContext(), JConstant.getWatermarkStr(), JConstant.waterMarkdegress, JConstant.waterMarkfontSize);
                waterLayout.setBackgroundDrawable(waterMarkBg);
                viewGroup.addView(waterLayout);
            }
            isAdd = true;
        }
        super.onStart();
    }

    public boolean isShowWatermark() {
        return true;
    }

    public FrameLayout getWaterLayout() {
        return waterLayout;
    }
}
