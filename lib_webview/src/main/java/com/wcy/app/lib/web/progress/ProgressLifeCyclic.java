package com.wcy.app.lib.web.progress;


public interface ProgressLifeCyclic {

    void showProgressBar();

    void setProgressBar(int newProgress);

    void finish();
}
