package com.androidlibrary.myapplication;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.live.RxResult;
import com.ruiyun.comm.library.live.interfaces.CallBack;
import com.ruiyun.comm.library.ui.BaseMActivity;
import com.wcy.app.lib.network.HttpUtils;
import com.wcy.app.lib.network.exception.ApiException;
import com.wcy.app.lib.web.utils.WebViewLoad;

import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.utils.RxLogTool;
import org.wcy.android.utils.RxPermissionsTool;
import org.wcy.android.utils.RxTool;


public class MainActivity extends BaseMActivity<GuideModel> implements CallBack {
    TextView msg;
    ImageView image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxTool.init(getApplication());
        setView(R.layout.activity_main, "");
        msg = findViewById(R.id.tv_msg);
        try {
            ApplicationInfo appInfo = RxTool.getContext().getPackageManager().getApplicationInfo(RxTool.getContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = appInfo.metaData;
            String httpUrl = bundle.getString("HTTP_URL");
            HttpUtils.init(httpUrl, 30, true);
            JConstant.setHttpPostService();
            RxLogTool.d("httpUrl", "地址初始化成功");
        } catch (Exception e) {
            e.printStackTrace();
            RxLogTool.e("httpUrl", "地址初始化失败");
        }
        RxPermissionsTool.with(this).addPermission(RxPermissionsTool.PERMISSION_WRITE_EXTERNAL_STORAGE).addPermission(RxPermissionsTool.PERMISSION_READ_EXTERNAL_STORAGE).addPermission(RxPermissionsTool.PERMISSION_READ_PHONE_STATE).addPermission(RxPermissionsTool.PERMISSION_CAMERA).addPermission(RxPermissionsTool.PERMISSION_ACCESS_FINE_LOCATION).addPermission(RxPermissionsTool.PERMISSION_ACCESS_COARSE_LOCATION).addPermission(RxPermissionsTool.REQUEST_INSTALL_PACKAGES).initPermission();
        findViewById(R.id.btnLogin).setOnClickListener(view -> {
            mViewModel.loading();
//            ChangeTimeDialogUtils changeTimeDialogUtils = new ChangeTimeDialogUtils(MainActivity.this, true) {
//                @Override
//                public void changeedData(String date) {
//                    hide();
//                    toast(date);
//                }
//            };
//            changeTimeDialogUtils.showDialog(DateUtil.getCurrentDate(DateUtil.DF_YYYY_MM_DD_HH_MM), 2019, 2029);
//
//            UIData uiData = UIData.create();
//            uiData.setDownloadUrl("http:\\/\\/ysyapp.yj.cn.com\\/apk\\/ysyapp4.4.0.apk");
//            uiData.setTitle("aaa");
//            uiData.setContent("");
//            AllenVersionChecker.getInstance().downloadOnly(uiData).executeMission(getThisContext());
        });
        findViewById(R.id.web_btn).setOnClickListener(view -> {
            // KotlinBug.Companion.show(getThisContext());
            WebViewLoad.load("http://www.baidu.com");

//            PictureSelector.create(MainActivity.this)
//                    .openGallery(PictureMimeType.ofImage())
//                    .maxSelectNum(1)
//                    .selectionMode(PictureConfig.SINGLE)//单选还是多选
//                    .isCamera(true)
//                    .previewEggs(true)
//                    .openClickSound(false)//是否开启点击声音
//                   .isChangeStatusBarFontColor(false)
//                    .forResult(PictureConfig.CHOOSE_REQUEST);
        });
        findViewById(R.id.btnAddJava).setOnClickListener(view -> {
            toast("还没有增加java文件");
        });
        findViewById(R.id.btnAddKotlin).setOnClickListener(view -> {
            toast("还没有增加Kotlin文件");
        });
        findViewById(R.id.btnScan).setOnClickListener(view -> {
            ARouter.getInstance().build("/qrcode/scan_activity").withBoolean("isVoice",true).withBoolean("isVibrate",true).withString("triAngleColor","#ffcc0000").navigation(getThisActivity(),300);
        });
    }

    @Override
    public void dataObserver() {
//        registerObserver(VersionBean.class).observe(this, versionBean -> {
//           // msg.setText(versionBean.getUpdateContent());
//        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
//        toast(list.get(0).getPath());
        if (resultCode == RESULT_OK) {
            msg.setText(data.getStringExtra("context"));
        }
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        RxActivityTool.AppExit(getThisContext());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            RxActivityTool.AppExit(getThisContext());
        }
        return false;
    }

    @Override
    public void onNext(RxResult result) {
        msg.setText("返回信息" + result);
    }

    @Override
    public void onError(ApiException e) {
        msg.setText("错误信息" + e.getDisplayMessage());
    }
}
