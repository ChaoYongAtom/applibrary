package org.wcy.android.live;

public interface LoadInterface {
    void dataObserver();

    void showSuccess(int state, String msg);

    void showLoading();

    void showError(int state, String msg);

    String getClassName();

    String getStateEventKey();
}
