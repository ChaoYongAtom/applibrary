package org.wcy.android.live;

/**
 * BaseResult
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/10/21
 * @description applibrary
 */
public class BaseResult {
    private String className;
    //显示数据（用户需要关心的数据）
    private Object result;
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    public <T> T getResult() {
        return (T) result;
    }
    public void setResult(Object result) {
        this.result = result;
    }
}
