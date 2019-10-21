package com.ruiyun.comm.library.live.interfaces;
import com.ruiyun.comm.library.live.RxResult;
import com.wcy.app.lib.network.exception.ApiException;


/**
 * @author：wcy on 18/7/31 12:33
 */
public interface CallBack {
    /**
     * @param result
     */
    void onNext(RxResult result);

    /**
     * @param e
     */
    void onError(ApiException e);
}
