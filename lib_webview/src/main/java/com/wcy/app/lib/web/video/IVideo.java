package com.wcy.app.lib.web.video;


import android.view.View;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;

public interface IVideo {


    void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback);


    void onHideCustomView();


    boolean isVideoState();

}
