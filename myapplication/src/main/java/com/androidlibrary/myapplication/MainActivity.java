package com.androidlibrary.myapplication;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.ruiyun.comm.library.ui.BaseMActivity;
import com.wcy.app.lib_network.HttpUtils;
import com.wcy.app.lib_network.exception.ApiException;
import com.wcy.app.lib_network.interfaces.CallBack;

import org.wcy.android.utils.ExampleUtil;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.utils.RxLogTool;
import org.wcy.android.utils.RxPermissionsTool;
import org.wcy.android.utils.RxTool;

import butterknife.BindView;

public class MainActivity extends BaseMActivity<GuideModel>  implements CallBack {
    @BindView(R.id.tv_msg)
    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxTool.init(getApplication());
        setView(R.layout.activity_main, "");
        try {
            ApplicationInfo appInfo = RxTool.getContext().getPackageManager().getApplicationInfo(RxTool.getContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = appInfo.metaData;
            String    httpUrl = bundle.getString("HTTP_URL");
            HttpUtils.init(httpUrl,30, true);
            RxLogTool.d("httpUrl", "地址初始化成功");
        } catch (Exception e) {
            e.printStackTrace();
            RxLogTool.e("httpUrl", "地址初始化失败");
        }

        RxPermissionsTool.with(this).addPermission(RxPermissionsTool.PERMISSION_WRITE_EXTERNAL_STORAGE).addPermission(RxPermissionsTool.PERMISSION_READ_EXTERNAL_STORAGE).addPermission(RxPermissionsTool.PERMISSION_READ_PHONE_STATE).addPermission(RxPermissionsTool.PERMISSION_CAMERA).addPermission(RxPermissionsTool.PERMISSION_ACCESS_FINE_LOCATION).addPermission(RxPermissionsTool.PERMISSION_ACCESS_COARSE_LOCATION).addPermission(RxPermissionsTool.REQUEST_INSTALL_PACKAGES).initPermission();
        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                object.put("systemType", "1");
                object.put("appVersion", RxActivityTool.getAppVersionName());
                object.put("mobileCode", ExampleUtil.getImei(RxTool.getContext()));
                object.put("version", "version2");
                object.put("registrationID", "111");
                HttpUtils.post("newestversion", object.toJSONString(), null,MainActivity.this);
                //图片选择
//                PictureSelector.create(MainActivity.this)
//                        .openGallery(PictureMimeType.ofImage())
//                        .theme(R.style.picture_white_style)
//                        .maxSelectNum(1)
//                        .selectionMode(PictureConfig.SINGLE)//单选还是多选
//                        .isCamera(true)
//                        .previewEggs(true)
//                        .openClickSound(false)//是否开启点击声音
//                        .forResult(PictureConfig.CHOOSE_REQUEST);
                //扫一扫
//                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
//                startActivity(intent);
            }
        });
    }

    @Override
    public void onNext(String result) {
        msg.setText("返回信息" + result);
    }

    @Override
    public void onError(ApiException e) {
        msg.setText("错误信息" + e.getDisplayMessage());
    }
}
