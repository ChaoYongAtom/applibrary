package com.androidlibrary.myapplication.andfix;

import android.content.Context;
import android.widget.Toast;

/**
 * BugClass
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/11/5
 * @description applibrary
 */
public class BugClass {
    public static void show(Context context){
        Toast.makeText(context,"这是一个优美的bug！",Toast.LENGTH_SHORT).show();
    }
}
