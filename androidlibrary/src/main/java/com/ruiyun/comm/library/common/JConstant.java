package com.ruiyun.comm.library.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.ruiyun.comm.library.mvvm.RxSubscriber;
import com.ruiyun.comm.library.mvvm.rx.HttpHelper;

import org.wcy.android.utils.AESOperator;
import org.wcy.android.utils.ExampleUtil;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.utils.RxDataTool;
import org.wcy.android.utils.RxLogTool;
import org.wcy.android.utils.RxTool;

/**
 * 系统变量
 */
public class JConstant {
    public final static int MIN_PAGE_ROWS = 20;
    private static boolean encrypt = true;
    public final static String heards = "heards";
    public static String VersionName="newestversion";
    private static LoinOutInterface loinOutInterface;
    private static String httpUrl;
    private static String token;
    private static Class httpPostService;
    private static String registrationID;
    private static int connectionTime = 15;
    private static String watermarkStr;
    public final static String isWaterMark = "isWaterMark";
    public static int waterMarkAlpha = 80;
    public static String waterMarkColor = "#e8e8e8e8";
    public static int waterMarkfontSize = 12;
    public static int waterMarkdegress = -15;
    private static boolean isHeaders = true;
    private static String heardsVal = "";

    private static Class<? extends RxSubscriber> rxsubscriber;

    public static Class<? extends RxSubscriber> getRxsubscriber() {
        return rxsubscriber;
    }

    public static void setRxsubscriber(Class<? extends RxSubscriber> rxsubscriber) {
        JConstant.rxsubscriber = rxsubscriber;
    }

    public static boolean isEncrypt() {
        return JConstant.encrypt;
    }

    public static void setEncrypt(boolean encrypt) {
        JConstant.encrypt = encrypt;
    }


    public static LoinOutInterface getLoinOutInterface() {
        return loinOutInterface;
    }

    public static void setLoinOutInterface(LoinOutInterface loinOutInterface) {
        JConstant.loinOutInterface = loinOutInterface;
    }

    public interface LoinOutInterface {
        public void loginOut(Context context, int code, String msg);
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        JConstant.token = token;
    }

    public static void setHttpUrl(String httpUrl) {
        JConstant.httpUrl = httpUrl;

    }

    public static String getHttpUrl() {
        if (RxDataTool.isNullString(httpUrl)) {
            try {
                ApplicationInfo appInfo = RxTool.getContext().getPackageManager().getApplicationInfo(RxTool.getContext().getPackageName(), PackageManager.GET_META_DATA);
                Bundle bundle = appInfo.metaData;
                httpUrl = bundle.getString("HTTP_URL");
                encrypt = !bundle.getBoolean("ENABLE_DEBUG");
                RxLogTool.d("httpUrl","地址初始化成功");
            } catch (Exception e) {
                e.printStackTrace();
                RxLogTool.e("httpUrl","地址初始化失败");
            }
        }
        return httpUrl;
    }

    public static Class getHttpPostService() {
        return httpPostService;
    }

    public static int getConnectionTime() {
        return connectionTime;
    }

    public static void setConnectionTime(int connectionTime) {
        JConstant.connectionTime = connectionTime;
    }

    public static String getRegistrationID() {
        if (!RxDataTool.isNullString(registrationID)) {
            return registrationID;
        } else {
            return "0";
        }
    }

    public static void setRegistrationID(String registrationID) {
        JConstant.registrationID = registrationID;
    }

    public static String getWatermarkStr() {
        return watermarkStr;
    }

    public static void setWatermarkStr(String watermarkStr) {
        JConstant.watermarkStr = watermarkStr;
    }

    public static void setHttpPostService(Class httpPostService) {
        getHttpUrl();
        JConstant.httpPostService = httpPostService;
        if(!RxDataTool.isNullString(httpUrl)){
            new HttpHelper.Builder()
                    .initOkHttp()
                    .createRetrofit(httpUrl)
                    .build();
        }
    }

    public static boolean isIsHeaders() {
        return isHeaders;
    }

    public static void setIsHeaders(boolean isHeaders) {
        JConstant.isHeaders = isHeaders;
    }

    public static String getHeardsVal() {
        if (RxDataTool.isNullString(heardsVal)) {
            String http = JConstant.getHttpUrl();
            JSONObject object = new JSONObject();
            object.put("systemType", "1");
            object.put("appVersion", RxActivityTool.getAppVersionName());
            object.put("mobileCode", ExampleUtil.getImei(RxTool.getContext()));
            if (!RxDataTool.isNullString(http))
                object.put("version", http.substring(http.indexOf("version"), http.lastIndexOf("/")));
            object.put("registrationID", JConstant.getRegistrationID());
            heardsVal = object.toJSONString();
            if (JConstant.isEncrypt()) {
                heardsVal = AESOperator.encrypt(heardsVal);
            }
        }
        return heardsVal;
    }

    public static void setHeardsVal(String heardsVal) {
        JConstant.heardsVal = heardsVal;
    }
}
