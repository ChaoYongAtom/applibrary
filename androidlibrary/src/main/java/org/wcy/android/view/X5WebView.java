package org.wcy.android.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ProgressBar;

import com.ruiyun.comm.library.common.JConstant;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class X5WebView extends WebView {
    private onTitle ontitle;
    private Context mContext;
    private ProgressBar progressbar;
    private WebViewClient client = new WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                mContext.startActivity(intent);
            } else {
                view.loadUrl(url);
            }
            return true;
        }
    };

    public class WebChromeClient extends com.tencent.smtt.sdk.WebChromeClient {
        @Override
        public void onProgressChanged(WebView webView, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            if (ontitle != null) {
                ontitle.onProgressChanged(newProgress);
            }
            super.onProgressChanged(webView, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView webView, String title) {
            super.onReceivedTitle(webView, title);
            if (ontitle != null) {
                ontitle.UpdateTitle(title);
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        this.mContext = arg0;
        progressbar = new ProgressBar(arg0, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.MATCH_PARENT, 10, 0, 0));
        addView(progressbar);
        this.setWebViewClient(client);
        // this.setWebChromeClient(chromeClient);
        // WebStorage webStorage = WebStorage.getInstance();
        initWebViewSettings();
        this.getView().setClickable(true);
    }
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean ret = super.drawChild(canvas, child, drawingTime);
        if(JConstant.getWatermarkStr().equals("X5")){
            canvas.save();
            Paint paint = new Paint();
            paint.setColor(0x7fff0000);
            paint.setTextSize(24.f);
            paint.setAntiAlias(true);
            if (getX5WebViewExtension() != null) {
                canvas.drawText(this.getContext().getPackageName() + "-pid:"
                        + android.os.Process.myPid(), 10, 50, paint);
                canvas.drawText(
                        "X5  Core:" + QbSdk.getTbsVersion(this.getContext()), 10,
                        100, paint);
            } else {
                String x5CrashInfo = com.tencent.smtt.sdk.WebView.getCrashExtraMessage(getContext());
                Log.i("x5CrashInfo",x5CrashInfo);
                canvas.drawText(this.getContext().getPackageName() + "-pid:"
                        + android.os.Process.myPid(), 10, 50, paint);
                canvas.drawText("Sys Core"+x5CrashInfo, 10, 100, paint);
            }
            canvas.drawText(Build.MANUFACTURER, 10, 150, paint);
            canvas.drawText(Build.MODEL, 10, 200, paint);
            canvas.restore();
        }
        return ret;
    }
    private void initWebViewSettings() {
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setWebChromeClient(new WebChromeClient());
        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
    }
    public void setOntitle(onTitle ontitle) {
        this.ontitle = ontitle;
    }

    public interface onTitle {
        public void UpdateTitle(String title);

        public void onProgressChanged(int newProgress);
    }

    public X5WebView(Context arg0) {
        super(arg0);
    }

}
