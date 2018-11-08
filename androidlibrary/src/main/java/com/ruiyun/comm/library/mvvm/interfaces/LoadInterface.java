package com.ruiyun.comm.library.mvvm.interfaces;
public interface LoadInterface {
    void dataObserver();

    void showSuccess(int state, String msg);

    void showLoading();

    void showError(int state, String msg);
}
