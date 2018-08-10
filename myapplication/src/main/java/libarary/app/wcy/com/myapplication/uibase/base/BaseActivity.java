package libarary.app.wcy.com.myapplication.uibase.base;

import com.ruiyun.comm.library.api.entitys.BaseResult;

import org.wcy.android.utils.RxActivityTool;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseActivity extends com.ruiyun.comm.library.ui.BaseActivity {
    Unbinder unbinder;

    /**
     * @param layoutID
     */
    public void setView(int layoutID) {
        setContentView(layoutID);
        unbinder = ButterKnife.bind(this);

    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) unbinder.unbind();
        RxActivityTool.finishActivity(this);
        super.onDestroy();
    }

    @Override
    public void onNext(BaseResult result) {
        super.onNext(result);
    }
}
