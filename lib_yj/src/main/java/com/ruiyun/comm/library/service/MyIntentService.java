package com.ruiyun.comm.library.service;

import android.app.IntentService;
import android.content.Intent;
import com.baidu.mobstat.StatService;

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
            }
            init();

        }
    }

    public abstract void init();
}
