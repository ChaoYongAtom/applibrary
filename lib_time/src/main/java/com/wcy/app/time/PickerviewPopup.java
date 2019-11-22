package com.wcy.app.time;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;

import org.wcy.android.utils.DateUtil;
import org.wcy.android.utils.RxDataTool;

import java.util.Date;

import static org.wcy.android.utils.DateUtil.getDaysOfCurMonth;

/**
 * PickerviewPopup
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/3/8
 * @description YjSales
 */
public abstract class PickerviewPopup extends BottomPopupView {
    private String year = "";
    private String month = "";
    private String day = "";
    private String hour = "";
    private String minte = "";
    private boolean isShowTime;
    private String showData;
    private int minYear;
    private int maxYear;

    public PickerviewPopup(@NonNull Context context,boolean isShowTime, String sData, int minYear, int maxYear) {
        super(context);
        this.minYear = minYear;
        this.maxYear = maxYear;
        this.isShowTime=isShowTime;
        if (minYear <= 0) {
            this.minYear = 1970;
        }
        if (maxYear <= 0) {
            this.maxYear = DateUtil.getDateYear(new Date());
        }
        this.showData = sData;

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_change_time;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        String data;
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
        View viewLine = findViewById(R.id.view_line);
        LinearLayout layoutHour = findViewById(R.id.llayout_hour);
        LinearLayout layoutMinte = findViewById(R.id.llout_minte);

        if (!isShowTime) {
            viewLine.setVisibility(View.GONE);
            layoutHour.setVisibility(View.GONE);
            layoutMinte.setVisibility(View.GONE);
        }

//        TextView tv_change_time = (TextView)  alertDialog.findViewById(R.id.tv_change_time);
//        tv_change_time.setText(DateUtil.toStrDateMin(new Date()) );
        final ChangeTextStyleNumberPicker numPicker_day = findViewById(R.id.numPicker_day);
        numPicker_day.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //年
        ChangeTextStyleNumberPicker numPicker_year = findViewById(R.id.numPicker_year);
        numPicker_year.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numPicker_year.setNumberPickerDividerColor(numPicker_year);
        numPicker_year.setWrapSelectorWheel(false);
        numPicker_year.setMinValue(minYear);
        numPicker_year.setMaxValue(maxYear);
        numPicker_year.setValue(DateUtil.getDateYear(data));


        numPicker_year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                year = format(newVal);

                numPicker_day.setMaxValue(getDaysOfCurMonth(year + "-" + month));
            }
        });

        //月
        ChangeTextStyleNumberPicker numPicker_month = findViewById(R.id.numPicker_month);
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
                int dm = getDaysOfCurMonth(year + "-" + month);
                numPicker_day.setMaxValue(dm);
                int d = Integer.parseInt(day);
                if (d > dm) {
                    day = String.valueOf(dm);
                }
            }
        });
        //日

        numPicker_day.setNumberPickerDividerColor(numPicker_day);
        numPicker_day.setWrapSelectorWheel(false);
        numPicker_day.setMinValue(1);
        numPicker_day.setMaxValue(getDaysOfCurMonth(year + "-" + month));
        int day1 = DateUtil.getDateDay(data);
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
            ChangeTextStyleNumberPicker numPicker_hour = findViewById(R.id.numPicker_hour);
            numPicker_hour.setNumberPickerDividerColor(numPicker_hour);
            numPicker_hour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            numPicker_hour.setWrapSelectorWheel(false);
            numPicker_hour.setMinValue(0);
            numPicker_hour.setMaxValue(23);
            numPicker_hour.setValue(Integer.parseInt(hour));
            numPicker_hour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    hour = format(newVal);
                }
            });
            //分
            ChangeTextStyleNumberPicker numPicker_min = findViewById(R.id.numPicker_min);
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
        TextView tv_dimss = findViewById(R.id.tv_dimss);//取消
        TextView tv_sure = findViewById(R.id.tv_sure);//确定

        tv_dimss.setOnClickListener(new DialogClick());
        tv_sure.setOnClickListener(new DialogClick());
    }

    public String format(int value) {
        String Str = String.valueOf(value);
        if (value < 10) {
            Str = "0" + Str;
        }
        return Str;
    }

    /**
     * dialog 弹出框
     **/
    private class DialogClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.tv_dimss) {
                dismiss();
            } else if (i == R.id.tv_sure) {
                if (day.equals("00")) {
                    day = "01";
                }
                if (isShowTime) {
                    selectData(year + "-" + month + "-" + day + " " + hour + ":" + minte);
                } else {
                    selectData(year + "-" + month + "-" + day);
                }

            }

        }
    }

    public abstract void selectData(String date);

    /**
     * 弹出底部滚轮选择
     *
     * @param context
     */
    public static void show(Context context, String sData, int minYear, int maxYear) {

    }
}
