package com.ruiyun.comm.library.mvvm;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruiyun.comm.library.api.entitys.UpdateImage;
import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.mvvm.interfaces.CallBack;

import org.wcy.android.retrofit.exception.ApiException;
import org.wcy.android.retrofit.exception.CodeException;
import org.wcy.android.retrofit.exception.HttpTimeException;
import org.wcy.android.retrofit.subscribers.ProgressDialogUtil;
import org.wcy.android.utils.AESOperator;
import org.wcy.android.utils.RxDataTool;
import org.wcy.android.utils.RxLogTool;

import java.math.BigDecimal;
import java.util.Set;

import rx.Subscriber;


/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 *
 * @version v1.0
 * @date 2017/3/14
 * @auth wcy
 * @company 重庆锐云科技有限公司
 */
public class RxSubscriber extends Subscriber<String> {
    //    回调接口
    private CallBack mSubscriberOnNextListener;
    //    加载框可自己定义
    /*是否能取消加载框*/
    private boolean            cancel = false;
    /*是否显示加载框*/
    private boolean            showProgress = false;
    //    加载框可自己定义
    private ProgressDialogUtil progressDialog;
    /*是否弹框*/
    private Class<?>           data;
    private boolean            isList = false;
    private String             msg;
    private boolean            isUpload;
    private String             method;

    private Context context;

    /**
     * 构造
     */
    public RxSubscriber(CallBack listenerSoftReference, Context context, String method, boolean showProgress) {
        this.method = method;
        this.mSubscriberOnNextListener = listenerSoftReference;
        this.context = context;
        if (showProgress) {
            this.showProgress = showProgress;
            progressDialog = new ProgressDialogUtil(context);
            progressDialog.setCanselable(isCancel());
        }
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        if (!showProgress) return;
        if (progressDialog != null)
            progressDialog.show();
    }


    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
        if (!showProgress) return;
        if (progressDialog != null) {
            onCancelProgress();
            progressDialog.hide();
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        errorDo(e);
    }


    /**
     * 错误统一处理
     *
     * @param e
     */
    private void errorDo(Throwable e) {
        if (mSubscriberOnNextListener == null) return;
        if (e instanceof HttpTimeException) {
            HttpTimeException exception = (HttpTimeException) e;
            mSubscriberOnNextListener.onError(new ApiException(exception, CodeException.RUNTIME_ERROR, exception.getMessage(), method));
        } else {
            RxLogTool.d("ProgressSubscriber", "网络连接错误：");
            mSubscriberOnNextListener.onError(new ApiException(e, CodeException.UNKNOWN_ERROR, "网络连接错误", method));
        }
        dismissProgressDialog();
    }


    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(String t) {

        if (mSubscriberOnNextListener != null) {
            try {
                if (!RxDataTool.isNullString(t)) {
                    RxResult baseResult = JSONObject.parseObject(t, RxResult.class);
                    if (baseResult == null) {
                        baseResult = new RxResult();
                        baseResult.setResult(getData().newInstance());
                        mSubscriberOnNextListener.onError(getApiException(null, CodeException.JSON_ERROR, "数据解析错误"));
                    } else {
                        if (baseResult.getCode() == 200) {
                            if (getData() != null) {
                                String dataJson = baseResult.getResult() == null ? "" : baseResult.getResult().toString();
                                if (JConstant.isEncrypt()) {
                                    dataJson = AESOperator.decrypt(dataJson);
                                    if (RxDataTool.isNullString(dataJson)) {
                                        dataJson = baseResult.getResult() == null ? "" : baseResult.getResult().toString();
                                    }
                                }
                                baseResult.setData(dataJson);
                                RxLogTool.dJson(dataJson);
                                if (isList()) {
                                    baseResult.setResult(JSONObject.parseArray(dataJson, getData()));
                                } else {
                                    if (isUpload) {
                                        baseResult.setResult(JSONObject.parseObject(dataJson, UpdateImage.class));
                                    } else {
                                        if (getData() == BigDecimal.class || getData() == String.class || getData() == Integer.class) {
                                            dataJson = getDataResult(dataJson);
                                        }
                                        if (getData() == BigDecimal.class) {
                                            baseResult.setResult(new BigDecimal(dataJson));
                                        } else if (getData() == String.class) {
                                            baseResult.setResult(dataJson);
                                        } else if (getData() == Integer.class) {
                                            baseResult.setResult(Integer.parseInt(dataJson));
                                        } else if (getData() != null) {
                                            baseResult.setResult(JSONObject.parseObject(dataJson, getData()));
                                        }
                                    }
                                }
                            }
                            mSubscriberOnNextListener.onNext(baseResult);
                        } else if (baseResult.getCode() == 101 || baseResult.getCode() == 102 || baseResult.getCode() == 103) {
                            if (JConstant.getLoinOutInterface() != null) {
                                JConstant.getLoinOutInterface().loginOut(context, baseResult.getCode(), baseResult.getMsg());
                            }
                        } else {
                            mSubscriberOnNextListener.onError(getApiException(null, CodeException.ERROR, baseResult.getMsg()));
                        }
                    }
                } else {
                    mSubscriberOnNextListener.onError(getApiException(null, CodeException.ERROR, "服务器返回数据错误"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                RxLogTool.d("RxSubscriberException" + method, e.getMessage());
                mSubscriberOnNextListener.onError(getApiException(e, CodeException.JSON_ERROR, "网络数据处理错误"));
            } finally {
                dismissProgressDialog();
            }
        }
    }

    private ApiException getApiException(Exception e, int JSON_ERROR, String msg) {
        return new ApiException(e, JSON_ERROR, msg, method);
    }

    private String getDataResult(String json) {
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

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            mSubscriberOnNextListener = null;
            this.unsubscribe();
        }
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public ProgressDialogUtil getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialogUtil progressDialog) {
        this.progressDialog = progressDialog;
    }

    public Class<?> getData() {
        return data;
    }

    public void setData(Class<?> data) {
        this.data = data;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        if (!RxDataTool.isNullString(msg)) this.msg = msg;
    }
}