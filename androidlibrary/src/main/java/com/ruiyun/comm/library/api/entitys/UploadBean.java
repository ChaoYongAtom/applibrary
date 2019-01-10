package com.ruiyun.comm.library.api.entitys;

import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.utils.RxDataTool;

import java.io.Serializable;

/**
 * Created by wcy on 2017/6/8.
 * 检测更新
 */

public class UploadBean implements Serializable {


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

    public int getVersion() {
        if (RxDataTool.isNullString(versionNum)) {
            return 0;
        } else {
            return Integer.parseInt(versionNum.replace(".", ""));
        }
    }

    public String getUpdateContent() {
        if (RxDataTool.isNullString(updateContent)) {
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
    public boolean isUpdate() {
        if (RxActivityTool.getAppVersionCode() < getVersion()) {
            return true;
        }
        return false;

    }
}
