package com.wcy.app.lib.dex;

import android.util.Log;

/**
 * LogUtil
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/12/19
 * @description applibrary
 */
public class LogUtil {
    private static String TAG = "FixDexUtil";
    public static boolean isDebug = false;
    public static void log(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

}
