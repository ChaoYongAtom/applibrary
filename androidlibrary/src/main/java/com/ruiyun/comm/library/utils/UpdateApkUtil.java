package com.ruiyun.comm.library.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.allenliu.versionchecklib.callback.APKDownloadListener;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.ruiyun.comm.library.api.entitys.UploadBean;
import com.ruiyun.comm.library.api.entitys.VersionBean;
import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.mvvm.RxResult;
import com.ruiyun.comm.library.mvvm.interfaces.CallBack;

import org.wcy.android.BuildConfig;
import org.wcy.android.R;
import org.wcy.android.utils.AESOperator;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.utils.RxLogTool;
import org.wcy.android.utils.RxTool;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 版本更新处理
 *
 * @author wcy
 */
public class UpdateApkUtil {
    private static String TAG = "UpdateApkUtil";
    public static void Update(Context context, CallBack callBack) {
        RxLogTool.d(TAG, "获取版本信息开始..............................");
        if (callBack == null) {
            toastTest(context, "正在检查，请稍候...");
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(6, TimeUnit.SECONDS).writeTimeout(6, TimeUnit.SECONDS).readTimeout(6, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = builder.build();
        RequestBody body = RequestBody.create(JSON, "");
        final Request request = new Request.Builder().url(JConstant.getHttpUrl() + JConstant.VersionName).addHeader("headers", JConstant.getHeardsVal()).post(body).build();
        Call call = okHttpClient.newCall(request);
        RxLogTool.d(TAG,  JConstant.getHttpUrl() + JConstant.VersionName);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                updateError(callBack, context);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.code() == 200) {
                        //获取接口返回数据
                        String string = response.body().string();
                        RxLogTool.d("onResponse", string);
                        RxResult baseResult = JSONObject.parseObject(string, RxResult.class);
                        if (baseResult.getCode() == 200) {
                            if (JConstant.isEncrypt()) {
                                baseResult.setResult(AESOperator.decrypt(baseResult.getResult()));
                            } else {
                                JSONObject jsonObject = baseResult.getResult();
                                baseResult.setResult(jsonObject.toJSONString());
                            }
                            RxLogTool.d("onResponse", baseResult.getResult());
                            VersionBean uploadBean = JSONObject.parseObject(baseResult.getResult(), VersionBean.class);
                            baseResult.setResult(uploadBean);
                            //判断服务器是否有新版本
                            if (uploadBean.isUpdate()) {
                                UIData uiData = UIData.create();
                                uiData.setDownloadUrl(uploadBean.download_url);
                                uiData.setTitle(uploadBean.title);
                                uiData.setContent(uploadBean.getUpdateContent());
                                DownloadBuilder builder = AllenVersionChecker.getInstance().downloadOnly(uiData);
                                builder.setForceRedownload(true);

                                builder.setCustomVersionDialogListener((context, versionBundle) -> {
                                    BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.update_version_layout);
                                    TextView versionchecklib_version_dialog_cancel=baseDialog.findViewById(R.id.versionchecklib_version_dialog_cancel);
                                    //versionBundle 就是UIData，之前开发者传入的，在这里可以拿出UI数据并展示
                                    android.view.View viewline = baseDialog.findViewById(R.id.view_line);
                                    TextView tvTitle = baseDialog.findViewById(R.id.tv_title);
                                    TextView tv_updatecreate = baseDialog.findViewById(R.id.tv_updatecreate);
                                    TextView tv_apksize = baseDialog.findViewById(R.id.tv_apksize);
                                    TextView tv_version = baseDialog.findViewById(R.id.tv_version);
                                    TextView textView = baseDialog.findViewById(R.id.tv_msg);
                                    TextView tv_ca = baseDialog.findViewById(R.id.versionchecklib_version_dialog_cancel);
                                    tvTitle.setText(versionBundle.getTitle());
                                    tv_version.setText("版本：" + uploadBean.versionNum);
                                    tv_apksize.setText("包大小：" + uploadBean.apkSize);
                                    tv_updatecreate.setText("更新时间：" + uploadBean.createDate);
                                    textView.setText(uploadBean.getUpdateContent());
                                    if (uploadBean.is_must_update) {
                                        baseDialog.setCancelable(false);
                                        viewline.setVisibility(android.view.View.GONE);
                                        tv_ca.setVisibility(android.view.View.GONE);
                                        versionchecklib_version_dialog_cancel.setVisibility(View.GONE);
                                    }
                                    return baseDialog;
                                });
                                //通过是否有回调函数来判断是否是启动页请求的接口
                                if (callBack != null) {
                                    builder.setOnCancelListener(() -> callBack.onNext(baseResult));
                                }
                                builder.executeMission(RxTool.getContext());
                            } else {
                                if (callBack == null) {
                                    toastTest(context, "你已经是最新版了");
                                } else {
                                    callBack.onNext(baseResult);
                                }
                            }
                        } else {
                            updateError(callBack, context);
                        }
                    } else {
                        updateError(callBack, context);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    updateError(callBack, context);
                }

            }
        });
    }

    private static void updateError(CallBack callBack, Context context) {
        if (callBack == null) {
            toastTest(context, "检查新版本失败，请稍后重试");
        } else {
            callBack.onError(null);
        }
    }

    private static void toastTest(Context context, String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        //放在UI线程弹Toast
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }
}