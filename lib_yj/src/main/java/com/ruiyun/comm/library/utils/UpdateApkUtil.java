package com.ruiyun.comm.library.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.live.BaseRepository;
import com.ruiyun.comm.library.live.RxResult;
import com.ruiyun.comm.library.live.interfaces.CallBack;
import com.wcy.app.lib.network.exception.ApiException;
import com.wcy.app.lib.update.VersionBean;

/**
 * 版本更新处理
 *
 * @author wcy
 */
public class UpdateApkUtil {

    public static void Update(Context context, CallBack callBack) {
        if (callBack == null) toastTest(context, "正在获取最新版本号信息，请稍等......");
        BaseRepository baseRepository = new BaseRepository();
        baseRepository.sendPost(JConstant.VersionName, null, VersionBean.class, new CallBack() {
            @Override
            public void onNext(RxResult result) {
                VersionBean bean = result.getResult();
                if (bean.isUpdate(context) && bean.isAlert) {
                    com.wcy.app.lib.update.UpdateApkUtil.Update(context, bean, () -> {
                        if (callBack != null) callBack.onNext(result);
                    });
                } else {
                    if (callBack != null) {
                        callBack.onNext(result);
                    } else {
                        toastTest(context, "您已经是最新版本了！！");
                    }
                }
            }

            @Override
            public void onError(ApiException e) {
                if (callBack != null) {
                    callBack.onError(e);
                } else {
                    toastTest(context, e.getDisplayMessage());
                }
            }
        });
    }

    private static void toastTest(Context context, String msg) {
        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                //放在UI线程弹Toast
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            });
        }).start();
    }
}