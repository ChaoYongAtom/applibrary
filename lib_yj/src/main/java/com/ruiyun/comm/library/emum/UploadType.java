package com.ruiyun.comm.library.emum;

import com.ruiyun.comm.library.common.JConstant;

/**
 * UploadType
 * 上传类型
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/2/16
 * @description YjSales
 */
public enum UploadType {
    IMAGE(1, JConstant.uploadName, "图片"), VIDEO(2, "platform/uploadvideo", "视频");
    private int ecode;
    private String ename;
    private String eurl;

    private UploadType(int ecode, String eurl, String ename) {
        this.ecode = ecode;
        this.eurl = eurl;
        this.ename = ename;
    }

    public int getEcode() {
        return ecode;
    }

    public void setEcode(int ecode) {
        this.ecode = ecode;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getEurl() {
        return eurl;
    }

    public void setEurl(String eurl) {
        this.eurl = eurl;
    }
}
