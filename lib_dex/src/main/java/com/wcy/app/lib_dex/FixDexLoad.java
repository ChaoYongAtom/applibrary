package com.wcy.app.lib_dex;

import android.content.Context;

/**
 * FixDexLoad
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/11/23
 * @description applibrary
 */
public class FixDexLoad {
    public static void init(Context context, String key) {
        FixDexUtil.getInstance().init(context, key);
    }
}
