package com.wcy.app.lib.update.interfaces;

import com.wcy.app.lib.update.VersionBean;

/**
 * UpdateInterface
 *
 * @version 4.0.0
 * @auth wangchaoyong
 * @time 2019/10/21
 * @description applibrary
 */
public interface UpdateInterface {
    public void succeed(VersionBean versionBean);

    public void error(String msg);
}
