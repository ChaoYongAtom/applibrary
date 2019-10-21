package com.wcy.app.lib.web.progress;

import android.view.ViewGroup;

public interface WebCreator extends ProgressManager {
    WebCreator create();

    com.tencent.smtt.sdk.WebView get();

    ViewGroup getGroup();
}
