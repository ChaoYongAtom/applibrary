package com.wcy.app.lib.web.progress;


import com.tencent.smtt.sdk.WebView;

public interface IndicatorController {

    void progress(WebView v, int newProgress);

    BaseProgressSpec offerIndicator();
}
