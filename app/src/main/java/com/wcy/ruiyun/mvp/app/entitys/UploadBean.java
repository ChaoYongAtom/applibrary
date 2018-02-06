package com.wcy.ruiyun.mvp.app.entitys;

import java.io.Serializable;

/**
 * Created by zhaomaohui on 2017/6/8.
 * 检测更新
 */

public class UploadBean implements Serializable {


    /**
     * is_must_update : 0
     * download_url : urlpath
     * versionNum : 1.2
     */

    private int is_must_update;
    private String download_url;
    private String versionNum;

    public int getIs_must_update() {
        return is_must_update;
    }

    public void setIs_must_update(int is_must_update) {
        this.is_must_update = is_must_update;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }
}
