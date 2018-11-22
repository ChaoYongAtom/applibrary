package com.ruiyun.comm.library.mvvm;

import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;
import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.mvvm.interfaces.CallBack;
import com.ruiyun.comm.library.mvvm.rx.HttpHelper;
import com.ruiyun.comm.library.mvvm.rx.RxSchedulers;

import org.wcy.android.retrofit.exception.ApiException;
import org.wcy.android.retrofit.exception.CodeException;
import org.wcy.android.utils.AESOperator;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.utils.RxKeyboardTool;
import org.wcy.android.utils.RxNetTool;
import org.wcy.android.utils.RxTool;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public abstract class BaseRepository extends AbsRepository {
    public static HashMap<String, Integer> mapMethods = new HashMap<>();
    private Object apiService;

    public void uplaod(CallBack listener, String path) {
        RxSubscriber subscriber = new RxSubscriber(listener, getmContext(), "uploadimage", true);
        subscriber.setUpload(true);
        Observable observable = getOverrideUpload(path);
        /*rx处理*/
        addSubscribe(observable
                .compose(RxSchedulers.io_main())
                .subscribe(subscriber));
    }

    public static void setMapMethods(HashMap<String, Integer> mapMethods) {
        BaseRepository.mapMethods = mapMethods;
    }

    public void sendPost(String method, CallBack listener) {
        sendPost(method, null, null, false, false, null, false, listener);
    }

    public void sendPost(String method, JSONObject parameters, Class<?> cl, CallBack listener) {
        sendPost(method, parameters, cl, false, false, null, false, listener);
    }

    public void sendPost(String method, JSONObject parameters, Class<?> cl, boolean isShowProgress, CallBack listener) {
        sendPost(method, parameters, cl, false, isShowProgress, null, false, listener);
    }

    public void sendPost(String method, JSONObject parameters, Class<?> cl, boolean isList, boolean isShowProgress, CallBack listener) {
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
    public void sendPost(String method, JSONObject parameters, Class<?> cl, boolean isList, boolean isShowProgress, String msg, boolean isCancel, CallBack listener) {
        RxKeyboardTool.hideSoftInput(RxActivityTool.currentActivity());
        if (RxNetTool.isNetworkAvailable(RxTool.getContext())) {
            if (null == apiService) {
                apiService = HttpHelper.getInstance().create(JConstant.getHttpPostService());
            }
            if (apiService != null) {
                Observable obs = getOverride(method, parameters);
                if (obs != null) {
                    /*rx处理*/
                    RxSubscriber subscriber = new RxSubscriber(listener, getmContext(), method, isShowProgress);
                    subscriber.setData(cl);
                    subscriber.setList(isList);
                    subscriber.setMsg(msg);
                    subscriber.setCancel(isCancel);
                    addSubscribe(obs
                            .compose(RxSchedulers.io_main())
                            .subscribe(subscriber));
                } else {
                    listener.onError(new ApiException(null, CodeException.UNKNOWN_ERROR, "接口信息不存在", method));
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
    public Observable getOverride(String method, JSONObject parameters) {
        try {
            Class cl = apiService.getClass();
            int count = mapMethods.get(method);
            Observable observable = null;
            String token = JConstant.getToken();
            String params = "";
            if (parameters != null && !parameters.toJSONString().equals("{}")) {
                if (JConstant.isEncrypt()) {
                    params = AESOperator.encrypt(parameters.toJSONString());
                } else {
                    params = parameters.toJSONString();
                }
            }
            if (count == 0) {
                observable = (Observable) cl.getMethod(method).invoke(apiService);
            } else if (count == 1) {
                if (parameters == null) {
                    observable = (Observable) cl.getMethod(method, new Class[]{String.class}).invoke(apiService, token);
                } else {
                    observable = (Observable) cl.getMethod(method, new Class[]{String.class}).invoke(apiService, params);
                }
            } else if (count == 2) {
                observable = (Observable) cl.getMethod(method, new Class[]{String.class, String.class}).invoke(apiService, params, token);
            }
            if (parameters != null && RxActivityTool.isAppDebug(RxTool.getContext())) {
                LogUtils.json(parameters.toJSONString());
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
    public Observable getOverrideUpload(String path) {
        try {
            String method = "uploadimage";
            Class cl = apiService.getClass();
            File file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            String token = JConstant.getToken();
            RequestBody uid = RequestBody.create(MediaType.parse("text/plain"), token);
            Observable observable = (Observable) cl.getMethod(method, new Class[]{RequestBody.class, MultipartBody.Part.class}).invoke(apiService, uid, part);
            return observable;
        } catch (Exception e) {
            return null;
        }
    }
}
