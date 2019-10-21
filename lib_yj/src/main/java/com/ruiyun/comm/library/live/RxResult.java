package com.ruiyun.comm.library.live;

import org.wcy.android.live.BaseResult;

/**
 * 回调信息统一封装类
 * Created by wcy on 2016/7/16.
 */
public class RxResult extends BaseResult {
    //  判断标示
    private int code;
    //    提示信息
    private String msg;

    /*方法-如果需要缓存必须设置这个参数；不需要不用設置*/
    private String method;

    private String data;

    private int tag;
    private String businessType;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

}
