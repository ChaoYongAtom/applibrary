package com.ruiyun.comm.library.mvvm;


import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;
import com.ruiyun.comm.library.api.entitys.UpdateImage;
import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.mvvm.interfaces.CallBack;
import com.ruiyun.comm.library.mvvm.rx.HttpHelper;
import com.ruiyun.comm.library.mvvm.rx.RxSchedulers;

import org.wcy.android.retrofit.exception.ApiException;
import org.wcy.android.retrofit.exception.CodeException;
import org.wcy.android.utils.AESOperator;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.utils.RxDataTool;
import org.wcy.android.utils.RxKeyboardTool;
import org.wcy.android.utils.RxLogTool;
import org.wcy.android.utils.RxNetTool;
import org.wcy.android.utils.RxTool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author：wct on 18/7/26 16:15
 */
public abstract class AbsRepository<T> {

    private CompositeDisposable mCompositeSubscription;
    private Context mContext;
    private CallBack callBack;
    public AbsRepository() {

    }

    private Object apiService;

    public void uplaod(String path,CallBack listener) {
        RxKeyboardTool.hideSoftInput(RxActivityTool.currentActivity());
        if(listener==null)listener=callBack;
        if (RxNetTool.isNetworkAvailable(RxTool.getContext())) {
            RxSubscriber<T> subscriber = new RxSubscriber();
            subscriber.setmSubscriberOnNextListener(listener);
            subscriber.setContext(getmContext());
            subscriber.setMethod("uploadimage");
            subscriber.setShowProgress(false);
            subscriber.setData(UpdateImage.class);
            subscriber.setUpload(true);
            Flowable observable = getOverrideUpload(path);
            addSubscribe(observable, subscriber);
        } else {
            listener.onError(new ApiException(null, CodeException.NETWORD_ERROR, "无网络连接，请检查网络是否正常", "uploadimage"));
        }
    }

    protected void addSubscribe(Flowable<T> flowable, RxSubscriber<T> subscriber) {
        if (flowable != null && subscriber != null) {
            addSubscribe(flowable.compose(RxSchedulers.io_main()).subscribeWith(subscriber));
        }
    }


    public void sendPost(String method, CallBack listener) {
        sendPost(method, null, null, false, false, null, false, listener);
    }

    public  void sendPost(String method, Object parameters, Class cl, CallBack listener) {
        sendPost(method, parameters, cl, false, false, null, false, listener);
    }

    public   void sendPost(String method, Object parameters, Class cl, boolean isShowProgress, CallBack listener) {
        sendPost(method, parameters, cl, false, isShowProgress, null, false, listener);
    }

    public  void sendPost(String method, Object parameters, Class cl, boolean isList, boolean isShowProgress, CallBack listener) {
        sendPost(method, parameters, cl, isList, isShowProgress, null, false, listener);
    }

    /**
     * @param method         接口名
     * @param parameters     参数
     * @param cl             返回对象类型
     * @param isList         是否是列表
     * @param isShowProgress 是否显示弹框
     * @param msg            弹框提示信息
     * @param isCancel       弹框是否可以取消
     * @param listener       回调接口
     */
    public void sendPost(String method, Object parameters, Class<?> cl, boolean isList, boolean isShowProgress, String msg, boolean isCancel, CallBack listener) {
        RxKeyboardTool.hideSoftInput(RxActivityTool.currentActivity());
        if(listener==null)listener=callBack;
        if (RxNetTool.isNetworkAvailable(RxTool.getContext())) {
            if (null == apiService) {
                apiService = HttpHelper.getInstance().create(JConstant.getHttpPostService());
            }
            if (apiService != null) {
                try {
                    Flowable obs = getOverride(method, parameters);
                    if (obs != null) {
                        RxSubscriber<T> subscriber;
                        if (JConstant.getRxsubscriber() != null) {
                            subscriber = JConstant.getRxsubscriber().newInstance();
                        } else {
                            subscriber = new RxSubscriber();
                        }
                        subscriber.setmSubscriberOnNextListener(listener);
                        subscriber.setContext(getmContext());
                        subscriber.setMethod(method);
                        subscriber.setShowProgress(isShowProgress);
                        subscriber.setData(cl);
                        subscriber.setList(isList);
                        subscriber.setMsg(msg);
                        subscriber.setCancel(isCancel);
                        addSubscribe(obs, subscriber);
                    } else {
                        listener.onError(new ApiException(null, CodeException.UNKNOWN_ERROR, "接口信息不存在", method));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(new ApiException(null, CodeException.ERROR, "网络请求处理失败", method));
                }

            }
        } else {
            listener.onError(new ApiException(null, CodeException.NETWORD_ERROR, "无网络连接，请检查网络是否正常", method));
        }


    }

    /**
     * 根据接口名称动态调用接口
     *
     * @param method
     * @param parameters
     * @return
     */
    public Flowable<T> getOverride(String method, Object parameters) {
        try {
            Class cl = apiService.getClass();
            String params = "";
            if (parameters != null) {
                if (parameters instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) parameters;
                    if (jsonObject.size() > 0) {
                        List<String> keys = new ArrayList<>();
                        for (String str : jsonObject.keySet()) {
                            if (RxDataTool.isEmpty(jsonObject.get(str))) {
                                keys.add(str);
                            }
                        }
                        for (String key : keys) {
                            jsonObject.remove(key);
                        }
                        if (jsonObject.size() > 0) {
                            if (RxActivityTool.isAppDebug(RxTool.getContext())) {
                                RxLogTool.d("postParameters = ----------------->" + method, parameters.toString());
                                LogUtils.json(jsonObject.toJSONString());
                            }
                            if (JConstant.isEncrypt()) {
                                params = AESOperator.encrypt(jsonObject.toJSONString());
                            } else {
                                params = jsonObject.toJSONString();
                            }
                        }
                    }

                } else if (parameters instanceof String) {
                    RxLogTool.d("postParameters = ----------------->" + method, parameters.toString());
                    if (JConstant.isEncrypt() && !RxDataTool.isNullString(parameters.toString())) {
                        params = AESOperator.encrypt(parameters.toString());
                    }
                }
            }
            String token = JConstant.getToken();
            Flowable<T> observable = null;
            if (parameters != null || !RxDataTool.isNullString(token)) {
                if (parameters != null) {
                    observable = (Flowable<T>) cl.getMethod(method, new Class[]{String.class, String.class}).invoke(apiService, params, token);
                } else {
                    observable = (Flowable<T>) cl.getMethod(method, new Class[]{String.class}).invoke(apiService, token);
                }
            } else {
                if (!RxDataTool.isNullString(params)) {
                    observable = (Flowable<T>) cl.getMethod(method, new Class[]{String.class}).invoke(apiService, params);
                } else {
                    observable = (Flowable<T>) cl.getMethod(method).invoke(apiService);
                }
            }
            return observable;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取图片上传的接口地址
     *
     * @param path
     * @return
     */
    public Flowable<T> getOverrideUpload(String path) {
        try {
            Class cl = apiService.getClass();
            File file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            String token = JConstant.getToken();
            RequestBody uid = RequestBody.create(MediaType.parse("text/plain"), token);
            Flowable<T> observable = (Flowable<T>) cl.getMethod("uploadimage", new Class[]{RequestBody.class, MultipartBody.Part.class}).invoke(apiService, uid, part);
            return observable;
        } catch (Exception e) {
            return null;
        }
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public Context getmContext() {
        return mContext;
    }

    protected void addSubscribe(Disposable disposable) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(disposable);
    }

    public void unSubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.isDisposed()) {
            mCompositeSubscription.clear();
        }
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
