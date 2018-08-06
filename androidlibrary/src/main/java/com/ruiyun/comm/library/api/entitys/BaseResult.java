package com.ruiyun.comm.library.api.entitys;

/**
 * 回调信息统一封装类
 * Created by wcy on 2016/7/16.
 */
public class BaseResult<T>{
    //  判断标示
    private int code;
    //    提示信息
    private String msg;
    //显示数据（用户需要关心的数据）
    private T result;
    /*方法-如果需要缓存必须设置这个参数；不需要不用設置*/
    private String method;

    private String data;

    private int tag;

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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
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
}
