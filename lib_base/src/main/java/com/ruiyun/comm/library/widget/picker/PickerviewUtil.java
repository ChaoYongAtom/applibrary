package com.ruiyun.comm.library.widget.picker;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ruiyun.comm.library.widget.picker.adapter.ArrayWheelAdapter;
import com.ruiyun.comm.library.widget.picker.view.TimePickerView;
import com.ruiyun.comm.library.widget.picker.view.WheelView;

import org.wcy.android.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * --------------------------------------------
 * Create By :  Lvfq
 * Date ： 2016/8/25 0025 下午 5:50
 * -------------------------------------------
 **/
public class PickerviewUtil {

    /**
     * 时间选择回调
     */
    public interface TimerPickerCallBack {
        void onTimeSelect(String date);
    }

    /**
     * 弹出时间选择
     *
     * @param context
     * @param type     TimerPickerView 中定义的 选择时间类型
     * @param format   时间格式化
     * @param callBack 时间选择回调
     */
    public static void alertTimerPicker(Context context, Date date, TimePickerView.Type type, final String format, final TimerPickerCallBack callBack) {
        TimePickerView pvTime = new TimePickerView(context, type);
        //控制时间范围
        //        Calendar calendar = Calendar.getInstance();
        //        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));
        if (date != null) {
            pvTime.setTime(date);
        } else {
            pvTime.setTime(new Date());
        }
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
//                        tvTime.setText(getTime(date));
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                callBack.onTimeSelect(sdf.format(date));
            }
        });
        pvTime.setTextSize(16);
        //弹出时间选择器
        pvTime.show();
    }


    /**
     * 底部滚轮点击事件回调
     */
    public interface OnWheelViewClick<T> {
        void onClick(View view, int postion, T iteam);
    }

    public static void alertBottomWheelOption(Context context, int postion, final List<?> list, final OnWheelViewClick click) {
        alertBottomWheelOption(context, "", postion, list, click);
    }

    /**
     * 弹出底部滚轮选择
     *
     * @param context
     * @param list
     * @param click
     */
    public static void alertBottomWheelOption(Context context, String title, int postion, final List<?> list, final OnWheelViewClick click) {

        final PopupWindow popupWindow = new PopupWindow();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_bottom_wheel_option, null);
        TextView tv_confirm = view.findViewById(R.id.btnSubmit);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        final WheelView wv_option = view.findViewById(R.id.wv_option);
        wv_option.setAdapter(new ArrayWheelAdapter(list));
        wv_option.setCyclic(false);
        wv_option.setTextSize(16);
        wv_option.setCurrentItem(postion);
        tv_confirm.setOnClickListener(view1 -> {
            popupWindow.dismiss();
            click.onClick(view1, wv_option.getCurrentItem(), list.get(wv_option.getCurrentItem()));
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(view12 -> {
            // TODO: 2016/8/11 0011 取消
            popupWindow.dismiss();
        });
        view.setOnTouchListener((view13, motionEvent) -> {
            int top = view13.findViewById(R.id.ll_container).getTop();
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                int y = (int) motionEvent.getY();
                if (y < top) {
                    popupWindow.dismiss();
                }
            }
            return true;
        });
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.showAtLocation(((ViewGroup) ((Activity) context).findViewById(android.R.id.content)).getChildAt(0), Gravity.CENTER, 0, 0);
    }
}
