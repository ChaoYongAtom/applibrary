package com.androidlibrary.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.luck.picture.lib.permissions.RxPermissions;
import com.wcy.app.lib.dex.FixDexLoad;

import org.wcy.android.utils.RxPermissionsTool;

import io.reactivex.functions.Consumer;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RxPermissions(this).request(RxPermissionsTool.PERMISSION_WRITE_EXTERNAL_STORAGE,RxPermissionsTool.PERMISSION_READ_PHONE_STATE,RxPermissionsTool.PERMISSION_ACCESS_FINE_LOCATION,RxPermissionsTool.PERMISSION_CAMERA,RxPermissionsTool.PERMISSION_ACCESS_COARSE_LOCATION,RxPermissionsTool.REQUEST_INSTALL_PACKAGES).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                init();
            }
        });

    }
    private void init() {
      //  FixDexLoad.init(this,"NNWEB1E47B1D72C43B0A018DA669632B7EDPTO",true);
//        FixDexUtil fixDexUtil = FixDexUtil.getInstance();
//        fixDexUtil.init(this, "");
//        fixDexUtil.loadFixedDex("sophix-patch.jar");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, RefreshLayoutActivity.class));
                finish();
            }
        }, 500);

    }

}
