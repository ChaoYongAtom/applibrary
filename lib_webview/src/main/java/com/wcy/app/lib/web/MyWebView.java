package com.wcy.app.lib.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


public class MyWebView extends WebView {
    private onTitle ontitle;
    private Context mContext;
    private ProgressBar progressbar;
    private WebViewClient client = new WebViewClient() {
        @Override
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

    public WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView webView, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE) progressbar.setVisibility(VISIBLE);
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
    };

    @SuppressLint("SetJavaScriptEnabled")
    public MyWebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        this.mContext = arg0;

        progressbar = new ProgressBar(arg0, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new RelativeLayout.LayoutParams(-1, 5));

//准备progressBar带圆角的背景Drawable
        GradientDrawable progressBg = new GradientDrawable();
//设置绘制颜色
        progressBg.setColor(getResources().getColor(R.color.progressBg));

//准备progressBar带圆角的进度条Drawable
        GradientDrawable progressContent = new GradientDrawable();
//设置绘制颜色，此处可以自己获取不同的颜色
        progressContent.setColor(getResources().getColor(R.color.progressB));

//ClipDrawable是对一个Drawable进行剪切操作，可以控制这个drawable的剪切区域，以及相相对于容器的对齐方式
        ClipDrawable progressClip = new ClipDrawable(progressContent, Gravity.LEFT, ClipDrawable.HORIZONTAL);
//Setup LayerDrawable and assign to progressBar
//待设置的Drawable数组
        Drawable[] progressDrawables = {progressBg, progressClip};
        LayerDrawable progressLayerDrawable = new LayerDrawable(progressDrawables);
//根据ID设置progressBar对应内容的Drawable
        progressLayerDrawable.setId(0, android.R.id.background);
        progressLayerDrawable.setId(1, android.R.id.progress);
//设置progressBarDrawable
        progressbar.setProgressDrawable(progressLayerDrawable);
        addView(progressbar);
        // WebStorage webStorage = WebStorage.getInstance();
        initWebViewSettings();
    }


    private void initWebViewSettings() {
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setWebViewClient(client);
        setWebChromeClient(webChromeClient);
        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
    }

    public void setOntitle(onTitle ontitle) {
        this.ontitle = ontitle;
    }

    public interface onTitle {
        void UpdateTitle(String title);

        void onProgressChanged(int newProgress);
    }

    public MyWebView(Context arg0) {
        super(arg0);
    }

}
