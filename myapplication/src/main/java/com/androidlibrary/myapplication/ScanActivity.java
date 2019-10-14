package com.androidlibrary.myapplication;


import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.zxing.Result;
import com.google.zxing.client.android.BaseCaptureActivity;

import org.wcy.android.view.toast.ToastUtils;

public class ScanActivity extends BaseCaptureActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }


    @Override
    public void dealDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        playBeepSoundAndVibrate(false, true);
        ToastUtils.show(ScanActivity.this, rawResult.getText(), ToastUtils.NO);
        finish();
    }
}
