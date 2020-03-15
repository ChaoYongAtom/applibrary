package com.wcy.app.lib.web.ui;

import android.content.Context;
import android.webkit.JavascriptInterface;
import com.jeremyliao.liveeventbus.LiveEventBus;

import org.wcy.android.utils.RxDataTool;


/**
 * describe：公共加载fragment类
 *
 * @author ：鲁宇峰 on 2018/8/8 13：44
 *         email：luyufengc@enn.cn
 */
public class AndroidInterface {


    private Context context;

    public AndroidInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void ryShare(final String msg) {
        if(!RxDataTool.isNullString(msg)){
            LiveEventBus.get("ryShare").post(msg);
        }
    }

}
