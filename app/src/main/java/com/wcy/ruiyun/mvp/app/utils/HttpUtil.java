package com.wcy.ruiyun.mvp.app.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;
import com.wcy.ruiyun.mvp.app.api.SubjectPostApi;
import com.wcy.ruiyun.mvp.app.api.entitys.BaseResult;
import com.wcy.ruiyun.mvp.app.service.InitializeService;
import com.wcy.ruiyun.mvp.app.view.BaseView;
import com.wcy.ruiyun.mvp.app.widget.ProgressDialogView;

import org.wcy.android.retrofit.Api.BaseApi;
import org.wcy.android.retrofit.exception.ApiException;
import org.wcy.android.retrofit.exception.CodeException;
import org.wcy.android.retrofit.http.HttpManager;
import org.wcy.android.retrofit.listener.HttpOnNextListener;
import org.wcy.android.utils.AESOperator;
import org.wcy.android.utils.PreferenceUtils;
import org.wcy.android.utils.StringUtil;

import java.math.BigDecimal;

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

    public HttpUtil(AppCompatActivity activity, BaseView onListener) {
        this.application = activity;
        this.httpOnListener = onListener;
        manager = new HttpManager(activity, this, getHeaders());
        postEntity = new SubjectPostApi();
        postEntity.setBaseUrl(InitializeService.getHttpUrl());
        progressDialogView = new ProgressDialogView(activity);
        postEntity.setProgressDialog(progressDialogView);
    }

    /**
     * http请求头部信息并加密
     **/
    public String getHeaders() {
        if (!StringUtil.hasText(heards)) {
            heards = PreferenceUtils.getValue(application, "heards", "");
            if (!StringUtil.hasText(heards)) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("systemType", "1");
                    object.put("appVersion", "1.1.0");
                    object.put("mobileCode", "7d055698-20e2-4062-bad6-5932ff99e89c");
                    object.put("registrationID", "13065ffa4e3406f5001");
                    object.put("version", "version3");
                    Log.i("heards====", object.toJSONString());
                    heards = AESOperator.encrypt(object.toJSONString());
                } catch (Exception e) {
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

    @Override
    public void onNext(BaseApi api, String result) {
        if (true) {
            BaseResult baseResult = new BaseResult();
            baseResult.setCode(200);
            baseResult.setData(result);
            baseResult.setMsg(result);
            baseResult.setResult(result);
            httpOnListener.onNext(baseResult);
        } else {
            try {
                BaseResult baseResult = JSONObject.parseObject(result, BaseResult.class);
                if (baseResult == null) {
                    baseResult = new BaseResult();
                    baseResult.setResult(api.getData().newInstance());
                    httpOnListener.onError(new ApiException(null, CodeException.JSON_ERROR, "数据解析错误"), api.getMethod());
                } else {
                    if (baseResult.getCode() == 200) {
                        if (api.getData() != null) {
                            String dataJson = String.valueOf(baseResult.getResult());
                            try {
                                dataJson = AESOperator.decrypt(dataJson);
                            } catch (Exception e) {
                                dataJson = String.valueOf(baseResult.getResult());
                            } finally {
                                if (dataJson == null) {
                                    dataJson = String.valueOf(baseResult.getResult());
                                }
                                Log.i("ProgresSubscriber解密", dataJson);
                                if (api.getData() == BigDecimal.class || api.getData() == String.class || api.getData() == Integer.class) {
                                    dataJson = StringUtil.getData(dataJson);
                                }
                                baseResult.setData(dataJson);
                                LogUtils.json(dataJson);
                            }
                            if (api.isList()) {
                                baseResult.setResult(JSONObject.parseArray(dataJson, api.getData()));
                            } else {
                                if (api.getData() == BigDecimal.class || api.getData() == String.class || api.getData() == Integer.class) {
                                    dataJson = StringUtil.getData(dataJson);
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
                        baseResult.setMethod(api.getMethod());
                        httpOnListener.onNext(baseResult);
                    } else if (baseResult.getCode() == 101 || baseResult.getCode() == 102) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(application);
                        builder.setMessage(baseResult.getMsg());
                        builder.setTitle("提示");
                        builder.setPositiveButton("立即重新登录", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // httpOnListener.onOut();
                            }
                        });
                        builder.setCancelable(false);
                        builder.create().show();
                    } else if (baseResult.getCode() == 103) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(application);
                        builder.setMessage(baseResult.getMsg());
                        builder.setTitle("提示");
                        builder.setPositiveButton("去设置楼盘", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //httpOnListener.switching();
                            }
                        });
                        builder.setCancelable(false);
                        builder.create().show();
                    } else {
                        httpOnListener.onError(new ApiException(null, CodeException.ERROR, baseResult.getMsg()), api.getMethod());
                    }
                }
            } catch (Exception e) {
                httpOnListener.onError(new ApiException(null, CodeException.ERROR, "数据处理异常，请稍后再试"), api.getMethod());
            }
        }
    }

    @Override
    public void onError(ApiException e, String mothead) {
        if (httpOnListener != null) {
            httpOnListener.onError(e, mothead);
        }
    }
}
