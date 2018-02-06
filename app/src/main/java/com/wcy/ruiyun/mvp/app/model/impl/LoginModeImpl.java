package com.wcy.ruiyun.mvp.app.model.impl;

import com.wcy.ruiyun.mvp.app.api.HttpPostService;
import com.wcy.ruiyun.mvp.app.api.entitys.BaseResult;
import com.wcy.ruiyun.mvp.app.model.LoginMode;
import com.wcy.ruiyun.mvp.app.model.impl.base.BaseModeImpl;
import com.wcy.ruiyun.mvp.app.listerers.OnLoginFinishedListener;

import org.wcy.android.retrofit.exception.ApiException;
import org.wcy.android.utils.StringUtil;

/**
 * Created by admin on 2018/1/19.
 */

public class LoginModeImpl extends BaseModeImpl implements LoginMode {
    OnLoginFinishedListener onLoginFinishedListener;

    public LoginModeImpl(OnLoginFinishedListener onLoginFinishedListener) {
        this.onLoginFinishedListener = onLoginFinishedListener;
    }

    @Override
    public void login(String username, String password) {
        if (!StringUtil.hasText(username)) {
            onLoginFinishedListener.onUsernameError();
        } else if (!StringUtil.hasText(password)) {
            onLoginFinishedListener.onPasswordError();
        } else {
//            JSONObject parameters = new JSONObject();
//            parameters.put("operatorAccount", username);
//            parameters.put("operatorPwd", password);
            sendPost(HttpPostService.Login, null, null);
        }
    }

    @Override
    public void onNext(BaseResult result) {
        super.onNext(result);
    }

    @Override
    public void onError(ApiException e, String mothead) {
        super.onError(e, mothead);
    }
}
