package com.wcy.app.lib.update;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.Serializable;

/**
 * Created by wcy on 2017/6/8.
 * 检测更新
 */

public class VersionBean implements Serializable {


    /**
     * is_must_update : 0
     * download_url : urlpath
     * versionNum : 1.2
     */
    public String title;//标题
    private String updateContent = "";//更新内容
    public boolean is_must_update;//是否强制更新
    public String download_url;//下载地址
    public String versionNum = "";//服务器版本
    public String createDate = "";//更新时间
    public String apkSize = "";
    public boolean isAlert;//是否弹出提示框	Integer	（1是、0否）

    public int getVersion() {
        if (versionNum == null || "".equals(versionNum)) {
            return 0;
        } else {
            return Integer.parseInt(versionNum.replace(".", ""));
        }
    }

    public String getUpdateContent() {
        if (updateContent == null) {
            return "";
        } else {
            return updateContent.replace("br", "\n");
        }
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    /**
     * 服务器版本是否大于本地
     *
     * @return true 有新版本  false 没有新版本
     */
    public boolean isUpdate(Context context) {
        if (getAppVersionCode(context) < getVersion()) {
            return true;
        }
        return false;

    }

    /**
     * 获取App版本码
     *
     * @param context 上下文
     * @return App版本码
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }
}

