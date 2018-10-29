package libarary.app.wcy.com.myapplication.uibase.base;

import android.os.Bundle;

import com.ruiyun.comm.library.mvp.BaseModel;
import com.ruiyun.comm.library.mvp.BasePresenter;
import com.ruiyun.comm.library.mvp.BaseView;
import com.ruiyun.comm.library.utils.ParameterizedTypeUtil;

/**
 * Created by wcy on 2018/1/18.
 */

public abstract class BaseMVPFragment<P extends BasePresenter, M extends BaseModel> extends BaseFragment implements BaseView {
    protected P presenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = ParameterizedTypeUtil.init(this, this, getThisActivity(),this,false);
    }

    @Override
    public void onDestroy() {
        if (presenter != null) presenter.onDettach();
        super.onDestroy();
    }
}
