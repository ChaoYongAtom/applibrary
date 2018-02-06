package com.wcy.ruiyun.mvp.app.ui.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wcy.ruiyun.mvp.app.R;
import com.wcy.ruiyun.mvp.app.api.entitys.BaseResult;
import com.wcy.ruiyun.mvp.app.presenter.LoginPresenter;
import com.wcy.ruiyun.mvp.app.ui.common.BaseFragment;
import com.wcy.ruiyun.mvp.app.view.LoginView;

import org.wcy.android.retrofit.exception.ApiException;
import org.wcy.android.view.EmptyLayout;
import org.wcy.android.view.refresh.MaterialRefreshListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * loginFragemnet
 */
public class LoginFragment extends BaseFragment<LoginPresenter> implements LoginView {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;

    public static LoginFragment newInstance() {
        LoginFragment newFragment = new LoginFragment();
        return newFragment;
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = setView(inflater, R.layout.fragment_login);
        return view;
    }

    @Override
    protected void initView() {
        emptyLayout.setOnRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getThisActivity(), "onRefresh", Toast.LENGTH_LONG).show();
                emptyLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emptyLayout.onRefreshComplete();

                    }
                }, 3000);

            }

            @Override
            public void onRefreshLoadMore() {
                Toast.makeText(getThisActivity(), "onRefreshLoadMore", Toast.LENGTH_LONG).show();
                emptyLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emptyLayout.onRefreshComplete();

                    }
                }, 3000);
            }
        });
        emptyLayout.showView();
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        presenter.validateCredentials(etName.getText().toString(), etPassword.getText().toString());
    }

    @Override
    public void setUserName() {
        etName.setError("请输入用户名");
    }

    @Override
    public void setPassword() {
        etPassword.setError("请输入密码");
    }

    @Override
    public void onNext(BaseResult result) {
        tvMsg.setTextColor(Color.BLACK);
        tvMsg.setText(result.getMsg());
        tvMsg.append(result.getData());
    }

    @Override
    public void onError(ApiException e, String mothead) {
        tvMsg.setTextColor(Color.RED);
        tvMsg.setText(e.getDisplayMessage());
    }

}
