package com.wcy.app.lib.web.utils;

import android.content.Context;
import android.content.Intent;
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
    public static void load(Context context, String url) {
        Intent intent = new Intent(context, WebXActivity.class);
        intent.putExtra(WebXActivity.URL, url);
        context.startActivity(intent);
    }
}
