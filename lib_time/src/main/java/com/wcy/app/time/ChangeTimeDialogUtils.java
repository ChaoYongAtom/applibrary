package com.wcy.app.time;


import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

/**
 * Created by wcy on 2017/3/24.
 * 日期选择器
 */

public abstract class ChangeTimeDialogUtils {
    private BasePopupView basePopupView;
    private Context mContext;
    private boolean isShowTime;

    /**
     * dialog 创建
     *
     * @param context
     * @param isShowTime 是否显示时间
     */
    public ChangeTimeDialogUtils(AppCompatActivity context, boolean isShowTime) {
        this.mContext = context;
        this.isShowTime = isShowTime;
    }


    public void showDialog(String showData, int minYear, int maxYear) {
        basePopupView = new XPopup.Builder(mContext).moveUpToKeyboard(false).asCustom(new PickerviewPopup(mContext,isShowTime, showData, minYear, maxYear) {

            @Override
            public void selectData(String date) {
                changeedData(date);
            }
        }).show();
    }

    public void showDialog(String showData) {
        showDialog(showData, 0, 0);
    }


    public abstract void changeedData(String date);


    public void hide() {
        if (basePopupView != null && basePopupView.isShow()) {
            basePopupView.dismiss();
        }
    }

}
