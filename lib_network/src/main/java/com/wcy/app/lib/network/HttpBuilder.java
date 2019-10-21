package com.wcy.app.lib.network;

import java.util.HashMap;
import java.util.Map;

/**
 * HttpBuidle
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/10/18
 * @description YjSales
 */
public class HttpBuilder {

    public static HttpBuilder getBuilder(String url) {
        return new HttpBuilder(url);
    }

    String domainUrl;//是否使用默认地址，如果不使用默认地址则使用第二默认地址
    int timeout = 6;//动态设置连接超时时间 必须大于0 ，如果小于0则使用默认超时时间
    String url;// 访问地址
    String tag;//设置标签
    Map<String, String> headers;//添加header
    Map<String, Object> parameters;//数据类型的参数
    Map<String, String> files;//文件类型的参数
    boolean isShowProgress;//
    String msg;//
    boolean isCancel;//

    private HttpBuilder(String url) {
        this.url = url;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public HttpBuilder setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public HttpBuilder setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public String getTag() {
        return tag;
    }

    public HttpBuilder setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public Map<String, String> getHeaders() {
        if(headers==null){
            headers=new HashMap<>();
        }
        return headers;
    }

    public HttpBuilder setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public HttpBuilder setHeaders(String headers) {
        getHeaders().put("headers", headers);
        return this;
    }

    public Map<String, Object> getParameters() {
        if(parameters==null){
            parameters=new HashMap<>();
        }
        return parameters;
    }

    public HttpBuilder setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
        return this;
    }

    public Map<String, String> getFiles() {
        if(files==null){
            files=new HashMap<>();
        }
        return files;
    }

    public HttpBuilder setFiles(Map<String, String> files) {
        this.files = files;
        return this;
    }

    public boolean isShowProgress() {
        return isShowProgress;
    }

    public HttpBuilder setShowProgress(boolean showProgress) {
        isShowProgress = showProgress;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public HttpBuilder setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public HttpBuilder setCancel(boolean cancel) {
        isCancel = cancel;
        return this;
    }
}
