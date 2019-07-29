package com.ruiyun.comm.library.mvvm;


import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;
import com.ruiyun.comm.library.api.HttpUploadService;
import com.ruiyun.comm.library.api.entitys.UploadBean;
import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.emum.UploadType;
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
    private HttpUploadService apiService;
    private String fragmentName = "";

    public AbsRepository() {
    }

    /**
     * 多文件上传，返回list对象
     *
     * @param uploadType
     * @param paths
     * @param urls
     * @param listener
     */
    public void uplaod(UploadType uploadType, List<String> paths, List<UploadBean> urls, CallBack listener) {
        String s = paths.get(0);
        uplaod(uploadType, s, new CallBack() {
            @Override
            public void onNext(RxResult result) {
                urls.add(result.getResult());
                paths.remove(s);
                if (paths.size() == 0) {
                    result.setResult(urls);
                    listener.onNext(result);
                } else {
                    uplaod(uploadType, paths, urls, listener);
                }
            }

            @Override
            public void onError(ApiException e) {
                listener.onError(e);
            }
        });

    }

    /**
     * 单文件上传返回UploadBean对象
     *
     * @param uploadType
     * @param paths
     * @param listener
     */
    public void uplaod(UploadType uploadType, List<String> paths, CallBack listener) {
        String s = paths.get(0);
        uplaod(uploadType, s, new CallBack() {
            @Override
            public void onNext(RxResult result) {
                listener.onNext(result);
            }

            @Override
            public void onError(ApiException e) {
                listener.onError(e);
            }
        });

    }

    /**
     * 多文件上传 最终返回string，通过，拼接
     *
     * @param uploadType
     * @param paths
     * @param sb
     * @param listener
     */
    public void uplaod(UploadType uploadType, List<String> paths, StringBuffer sb, CallBack listener) {
        String s = paths.get(0);
        uplaod(uploadType, s, new CallBack() {
            @Override
            public void onNext(RxResult result) {
                UploadBean uploadBean = result.getResult();
                sb.append(uploadBean.fileUrl).append(",");
                paths.remove(s);
                if (paths.size() == 0) {
                    sb.delete(sb.length() - 1, sb.length());
                    result.setResult(sb.toString());
                    listener.onNext(result);
                } else {
                    uplaod(uploadType, paths, sb, listener);
                }
            }

            @Override
            public void onError(ApiException e) {
                listener.onError(e);
            }
        });

    }

    public void uplaod(UploadType uploadType, String path, CallBack listener) {
        RxKeyboardTool.hideSoftInput(RxActivityTool.currentActivity());
        if (uploadType == null) uploadType = UploadType.IMAGE;
        if (listener == null) listener = callBack;
        if (RxNetTool.isNetworkAvailable(RxTool.getContext())) {
            try {
                RxSubscriber<T> subscriber;
                if (JConstant.getRxsubscriber() != null) {
                    subscriber = JConstant.getRxsubscriber().newInstance();
                } else {
                    subscriber = new RxSubscriber();
                }
                subscriber.setmSubscriberOnNextListener(listener);
                subscriber.setContext(getmContext());
                subscriber.setMethod(uploadType.getEurl());
                subscriber.setShowProgress(false);
                subscriber.setData(UploadBean.class);
                subscriber.setUpload(true);
                Flowable observable = getOverrideUpload(path, subscriber.getMethod());
                if (observable != null) {
                    addSubscribe(observable, subscriber);
                } else {
                    listener.onError(new ApiException(null, CodeException.NETWORD_ERROR, "图片上传错误", uploadType.getEurl()));
                }
            }catch (Exception e){
                listener.onError(new ApiException(null, CodeException.NETWORD_ERROR, "图片上传错误", uploadType.getEurl()));
            }
        } else {
            listener.onError(new ApiException(null, CodeException.NETWORD_ERROR, "无网络连接，请检查网络是否正常", uploadType.getEurl()));
        }
    }

    protected void addSubscribe(Flowable<T> flowable, RxSubscriber<T> subscriber) {
        if (flowable != null && subscriber != null) {
            addSubscribe(flowable.compose(RxSchedulers.io_main()).subscribeWith(subscriber), subscriber.getMethod());
        }
    }

    public void sendPost(String method, CallBack listener) {
        sendPost(method, null, null, false, false, null, false, listener);
    }

    public void sendPost(String method, Object parameters, Class cl, CallBack listener) {
        sendPost(method, parameters, cl, false, false, null, false, listener);
    }

    public void sendPost(String method, Object parameters, Class cl, boolean isShowProgress, CallBack listener) {
        sendPost(method, parameters, cl, false, isShowProgress, null, false, listener);
    }

    public void sendPost(String method, Object parameters, Class cl, boolean isList, boolean isShowProgress, CallBack listener) {
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
        if (listener == null) listener = callBack;
        if (RxNetTool.isNetworkAvailable(RxTool.getContext())) {
            if (null == apiService) {
                apiService = HttpHelper.getInstance().create(HttpUploadService.class);
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
                        subscriber.setStartTime(System.currentTimeMillis());
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
    private Flowable<RxResult> getOverride(String method, Object parameters) {
        String params = "";
        if (parameters != null) {
            if (parameters instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) parameters;
                if (jsonObject.size() > 0) {
                    List<String> keys = new ArrayList<>();
                    //处理空参数
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
        return apiService.sendPost(method, params, JConstant.getToken());
    }

    /**
     * 获取图片上传的接口地址
     *
     * @param path
     * @return
     */
    private Flowable<RxResult> getOverrideUpload(String path, String method) {
        File file = new File(path);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        RequestBody uid = RequestBody.create(MediaType.parse("multipart/form-data"), JConstant.getToken());
        if (null == apiService) {
            apiService = HttpHelper.getInstance().create(HttpUploadService.class);
        }
        return apiService.upload(method, uid, part);
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public Context getmContext() {
        return mContext;
    }

    protected void addSubscribe(Disposable disposable, String method) {
        String key = fragmentName.concat(method);
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        if (mCompositeSubscription != null && HttpHelper.disposableMap.containsKey(key)) {
            RxLogTool.d("onNext", method + "请求存在");
            Disposable disposable1 = HttpHelper.disposableMap.get(key);
            if (disposable1 instanceof RxSubscriber && ((RxSubscriber) disposable1).isLoading()) {
                RxLogTool.d("onNext", method + "取消请求");
                mCompositeSubscription.remove(disposable1);
            }
        }
        RxLogTool.d("onNext", method + "发送请求");
        HttpHelper.disposableMap.put(key, disposable);
        mCompositeSubscription.add(disposable);
    }

    public void unSubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.isDisposed()) {
            mCompositeSubscription.clear();
            mCompositeSubscription = null;
        }
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
