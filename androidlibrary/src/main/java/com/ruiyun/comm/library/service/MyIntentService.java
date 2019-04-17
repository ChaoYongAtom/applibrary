package com.ruiyun.comm.library.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.baidu.mobstat.StatService;
import com.tencent.smtt.sdk.QbSdk;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public abstract class MyIntentService extends IntentService {
    public static final String ACTION_FOO = "com.ruiyun.app.services.action.FOO";

    public MyIntentService() {
        super("MyIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                StatService.autoTrace(getApplication());
                StatService.setOn(getApplication(), StatService.JAVA_EXCEPTION_LOG);
                try {
                    QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

                        @Override
                        public void onViewInitFinished(boolean arg0) {
                            //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                            Log.d("QbSdk", " onViewInitFinished is " + arg0);
                        }

                        @Override
                        public void onCoreInitFinished() {
                            Log.d("QbSdk", " onViewInitFinished onCoreInitFinished ");
                            // TODO Auto-generated method stub
                        }
                    };
                    //x5内核初始化接口
                    QbSdk.initX5Environment(this, cb);
                } catch (Exception e) {
                }
            }
            init();

        }
    }

    public abstract void init();
}
