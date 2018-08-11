package com.ruiyun.comm.library.widget;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;


import org.wcy.android.R;
import org.wcy.android.utils.DateUtil;
import org.wcy.android.utils.RxDataTool;
import org.wcy.android.view.dialog.NiftyDialogBuilder;
import org.wcy.android.view.dialog.effects.Effectstype;

import java.util.Date;

import static org.wcy.android.utils.DateUtil.getDaysOfCurMonth;


/**
 * Created by zhaomaohui on 2017/3/24.
 * 日期选择器
 */

public abstract class ChangeTimeDialogUtils {


    private NiftyDialogBuilder alertDialog;
//    private Dialog alertDialog;

    private String year = "";
    private String month = "";
    private String day = "";
    private String hour = "";
    private String minte = "";

    private boolean isShowTime;
    private AppCompatActivity context;
    private String showData = "";

    /**
     * dialog 创建
     *
     * @param context
     * @param isShowTime 是否显示时间
     */
    public ChangeTimeDialogUtils(AppCompatActivity context, boolean isShowTime) {
        this.context = context;
        this.isShowTime = isShowTime;
    }


    /**
     * 设置显示时间
     **/
    public void setData(String showData) {
        this.showData = showData;
        String data = "";
        if (!RxDataTool.isNullString(showData)) {
            data = showData;
            year = format(DateUtil.getDateYear(data));
            month = format(DateUtil.getDateMonedh(data) + 1);
            day = format(DateUtil.getDateDay(showData));


        } else {
            data = DateUtil.getCurrentDate(DateUtil.DF_YYYY_MM_DD);
            year = format(DateUtil.getDateYear(data));
            month = format(DateUtil.getDateMonedh(data) + 1);
            day = format(DateUtil.getDateDay(data) - 1);
        }
        alertDialog = NiftyDialogBuilder.getInstance(context);
        alertDialog.withEffect(Effectstype.SlideBottom).setCancelable(false);
        View window = View.inflate(context, R.layout.dialog_change_time, null);
        alertDialog.setView(window);


        View viewLine = alertDialog.findViewById(R.id.view_line);
        LinearLayout layoutHour = alertDialog.findViewById(R.id.llayout_hour);
        LinearLayout layoutMinte = alertDialog.findViewById(R.id.llout_minte);

        if (!isShowTime) {
            viewLine.setVisibility(View.GONE);
            layoutHour.setVisibility(View.GONE);
            layoutMinte.setVisibility(View.GONE);
        }

//        TextView tv_change_time = (TextView)  alertDialog.findViewById(R.id.tv_change_time);
//        tv_change_time.setText(DateUtil.toStrDateMin(new Date()) );
        final ChangeTextStyleNumberPicker numPicker_day = alertDialog.findViewById(R.id.numPicker_day);
        numPicker_day.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //年
        ChangeTextStyleNumberPicker numPicker_year = alertDialog.findViewById(R.id.numPicker_year);
        numPicker_year.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numPicker_year.setNumberPickerDividerColor(numPicker_year);
        numPicker_year.setWrapSelectorWheel(false);
        numPicker_year.setMinValue(1970);
        numPicker_year.setMaxValue(DateUtil.getDateYear(new Date()));
        numPicker_year.setValue(DateUtil.getDateYear(data));


        numPicker_year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                year = format(newVal);

                numPicker_day.setMaxValue(getDaysOfCurMonth(year + "-" + month));
            }
        });

        //月
        ChangeTextStyleNumberPicker numPicker_month = alertDialog.findViewById(R.id.numPicker_month);
        numPicker_month.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numPicker_month.setNumberPickerDividerColor(numPicker_month);
        numPicker_month.setWrapSelectorWheel(false);
        numPicker_month.setMinValue(1);
        numPicker_month.setMaxValue(12);

        numPicker_month.setValue(Integer.valueOf(month));

        numPicker_month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                month = format(newVal);
                numPicker_day.setMaxValue(getDaysOfCurMonth(year + "-" + month));
            }
        });
        //日

        numPicker_day.setNumberPickerDividerColor(numPicker_day);
        numPicker_day.setWrapSelectorWheel(false);
        numPicker_day.setMinValue(1);
        numPicker_day.setMaxValue(getDaysOfCurMonth());
        int day1 = DateUtil.getDateDay(data);
        if (RxDataTool.isNullString(showData) && day1 > 1) {
            day1--;
        }
        numPicker_day.setValue(day1);
        numPicker_day.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                day = format(newVal);
            }
        });
        if (isShowTime) {
            hour = format(DateUtil.getDateHour(DateUtil.toDateTime(data, DateUtil.DF_YYYY_MM_DD_HH_MM)));
            minte = format(DateUtil.getDateMinute(DateUtil.toDateTime(data, DateUtil.DF_YYYY_MM_DD_HH_MM)));
            //时
            ChangeTextStyleNumberPicker numPicker_hour = alertDialog.findViewById(R.id.numPicker_hour);
            numPicker_hour.setNumberPickerDividerColor(numPicker_hour);
            numPicker_hour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            numPicker_hour.setWrapSelectorWheel(false);
            numPicker_hour.setMinValue(1);
            numPicker_hour.setMaxValue(24);
            numPicker_hour.setValue(Integer.parseInt(hour));
            numPicker_hour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    hour = format(newVal);
                }
            });
            //分
            ChangeTextStyleNumberPicker numPicker_min = alertDialog.findViewById(R.id.numPicker_min);
            numPicker_min.setNumberPickerDividerColor(numPicker_min);
            numPicker_min.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            numPicker_min.setWrapSelectorWheel(false);
            numPicker_min.setMinValue(0);
            numPicker_min.setMaxValue(59);
            numPicker_min.setValue(Integer.parseInt(minte));
            numPicker_min.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    minte = format(newVal);
                }
            });
        }
        TextView tv_dimss = alertDialog.findViewById(R.id.tv_dimss);//取消
        TextView tv_sure = alertDialog.findViewById(R.id.tv_sure);//确定

        tv_dimss.setOnClickListener(new DialogClick());
        tv_sure.setOnClickListener(new DialogClick());
    }


    public void showDialog(String showData) {
        setData(showData);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    public String format(int value) {
        String Str = String.valueOf(value);
        if (value < 10) {
            Str = "0" + Str;
        }
        return Str;
    }


    public abstract void changeedData(String date);

    /**
     * dialog 弹出框
     **/
    private class DialogClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.tv_dimss) {
                alertDialog.dismiss();
            } else if (i == R.id.tv_sure) {
                if (day.equals("00")) {
                    day = "01";
                }
                if (isShowTime) {
                    changeedData(year + "-" + month + "-" + day + " " + hour + ":" + minte);
                } else {
                    changeedData(year + "-" + month + "-" + day);
                }

            }

        }
    }

    public void hide() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

}
