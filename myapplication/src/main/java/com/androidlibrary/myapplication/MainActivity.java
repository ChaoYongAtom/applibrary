package com.androidlibrary.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

import org.wcy.android.utils.RxPermissionsTool;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxPermissionsTool.with(this).addPermission(RxPermissionsTool.PERMISSION_WRITE_EXTERNAL_STORAGE).addPermission(RxPermissionsTool.PERMISSION_READ_EXTERNAL_STORAGE).addPermission(RxPermissionsTool.PERMISSION_READ_PHONE_STATE).addPermission(RxPermissionsTool.PERMISSION_CAMERA).addPermission(RxPermissionsTool.PERMISSION_ACCESS_FINE_LOCATION).addPermission(RxPermissionsTool.PERMISSION_ACCESS_COARSE_LOCATION).addPermission(RxPermissionsTool.REQUEST_INSTALL_PACKAGES).initPermission();
        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create(MainActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.picture_white_style)
                        .maxSelectNum(1)
                        .selectionMode(PictureConfig.SINGLE)//单选还是多选
                        .isCamera(true)
                        .previewEggs(true)
                        .openClickSound(false)//是否开启点击声音
                        .forResult(PictureConfig.CHOOSE_REQUEST);
//                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
//                startActivity(intent);
            }
        });
    }
}
