package com.wcy.app.lib.web.client;

import android.os.Build;

import androidx.collection.ArrayMap;

import com.tencent.smtt.sdk.WebView;
import com.wcy.app.lib.web.SuperWebX5;


public class WebSecurityControllerImpl implements WebSecurityController<WebSecurityCheckLogic> {

    private WebView mWebView;
    private ArrayMap<String, Object> mMap;
    private SuperWebX5.SecurityType mSecurityType;

    public WebSecurityControllerImpl(WebView view, ArrayMap<String, Object> map, SuperWebX5.SecurityType securityType) {
        this.mWebView = view;
        this.mMap = map;
        this.mSecurityType=securityType;
    }

    @Override
    public void check(WebSecurityCheckLogic webSecurityCheckLogic) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            webSecurityCheckLogic.dealHoneyComb(mWebView);
        }

        if (mMap != null &&mSecurityType== SuperWebX5.SecurityType.strict&& !mMap.isEmpty()) {
            webSecurityCheckLogic.dealJsInterface(mMap,mSecurityType);
        }

    }
}
