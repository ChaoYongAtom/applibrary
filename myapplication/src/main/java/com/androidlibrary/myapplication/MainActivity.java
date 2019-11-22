package com.androidlibrary.myapplication;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidlibrary.myapplication.andfix.KotlinBug;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.live.RxResult;
import com.ruiyun.comm.library.live.interfaces.CallBack;
import com.ruiyun.comm.library.ui.BaseMActivity;
import com.wcy.app.lib.network.HttpUtils;
import com.wcy.app.lib.network.exception.ApiException;
import com.wcy.app.lib.update.VersionBean;
import com.wcy.app.time.ChangeTimeDialogUtils;

import org.wcy.android.utils.RxLogTool;
import org.wcy.android.utils.RxPermissionsTool;
import org.wcy.android.utils.RxTool;

import java.util.List;

public class MainActivity extends BaseMActivity<GuideModel> implements CallBack {
    TextView msg;
    ImageView image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxTool.init(getApplication());
        setView(R.layout.activity_main, "");
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
            ChangeTimeDialogUtils changeTimeDialogUtils = new ChangeTimeDialogUtils(MainActivity.this, true) {
                @Override
                public void changeedData(String date) {
                    hide();
                    toast(date);
                }
            };
            changeTimeDialogUtils.showDialog(null, 2019, 2029);
        });
        findViewById(R.id.web_btn).setOnClickListener(view -> {
            KotlinBug.Companion.show(getThisContext());
            //WebViewLoad.load(getThisContext(), "http://appadviser.hejuzg.cn/module/portrait/index.html?myBuildingProjectId=5357129&userUnionId=oTBn0wtR-ZOCwCJjgqEogzMp8TMY&openId=");

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
    }

    @Override
    public void dataObserver() {
        registerObserver(VersionBean.class).observe(this, versionBean -> {
            msg.setText(versionBean.getUpdateContent());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
        toast(list.get(0).getPath());
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
