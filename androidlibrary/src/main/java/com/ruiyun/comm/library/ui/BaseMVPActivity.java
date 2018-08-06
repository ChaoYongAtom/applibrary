package com.ruiyun.comm.library.ui;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ruiyun.comm.library.mvp.BaseModeImpl;
import com.ruiyun.comm.library.mvp.BasePresenter;
import com.ruiyun.comm.library.mvp.BaseView;
import com.ruiyun.comm.library.utils.ParameterizedTypeUtil;


/**
 * 公共baseactivity
 * Created by wcy on 2018/1/18.
 */

public abstract class BaseMVPActivity<T extends BasePresenter, M extends BaseModeImpl> extends BaseActivity implements BaseView {

    protected T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //内部获取第一个类型参数的真实类型  ，反射new出对象
        presenter = ParameterizedTypeUtil.init(this, this, this);
    }
    @Override
    protected void onDestroy() {
        if (presenter != null) presenter.onDettach();
        super.onDestroy();
    }
}
