package com.ruiyun.comm.library.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomInstallListener;
import com.ruiyun.comm.library.api.entitys.UploadBean;
import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.mvvm.RxResult;
import com.ruiyun.comm.library.mvvm.interfaces.CallBack;

import org.wcy.android.R;
import org.wcy.android.utils.AESOperator;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.utils.RxTool;

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
 * @author  wcy
 */
public class UpdateApkUtil {
    public static void Update(CallBack callBack) {
        if(callBack==null){
            Toast.makeText(RxTool.getContext(), "正在检查，请稍候...", Toast.LENGTH_LONG).show();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(6, TimeUnit.SECONDS)
                .writeTimeout(6, TimeUnit.SECONDS).readTimeout(6, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = builder.build();
        RequestBody body = RequestBody.create(JSON, "");
        final Request request = new Request.Builder().url(JConstant.getHttpUrl() + "newestversion").addHeader("headers", JConstant.getHeardsVal()).post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                updateError(callBack);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.code() == 200) {
                        //获取接口返回数据
                        String string = response.body().string();
                        RxResult baseResult = JSONObject.parseObject(string, RxResult.class);
                        if (baseResult.getCode() == 200) {
                            JSONObject jsonObject=baseResult.getResult();
                            baseResult.setResult(jsonObject.toJSONString());
                            if (JConstant.isEncrypt()) {
                                baseResult.setResult(AESOperator.decrypt(baseResult.getResult()));
                            }
                            UploadBean uploadBean = JSONObject.parseObject(baseResult.getResult(), UploadBean.class);
                            baseResult.setResult(uploadBean);
                            //判断服务器是否有新版本
                           if(uploadBean.isUpdate()){
                               UIData uiData = UIData.create();
                               uiData.setDownloadUrl(uploadBean.download_url);
                               uiData.setTitle("新版本更新");
                               uiData.setContent("这是版本更新内容");
                               DownloadBuilder builder = AllenVersionChecker
                                       .getInstance()
                                       .downloadOnly(uiData);
                               builder.setForceRedownload(true);
                               builder.setCustomVersionDialogListener((context, versionBundle) -> {
                                   BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.update_version_layout);
                                   //versionBundle 就是UIData，之前开发者传入的，在这里可以拿出UI数据并展示
                                   TextView textView = baseDialog.findViewById(R.id.tv_msg);
                                   textView.setText(versionBundle.getContent());
                                   return baseDialog;
                               });
                               //通过是否有回调函数来判断是否是启动页请求的接口
                               if (callBack != null) {
                                   //判断是否是强制更新，如果是强制更新取消下载则直接退出应用
                                   if (uploadBean.is_must_update) {
                                       builder.setForceUpdateListener(() -> RxActivityTool.AppExit(RxTool.getContext()));
                                   } else {//如果不是强制更新取消下载则回调成功函数
                                       builder.setOnCancelListener(() -> callBack.onNext(baseResult));
                                   }
                               }
                               builder.executeMission(RxTool.getContext());
                           }else{
                               if (callBack == null) {
                                   Toast.makeText(RxTool.getContext(), "你已经是最新版了", Toast.LENGTH_LONG).show();
                               } else {
                                   callBack.onNext(baseResult);
                               }
                           }
                        } else {
                            updateError(callBack);
                        }
                    } else {
                        updateError(callBack);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    updateError(callBack);
                }

            }
        });
    }

    private static void updateError(CallBack callBack) {
        if (callBack == null) {
            Toast.makeText(RxTool.getContext(), "检查新版本失败，请稍后重试", Toast.LENGTH_LONG).show();
        } else {
            callBack.onError(null);
        }
    }
}
