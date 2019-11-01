package com.androidlibrary.myapplication;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiyun.comm.library.common.JConstant;
import com.ruiyun.comm.library.live.RxResult;
import com.ruiyun.comm.library.live.interfaces.CallBack;
import com.ruiyun.comm.library.ui.BaseMActivity;
import com.ruiyun.comm.library.ui.WebXFragment;
import com.ruiyun.comm.library.utils.TurnFragmentUtil;
import com.ruiyun.comm.library.utils.UpdateApkUtil;
import com.wcy.app.lib.imageloader.ImageLoaderManager;
import com.wcy.app.lib.network.HttpUtils;
import com.wcy.app.lib.network.exception.ApiException;
import com.wcy.app.lib.update.VersionBean;

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
        image_view = findViewById(R.id.image_view);
        ImageLoaderManager.loadImage("http://b-ssl.duitang.com/uploads/blog/201312/04/20131204184148_hhXUT.jpeg", image_view);
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
        findViewById(R.id.btnSubmit).setOnClickListener(view -> UpdateApkUtil.Update(getThisContext(), new CallBack() {
            @Override
            public void onNext(RxResult result) {
                toast("点击了取消");
            }

            @Override
            public void onError(ApiException e) {

            }
        }));
        findViewById(R.id.btnLogin).setOnClickListener(view -> mViewModel.login());
        findViewById(R.id.web_btn).setOnClickListener(view -> {
            String url = "http://prwap.hejuzg.cn/ww/wap/h5/auth?h5ModuleNo=%7b%22moduleNo%22%3a1%7d&terminalId=33&token=67ba5cc83c1028082f19137d409d7a25";
            Bundle bundle = new Bundle();
            bundle.putString(WebXFragment.URL, url);
            TurnFragmentUtil.startFragment(getThisContext(), WebXFragment.class, bundle);
        });
    }

    @Override
    public void dataObserver() {
        registerObserver(VersionBean.class).observe(this, versionBean -> {
            msg.setText(versionBean.getUpdateContent());
        });
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
