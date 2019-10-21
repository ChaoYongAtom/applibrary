package com.wcy.app.lib.web.file;

public interface DownLoadResultListener {


    void success(String path);

    void error(String path, String resUrl, String cause, Throwable e);

}
