package com.wcy.app.lib.network.interfaces;

/**
 * @author：wcy on 18/7/31 12:33
 */
public interface DownLoadResult {
    /**
     * @param result
     */
    void onNext(String result);

    /**
     * @param e
     */
    void onError(Throwable e);

    void Progress(int progress, long currentSize, long totalSize);
}
