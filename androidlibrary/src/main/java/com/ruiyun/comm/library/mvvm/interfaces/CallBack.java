package com.ruiyun.comm.library.mvvm.interfaces;



import com.ruiyun.comm.library.mvvm.RxResult;

import org.wcy.android.retrofit.exception.ApiException;


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
