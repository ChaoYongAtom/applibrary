package com.lxj.xpopup.util.navbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.lxj.xpopup.util.XPopupUtils;

/**
 * NavigationBar
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/12/2
 * @description XPopup-androidx
 */
public class NavigationBar {
    public static boolean hasNavigationBar(Context context) {
        return getScreenHeight(context) != getScreenHeight3(context);
    }
    public static int barHeight(Context context){
        int hbh=getNavigationBarHeight(context);
        int xnbh= XPopupUtils.getNavBarHeight();
        if(hbh==xnbh){
            return xnbh;
        }else{
            if(hbh>xnbh){
                return xnbh;
            }else{
                return hbh;
            }
        }
    }
    /**
     * 不包含虚拟导航栏高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    /**
     *  包含虚拟导航栏高度
     * @param context
     * @return
     */
    public static int getScreenHeight3(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            defaultDisplay.getRealSize(outPoint);
        }
        return outPoint.y;
    }
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = -1;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        return navigationBarHeight;
    }
    public static int getNavBarHeight() {
        Resources res = Resources.getSystem();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return res.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }
}
