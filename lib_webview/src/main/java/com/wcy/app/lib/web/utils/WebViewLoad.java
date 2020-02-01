package com.wcy.app.lib.web.utils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wcy.app.lib.web.ui.WebXActivity;

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
    }
}
