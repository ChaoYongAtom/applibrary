package org.wcy.android.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * MarqueeTextView
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/5/24
 * @description YjSales
 */
public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView{
    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {//必须重写，且返回值是true，表示始终获取焦点
        return true;
    }
}
