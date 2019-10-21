package com.ruiyun.comm.library.ui;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.ruiyun.comm.library.R;

import org.wcy.android.ui.BaseFragment;

/**
 * Created by wcy on 2017/8/2.
 */

public class WebFragment extends BaseFragment {
    public static final String URL = "url";
    ImageView btn_close;
    TextView title_tv;
    WebView webView;
    @Override
    public int setCreatedLayoutViewId() {
        return 0;
    }

    @Override
    public String setTitle() {
        return null;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return setView(inflater, R.layout.fragment_web);
//    }
//
//    @Override
//    protected void initView() {
//        btn_close = rootView.findViewById(R.id.btn_close);
//        title_tv = rootView.findViewById(R.id.title_tv);
//        webView = rootView.findViewById(R.id.webView);
//
//        webView.loadUrl(getArguments().getString(URL));
//        btn_close.setOnClickListener(v -> finishActivity());
//    }


    @Override
    public boolean onBackPressedSupport() {
        goback();
        return true;
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }

    private void goback() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            finishFramager();
        }
    }

}
