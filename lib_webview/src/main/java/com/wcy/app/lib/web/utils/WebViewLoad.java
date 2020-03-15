package com.wcy.app.lib.web.utils;

import android.app.Activity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wcy.app.lib.web.ui.WebXActivity;

import java.net.URLEncoder;

/**
 * WebViewLoad
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/11/16
 * @description applibrary
 */
public class WebViewLoad {
    public static void load(String url) {
        ARouter.getInstance().build("/webview/web_activity").withString(WebXActivity.URL, url).navigation();
//        try {
//            int i = url.lastIndexOf("/");
//            String substring = url.substring(0, i);
//            String substring1 = url.substring(i + 1);
//            String encode = URLEncoder.encode(substring1, "UTF-8");
//            String playVideo = substring + "/" + encode;
//            ARouter.getInstance().build("/webview/web_activity").withString(WebXActivity.URL, playVideo).navigation();
//        } catch (Exception e) {
//            ARouter.getInstance().build("/webview/web_activity").withString(WebXActivity.URL, url).navigation();
//        }
    }

    public static void load(Activity activity, String url, int requestCode) {
//        try {
//            int i = url.lastIndexOf("/");
//            String substring = url.substring(0, i);
//            String substring1 = url.substring(i + 1);
//            String encode = URLEncoder.encode(substring1, "UTF-8");
//            String playVideo = substring + "/" + encode;
//            ARouter.getInstance().build("/webview/web_activity").withString(WebXActivity.URL, playVideo).navigation(activity, requestCode);
//        } catch (Exception e) {
//            ARouter.getInstance().build("/webview/web_activity").withString(WebXActivity.URL, url).navigation(activity, requestCode);
//        }
    }
}
