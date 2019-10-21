package org.wcy.android.utils;

import android.util.Log;

import com.apkfuns.logutils.LogUtils;

public class RxLogTool {
    /**
     * verbose输出
     *
     * @param msg
     * @param args
     */
    public static void v(String msg, String args) {
        Log.v(msg, args);
    }

    /**
     * debug输出
     *
     * @param msg
     * @param args
     */
    public static void d(String msg, String args) {
        if (RxTool.isApkInDebug()&&!RxDataTool.isNullString(msg)&&!RxDataTool.isNullString(args)) Log.d(msg, args);
    }

    /**
     * info输出
     *
     * @param msg
     * @param args
     */
    public static void i(String msg, String args) {
        Log.i(msg, args);
    }

    /**
     * warn输出
     *
     * @param msg
     * @param args
     */
    public static void w(String msg,String args) {
        Log.w(msg, args);
    }

    /**
     * error输出
     *
     * @param msg
     * @param args
     */
    public static void e(String msg, String args) {
        Log.e(msg, args);
    }

    /**
     * 打印json
     *
     * @param json
     */
    public static void json(String json) {
        LogUtils.json(json);
    }

    /**
     * 打印json
     *
     * @param json
     */
    public static void dJson(String json) {
        if (RxTool.isApkInDebug()&&!RxDataTool.isNullString(json)) {
            LogUtils.json(json);
        }
    }

    /**
     * 输出xml
     *
     * @param xml
     */
    public static void xml(String xml) {
        LogUtils.xml(xml);
    }

}
