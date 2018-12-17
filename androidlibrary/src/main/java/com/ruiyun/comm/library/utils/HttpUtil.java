package com.ruiyun.comm.library.utils;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruiyun.comm.library.api.SubjectPostApi;
import com.ruiyun.comm.library.api.UploadApi;
import com.ruiyun.comm.library.api.entitys.BaseResult;
import com.ruiyun.comm.library.api.entitys.UpdateImage;
import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.mvp.BaseView;
import com.ruiyun.comm.library.widget.ProgressDialogView;
import com.tencent.bugly.crashreport.CrashReport;
import com.trello.rxlifecycle.LifecycleProvider;

import org.wcy.android.retrofit.Api.BaseApi;
import org.wcy.android.retrofit.exception.ApiException;
import org.wcy.android.retrofit.exception.CodeException;
import org.wcy.android.retrofit.http.HttpManager;
import org.wcy.android.retrofit.listener.HttpOnNextListener;
import org.wcy.android.utils.AESOperator;
import org.wcy.android.utils.ExampleUtil;
import org.wcy.android.utils.PreferenceUtils;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.utils.RxDataTool;
import org.wcy.android.utils.RxLogTool;
import org.wcy.android.utils.RxTool;

import java.math.BigDecimal;
import java.util.Set;


/**
 * 网络请求
 * Created by wcy on 2017/9/13.
 */

public class HttpUtil implements HttpOnNextListener {
    //    公用一个HttpManager
    private HttpManager manager;
    //    post请求接口信息
    protected SubjectPostApi postEntity;
    private Context application;
    ProgressDialogView progressDialogView;
    private BaseView httpOnListener;
    private static String heards = null;
    private UploadApi uplaodApi;
    private boolean isActivity;
    public HttpUtil(Context context, LifecycleProvider lifecycleProvider, BaseView onListener,boolean isActivity) {
        this.application = context;
        this.httpOnListener = onListener;
        this.isActivity=isActivity;
        manager = new HttpManager(context, lifecycleProvider, this, getHeaders());
        postEntity = new SubjectPostApi();
        postEntity.setActivity(isActivity);
        postEntity.setBaseUrl(JConstant.getHttpUrl());
        postEntity.setConnectionTime(JConstant.getConnectionTime());
        progressDialogView = new ProgressDialogView(context);
        postEntity.setProgressDialog(progressDialogView);
    }

    public void setConnectionTime(int connectionTime) {
        postEntity.setConnectionTime(connectionTime);
    }

    /**
     * http请求头部信息并加密
     **/
    public String getHeaders() {
        if (RxDataTool.isNullString(heards)) {
            heards = PreferenceUtils.getValue(application, JConstant.heards, "");
            if (RxDataTool.isNullString(heards)) {
                try {
                    JSONObject object = new JSONObject();
                    String http = JConstant.getHttpUrl();
                    object.put("systemType", "1");
                    object.put("appVersion", RxActivityTool.getAppVersionName());
                    object.put("mobileCode", ExampleUtil.getImei(application));
                    if (!RxDataTool.isNullString(http))
                        object.put("version", http.substring(http.indexOf("version"), http.lastIndexOf("/")));
                    object.put("registrationID", JConstant.getRegistrationID());
                    RxLogTool.d("heards====", heards);
                    if (JConstant.isEncrypt()) {
                        heards = AESOperator.encrypt(object.toJSONString());
                    } else {
                        heards = object.toJSONString();
                    }
                    PreferenceUtils.setValue(application, JConstant.heards, heards);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return heards;
    }

    /**
     * @param method         请求指令
     * @param parameters     参数
     * @param cl             返回参数类型
     * @param isList         是否返回集合
     * @param isShowProgress 是否显示加载框
     * @param toast          加载框提示信息
     */
    public void send(String method, JSONObject parameters, Class<?> cl, boolean isList, boolean isShowProgress, String toast) {
        postEntity.setParameters(method, parameters);
        postEntity.setData(cl);
        postEntity.setList(isList);
        postEntity.setShowProgress(isShowProgress);
        postEntity.setMsg(toast);
        if (progressDialogView != null) {
            progressDialogView.setMsg(toast);
        }
        manager.doHttpDeal(postEntity);
    }

    /**
     * 图片上传
     *
     * @param path 图片路径
     */
    public void upload(String path) {
        if (uplaodApi == null) {
            uplaodApi = new UploadApi();
            uplaodApi.setActivity(isActivity);
        }
        uplaodApi.setBaseUrl(JConstant.getHttpUrl());
        uplaodApi.setFile(path);
        manager.doHttpDeal(uplaodApi);
    }

    public void upload(byte[] path) {
        if (uplaodApi == null){
            uplaodApi = new UploadApi();
            uplaodApi.setActivity(isActivity);
        }
        uplaodApi.setBaseUrl(JConstant.getHttpUrl());
        uplaodApi.setByte(path);
        manager.doHttpDeal(uplaodApi);
    }

    @Override
    public void onNext(BaseApi api, String result) {
        try {
            RxLogTool.d("result------------->"+api.getMethod(), result);
            BaseResult baseResult = JSONObject.parseObject(result, BaseResult.class);
            if (baseResult == null) {
                baseResult = new BaseResult();
                baseResult.setResult(api.getData().newInstance());
                httpOnListener.onError(getApiException(null, CodeException.JSON_ERROR, "数据解析错误",result), api.getMethod());
            } else {
                if (baseResult.getCode() == 200) {
                    if (api.getData() != null) {
                        String dataJson = String.valueOf(baseResult.getResult());
                        if (JConstant.isEncrypt()) {
                            try {
                                dataJson = AESOperator.decrypt(dataJson);
                            } catch (Exception e) {
                                dataJson = String.valueOf(baseResult.getResult());
                            } finally {
                                if (RxDataTool.isNullString(dataJson)) {
                                    dataJson = String.valueOf(baseResult.getResult());
                                }
                                baseResult.setData(dataJson);
                            }
                        } else {
                            baseResult.setData(dataJson);
                        }
                        RxLogTool.d("AESOperatorresult", dataJson);
                        RxLogTool.dJson(dataJson);
                        if (api.isList()) {
                            baseResult.setResult(JSONObject.parseArray(dataJson, api.getData()));
                        } else {
                            if (uplaodApi != null && api.getMethod().equals("")) {
                                baseResult.setResult(JSONObject.parseObject(dataJson, UpdateImage.class));
                            } else {
                                if (api.getData() == BigDecimal.class || api.getData() == String.class || api.getData() == Integer.class) {
                                    dataJson = getData(dataJson);
                                }
                                if (api.getData() == BigDecimal.class) {
                                    baseResult.setResult(new BigDecimal(dataJson));
                                } else if (api.getData() == String.class) {
                                    baseResult.setResult(dataJson);
                                } else if (api.getData() == Integer.class) {
                                    baseResult.setResult(Integer.parseInt(dataJson));
                                } else {
                                    baseResult.setResult(JSONObject.parseObject(dataJson, api.getData()));
                                }
                            }
                        }

                    }
                    baseResult.setMethod(api.getMethod());
                    httpOnListener.onNext(baseResult);
                } else if (baseResult.getCode() == 101 || baseResult.getCode() == 102 || baseResult.getCode() == 103) {
                    if (JConstant.getLoinOutInterface() != null) {
                        JConstant.getLoinOutInterface().loginOut(application, baseResult.getCode(), baseResult.getMsg());
                    }
                } else {
                    ApiException apiException=new ApiException(null, CodeException.ERROR, baseResult.getMsg());
                    apiException.setBusinessType(baseResult.getBusinessType());
                    apiException.setData(result);
                    httpOnListener.onError(apiException, api.getMethod());
                }
            }
        } catch (Exception e) {
            RxLogTool.e("HttpUtilonNext", api.getMethod());
            if (!RxTool.isApkInDebug()) {
                try {
                    CrashReport.postCatchedException(e);
                } catch (Exception e1) {
                }
            }

            httpOnListener.onError(getApiException(null, CodeException.ERROR, "数据处理异常，请稍后再试",result), api.getMethod());
        }
    }
    public ApiException getApiException(Exception e, int JSON_ERROR, String msg, String result) {
        ApiException apiException = new ApiException(e, JSON_ERROR, msg);
        apiException.setData(result);
        return apiException;
    }
    private String getData(String json) {
        String dataValue = "";
        try {
            JSONObject dataObj = JSON.parseObject(json);
            if (dataObj.size() == 1) {
                Set<String> sIterator = dataObj.keySet();
                for (String str : sIterator) {
                    dataValue = dataObj.get(str).toString();
                }
            } else {
                dataValue = json;
            }
        } catch (Exception e) {
            dataValue = json;
        }
        return dataValue;
    }

    @Override
    public void onError(ApiException e, String mothead) {
        if (httpOnListener != null) {
            httpOnListener.onError(e, mothead);
        }
    }
}
