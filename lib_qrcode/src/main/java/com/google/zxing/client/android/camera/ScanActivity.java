package com.google.zxing.client.android.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.zxing.R;
import com.google.zxing.Result;
import com.google.zxing.client.android.BaseCaptureActivity;
import com.gyf.immersionbar.ImmersionBar;

/**
 * ScanActivity
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2020/1/14
 * @description applibrary
 */
@Route(path = "/qrcode/scan_activity")
public class ScanActivity extends BaseCaptureActivity {
    boolean isVoice, isVibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_capture);
        isVoice = getIntent().getBooleanExtra("isVoice", false);
        isVibrate = getIntent().getBooleanExtra("isVibrate", false);
        getAutoScannerView().setColor(getIntent().getStringExtra("triAngleColor"), getIntent().getStringExtra("lineColor"), getIntent().getStringExtra("textColor"));
        initImmersionBar();
    }

    private void initImmersionBar() {
        ImmersionBar.with(this).titleBar(R.id.title_layout).statusBarDarkFont(true, 0.2f).init();
    }

    @Override
    public void dealDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        playBeepSoundAndVibrate(isVoice, isVibrate);
        Intent intent = getIntent();
        intent.putExtra("context", rawResult.getText());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
