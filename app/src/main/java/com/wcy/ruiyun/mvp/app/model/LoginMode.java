package com.wcy.ruiyun.mvp.app.model;
import com.wcy.ruiyun.mvp.app.model.impl.base.BaseModel;

/**
 * Created by admin on 2018/1/18.
 */

public interface LoginMode extends BaseModel{
    void login(String user, String password);
}
