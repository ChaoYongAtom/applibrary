package com.wcy.ruiyun.mvp.app.presenter;

import com.wcy.ruiyun.mvp.app.listerers.OnLoginFinishedListener;
import com.wcy.ruiyun.mvp.app.model.LoginMode;
import com.wcy.ruiyun.mvp.app.model.impl.LoginModeImpl;
import com.wcy.ruiyun.mvp.app.model.impl.base.BaseModel;
import com.wcy.ruiyun.mvp.app.view.LoginView;


/**
 * Created by admin on 2018/1/18.
 */

public class LoginPresenter extends BasePresenter<LoginView> implements OnLoginFinishedListener {
    LoginMode loginMode;
    public LoginPresenter(LoginView loginView) {
        super(loginView);
    }
    public void validateCredentials(String username, String password) {
        loginMode.login(username, password);
    }
    public void onUsernameError() {
        view.setUserName();
    }

    public void onPasswordError() {
        view.setPassword();
    }
    @Override
    protected BaseModel init() {
        loginMode = new LoginModeImpl( this);
        return loginMode;
    }
}
