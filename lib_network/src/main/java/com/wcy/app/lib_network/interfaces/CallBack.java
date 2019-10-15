package com.wcy.app.lib_network.interfaces;


import com.wcy.app.lib_network.exception.ApiException;

/**
 * @author：wcy on 18/7/31 12:33
 */
public interface CallBack {
    /**
     * @param result
     */
    void onNext(String result);

    /**
     * @param e
     */
    void onError(ApiException e);
}
