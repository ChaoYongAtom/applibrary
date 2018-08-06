package com.ruiyun.comm.library.api;


import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;
import com.ruiyun.comm.library.common.JConstant;

import org.wcy.android.retrofit.Api.BaseApi;
import org.wcy.android.utils.AESOperator;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.utils.RxTool;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Observable;


/**
 * Created by wcy on 2017/3/14.
 */
public class SubjectPostApi extends BaseApi {
    private JSONObject parameters;

    public JSONObject getParameters() {
        return parameters;
    }

    public static HashMap<String, Integer> mapMethods = new HashMap<>();

    /**
     * 参数设置
     *
     * @param method     请求指令
     * @param parameters 请求参数
     */
    public void setParameters(String method, JSONObject parameters) {
        super.setMethod(method);
        this.parameters = parameters;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        Object httpService = retrofit.create(JConstant.getHttpPostService());
        try {
            String params = "";
            String token = JConstant.getToken();
            if (parameters != null) {
                if(RxActivityTool.isAppDebug(RxTool.getContext())){
                    LogUtils.json(parameters.toJSONString());
                }
                if (JConstant.isEncrypt()) {
                    params = AESOperator.encrypt(parameters.toJSONString());
                } else {
                    params = parameters.toJSONString();
                }
            }
            int count = mapMethods.get(getMethod());
            if (count == 0) {
                return (Observable) httpService.getClass().getMethod(getMethod()).invoke(httpService);
            } else if (count == 1) {
                if (parameters == null) {
                    params = token;
                }
                return (Observable) httpService.getClass().getMethod(getMethod(), new Class[]{String.class}).invoke(httpService, params);
            } else {
                return (Observable) httpService.getClass().getMethod(getMethod(), new Class[]{String.class, String.class}).invoke(httpService, params, token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
