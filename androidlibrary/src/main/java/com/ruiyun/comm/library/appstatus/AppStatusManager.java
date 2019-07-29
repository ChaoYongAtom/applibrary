package com.ruiyun.comm.library.appstatus;

/**
 * AppStatusManager
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/6/18
 * @description YjSales
 */
public class AppStatusManager {

    public int appStatus= AppStatus.STATUS_FORCE_KILLED;        //APP状态 初始值为没启动 不在前台状态

    public static AppStatusManager appStatusManager;

    private AppStatusManager() {

    }


    public static AppStatusManager getInstance() {
        if (appStatusManager == null) {
            appStatusManager = new AppStatusManager();
        }
        return appStatusManager;
    }

    public int getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
    }
}
