package com.ruiyun.comm.library.live;


import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.emum.UploadType;
import com.ruiyun.comm.library.entitys.UploadBean;
import com.ruiyun.comm.library.live.interfaces.CallBack;
import com.ruiyun.comm.library.utils.AESOperator;
import com.wcy.app.lib.network.HttpBuilder;
import com.wcy.app.lib.network.HttpUtils;
import com.wcy.app.lib.network.exception.ApiException;
import com.wcy.app.lib.network.exception.CodeException;
import com.wcy.app.lib.network.interfaces.NetWorkResult;

import org.wcy.android.live.AbsRepository;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.utils.RxDataTool;
import org.wcy.android.utils.RxKeyboardTool;
import org.wcy.android.utils.RxLogTool;
import org.wcy.android.utils.RxNetTool;
import org.wcy.android.utils.RxTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * @author：wct on 18/7/26 16:15
 */
public class BaseRepository extends AbsRepository {
    private CallBack callBack;

    /**
     * 多文件上传，返回list对象
     *
     * @param uploadType
     * @param paths
     * @param urls
     * @param listener
     */
    public void uplaod(UploadType uploadType, List<String> paths, Map<String, Object> map, List<UploadBean> urls, CallBack listener) {
        String s = paths.get(0);
        uplaod(uploadType, s, map, new CallBack() {
            @Override
            public void onNext(RxResult result) {
                urls.add(result.getResult());
                paths.remove(s);
                if (paths.size() == 0) {
                    result.setResult(urls);
                    listener.onNext(result);
                } else {
                    uplaod(uploadType, paths, map, urls, listener);
                }
            }

            @Override
            public void onError(ApiException e) {
                listener.onError(e);
            }
        });
    }

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

    public void uplaod(UploadType uploadType, List<String> paths, Map<String, Object> map, CallBack listener) {
        String s = paths.get(0);
        uplaod(uploadType, s, map, new CallBack() {
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
    public void uplaod(UploadType uploadType, List<String> paths, Map<String, Object> map, StringBuffer sb, CallBack listener) {
        String s = paths.get(0);
        uplaod(uploadType, s, map, new CallBack() {
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
                    uplaod(uploadType, paths, map, sb, listener);
                }
            }

            @Override
            public void onError(ApiException e) {
                listener.onError(e);
            }
        });
    }

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
        upload(uploadType.getEurl(), path, listener);
    }

    public void uplaod(UploadType uploadType, String path, Map<String, Object> map, CallBack listener) {
        upload(uploadType.getEurl(), path, map, listener);
    }

    public void upload(String method, String path, CallBack listener) {
        upload(method, path, null, listener);
    }

    public void upload(String method, String path, Map<String, Object> map, CallBack listener) {
        HttpBuilder builder = HttpBuilder.getBuilder(method);
        builder.getFiles().put("file", path);
        if (map != null) builder.setParameters(map);
        send(builder, UploadBean.class, listener);
    }

    public void sendPost(String method, CallBack listener) {
        sendPost(method, false, listener);
    }

    public void sendPost(String method, boolean isShowProgress, CallBack listener) {
        sendPost(method, null, null, isShowProgress, listener);
    }

    public void sendPost(String method, JSONObject parameters, CallBack listener) {
        sendPost(method, parameters, null, false, listener);
    }

    public void sendPost(String method, JSONObject parameters, boolean isShowProgress, CallBack listener) {
        sendPost(method, parameters, null, isShowProgress, listener);
    }

    public void sendPost(String method, JSONObject parameters, Class cl, CallBack listener) {
        sendPost(method, parameters, cl, false, listener);
    }

    public void sendPost(String method, JSONObject parameters, Class cl, boolean isShowProgress, CallBack listener) {
        send(method, parameters, cl, isShowProgress, "", true, listener);
    }

    /**
     * @param method         调用方法
     * @param parameters     参数
     * @param cl             转换类型
     * @param isShowProgress 是否显示加载中提示
     * @param msg            加载中提示内容
     * @param isCancel
     * @param listener
     */
    public void send(String method, JSONObject parameters, Class cl, boolean isShowProgress, String msg, boolean isCancel, CallBack listener) {
        HttpBuilder builder = HttpBuilder.getBuilder(method);
        builder.setShowProgress(isShowProgress).setCancel(isCancel).setMsg(msg);
        send(builder, parameters, cl, listener);
    }

    public void send(HttpBuilder builder, JSONObject parameters, Class cl, CallBack listener) {
        Map<String, Object> map = new HashMap<>();
        if (parameters != null) {
            List<String> keys = new ArrayList<>();
            //处理空参数
            for (String str : parameters.keySet()) {
                if (RxDataTool.isEmpty(parameters.get(str))) {
                    keys.add(str);
                }
            }
            for (String key : keys) {
                parameters.remove(key);
            }
            if (parameters.size() > 0) {
                if (RxActivityTool.isAppDebug(RxTool.getContext())) {
                    RxLogTool.d(builder.getUrl() + "params", JSONObject.toJSONString(parameters));
                }
                if (JConstant.isEncrypt()) {
                    map.put("params", AESOperator.encrypt(parameters.toJSONString()));
                } else {
                    map.put("params", parameters.toJSONString());
                }
            }
        }
        builder.setParameters(map);
        send(builder, cl, listener);
    }

    public void send(HttpBuilder builder, Class cl, CallBack listener) {
        RxKeyboardTool.hideSoftInput(RxActivityTool.currentActivity());
        if (listener == null) listener = callBack;
        if (RxNetTool.isNetworkAvailable(RxTool.getContext())) {
            try {
                RxSubscriber subscriber;
                if (JConstant.getRxsubscriber() != null) {
                    subscriber = JConstant.getRxsubscriber().newInstance();
                } else {
                    subscriber = new RxSubscriber();
                }
                subscriber.setmSubscriberOnNextListener(listener);
                subscriber.setContext(getmContext());
                subscriber.setMethod(builder.getUrl());
                subscriber.setData(cl);
                subscriber.setMsg(builder.getMsg());
                subscriber.setCancel(builder.isCancel());
                subscriber.setShowProgress(builder.isShowProgress());
                if (!RxDataTool.isNullString(JConstant.getToken()))
                    builder.getParameters().put("token", JConstant.getToken());
                builder.setTag(getFragmentName()).setHeaders(JConstant.getHeardsVal());
                subscriber.startTime = System.currentTimeMillis();
                Disposable disposable = HttpUtils.post(builder, new NetWorkResult() {
                    @Override
                    public void onNext(String result) {
                        subscriber.onNext(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                    }
                });
                addDisposable(disposable);
            } catch (Exception e) {
                Log.e("BaseRepository", e.getMessage());
                listener.onError(new ApiException(null, CodeException.RUNTIME_ERROR, "无网络连接，请检查网络是否正常", builder.getUrl()));
            }
        } else {
            listener.onError(new ApiException(null, CodeException.RUNTIME_ERROR, "无网络连接，请检查网络是否正常", builder.getUrl()));
        }
    }


    public JSONObject getJsonObject() {
        return new JSONObject();
    }


    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

}
