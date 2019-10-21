package com.wcy.app.lib.update;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.wcy.app.lib.update.interfaces.UpdateInterface;


/**
 * 版本更新处理
 *
 * @author wcy
 */
public class UpdateApkUtil {

    public static void Update(Context mContext, VersionBean uploadBean, UpdateInterface callBack) {
        if (uploadBean.isUpdate(mContext)) {
            if (callBack != null && !uploadBean.isAlert) {
                if (callBack != null) {
                    callBack.succeed(uploadBean);
                }
            } else {
                UIData uiData = UIData.create();
                uiData.setDownloadUrl(uploadBean.download_url);
                uiData.setTitle(uploadBean.title);
                uiData.setContent(uploadBean.getUpdateContent());
                DownloadBuilder builder = AllenVersionChecker.getInstance().downloadOnly(uiData);
                builder.setForceRedownload(true);
                builder.setCustomVersionDialogListener((context, versionBundle) -> {
                    BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.update_version_layout);
                    TextView versionchecklib_version_dialog_cancel = baseDialog.findViewById(R.id.versionchecklib_version_dialog_cancel);
                    //versionBundle 就是UIData，之前开发者传入的，在这里可以拿出UI数据并展示
                    android.view.View viewline = baseDialog.findViewById(R.id.view_line);
                    TextView tvTitle = baseDialog.findViewById(R.id.tv_title);
                    TextView tv_updatecreate = baseDialog.findViewById(R.id.tv_updatecreate);
                    TextView tv_apksize = baseDialog.findViewById(R.id.tv_apksize);
                    TextView tv_version = baseDialog.findViewById(R.id.tv_version);
                    TextView textView = baseDialog.findViewById(R.id.tv_msg);
                    TextView tv_ca = baseDialog.findViewById(R.id.versionchecklib_version_dialog_cancel);
                    tvTitle.setText(versionBundle.getTitle());
                    if (uploadBean.versionNum == null || "".equals(uploadBean.versionNum)) {
                        tv_version.setVisibility(View.GONE);
                    } else {
                        tv_version.setText("版本：" + uploadBean.versionNum);
                    }
                    if (uploadBean.apkSize == null || "".equals(uploadBean.apkSize)) {
                        tv_apksize.setVisibility(View.GONE);
                    } else {
                        tv_apksize.setText("包大小：" + uploadBean.apkSize);
                    }
                    if (uploadBean.createDate == null || "".equals(uploadBean.createDate)) {
                        tv_updatecreate.setVisibility(View.GONE);
                    } else {
                        tv_updatecreate.setText("更新时间：" + uploadBean.createDate);
                    }
                    textView.setText(uploadBean.getUpdateContent());
                    textView.setMovementMethod(ScrollingMovementMethod.getInstance());
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
                    builder.setOnCancelListener(() -> callBack.succeed(uploadBean));
                }
                builder.executeMission(mContext);
            }

        } else {
            if (callBack == null) {
                toastTest(mContext, "你已经是最新版了");
            } else {
                callBack.succeed(uploadBean);
            }
        }
    }

    private static void toastTest(Context context, String msg) {
        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                //放在UI线程弹Toast
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            });
        }).start();
    }
}