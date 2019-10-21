package com.ruiyun.comm.library.live;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mobstat.StatService;
import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.live.interfaces.CallBack;
import com.ruiyun.comm.library.utils.AESOperator;
import com.wcy.app.lib.network.exception.ApiException;
import com.wcy.app.lib.network.exception.CodeException;
import com.wcy.app.lib.network.interfaces.NetWorkResult;

import org.wcy.android.view.ProgressDialogUtil;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.utils.RxDataTool;
import org.wcy.android.utils.RxLogTool;
import org.wcy.android.utils.RxTool;

import java.math.BigDecimal;
import java.util.Set;

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
public class RxSubscriber implements NetWorkResult {
    //    回调接口
    protected CallBack mSubscriberOnNextListener;
    //    加载框可自己定义
    /*是否能取消加载框*/
    private boolean cancel = false;
    /*是否显示加载框*/
    private boolean showProgress = false;
    //    加载框可自己定义
    private ProgressDialogUtil progressDialog;
    /*是否弹框*/
    protected Class<?> data;
    protected String msg;
    protected String method;

    protected Context context;

    public void setmSubscriberOnNextListener(CallBack mSubscriberOnNextListener) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
        if (showProgress) {
            if (progressDialog == null) progressDialog = new ProgressDialogUtil(context);
            progressDialog.setCanselable(isCancel());
        }
        showProgressDialog();
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        if (!showProgress) return;
        if (progressDialog != null) progressDialog.show();
    }

    /**
     * 隐藏
     */
    protected void dismissProgressDialog() {
        if (!showProgress) return;
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param result 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(String result) {
        if (mSubscriberOnNextListener != null) {
            if (JConstant.getRxsubscriber() != null) {
                handleResult(result, null);
                dismissProgressDialog();
            } else {
                try {
                    RxResult baseResult = JSONObject.parseObject(result, RxResult.class);
                    if (getData() != null) baseResult.setClassName(getData().getSimpleName());
                    String dataJson = baseResult.getResult() == null ? "" : baseResult.getResult().toString();
                    if (!RxDataTool.isNullString(dataJson) && JConstant.isEncrypt()) {
                        dataJson = AESOperator.decrypt(dataJson);
                        if (RxDataTool.isNullString(dataJson)) {
                            dataJson = baseResult.getResult() == null ? "" : baseResult.getResult().toString();
                        }
                    }
                    if (!RxDataTool.isNullString(dataJson)) {
                        baseResult.setResult(dataJson);
                    }
                    if (baseResult.getCode() == 200) {
                        if (getData() != null) {
                            baseResult.setData(dataJson);
                            RxLogTool.dJson(dataJson);
                            if (dataJson.startsWith("[")) {
                                baseResult.setResult(JSONObject.parseArray(dataJson, getData()));
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
                        mSubscriberOnNextListener.onNext(baseResult);
                    } else if (baseResult.getCode() == 101 || baseResult.getCode() == 102 || baseResult.getCode() == 103) {
                        if (JConstant.getLoinOutInterface() != null) {
                            JConstant.getLoinOutInterface().loginOut(context, baseResult.getCode(), baseResult.getMsg());
                        }
                    } else {
                        ApiException apiException = getApiException(null, baseResult.getCode(), baseResult.getMsg(), JSONObject.toJSONString(baseResult));
                        apiException.setBusinessType(baseResult.getBusinessType());
                        mSubscriberOnNextListener.onError(apiException);
                    }

                } catch (Exception e) {
                    if (RxActivityTool.isAppDebug(RxTool.getContext())) {
                        e.printStackTrace();
                    } else {
                        StatService.recordException(RxTool.getContext(), new Throwable(result));
                    }
                    mSubscriberOnNextListener.onError(getApiException(e, CodeException.UNKNOWN_ERROR, "网络数据处理错误", result));
                } finally {
                    dismissProgressDialog();
                }
            }
        } else {
            RxLogTool.d("mSubscriberOnNextListener" + method, "没有mSubscriberOnNextListener");
        }

    }

    public ApiException getApiException(Exception e, int JSON_ERROR, String msg, String result) {
        ApiException apiException = new ApiException(e, JSON_ERROR, msg, method);
        apiException.setData(result);
        return apiException;
    }

    public String getDataResult(String json) {
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

    public <T> void setData(Class<T> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        if (!RxDataTool.isNullString(msg)) this.msg = msg;
    }

    public String getMethod() {
        return method;
    }

    public Context getContext() {
        return context;
    }

    public void handleResult(String result, RxResult baseResult) {
    }

    @Override
    public void onError(Throwable e) {
        RxLogTool.d("errorDo" + method, e.getMessage());
        if (mSubscriberOnNextListener == null) return;
        mSubscriberOnNextListener.onError(new ApiException(e, CodeException.UNKNOWN_ERROR, "网络连接错误", method));
        dismissProgressDialog();
    }
}