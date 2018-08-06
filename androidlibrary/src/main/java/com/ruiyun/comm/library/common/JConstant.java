package com.ruiyun.comm.library.common;

import org.wcy.android.utils.RxDataTool;

/**
 * 系统变量
 */
public class JConstant {
    public final static int INTERVAL = 1000;  //间隔时间（毫秒）
    public final static int MIN_PAGE_ROWS = 20;
    public final static int MAX_PAGE_ROWS = 20;
    public final static String bundleListName = "baseListData";
    private static boolean encrypt = true;
    public final static String heards = "heards";
    private static LoinOutInterface loinOutInterface;
    private static String httpUrl;
    private static String token;
    private static Class httpPostService;
    private static String registrationID;
    private static int connectionTime = 6;
    /* retry次数*/
    private static int retry = 2;

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
        public void loginOut();
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

    public static int getRetry() {
        return retry;
    }

    public static void setRetry(int retry) {
        JConstant.retry = retry;
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

    public static void setHttpPostService(Class httpPostService) {
        JConstant.httpPostService = httpPostService;
    }
}
