package com.wcy.app.lib.web.client;


import androidx.collection.ArrayMap;

import com.tencent.smtt.sdk.WebView;
import com.wcy.app.lib.web.SuperWebX5;


public interface WebSecurityCheckLogic {
    void dealHoneyComb(WebView view);

    void dealJsInterface(ArrayMap<String, Object> objects, SuperWebX5.SecurityType securityType);

}
