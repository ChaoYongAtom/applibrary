package libarary.app.wcy.com.myapplication.uibase.base;

import android.os.Bundle;
import android.view.View;

import com.ruiyun.comm.library.api.entitys.BaseResult;
import com.ruiyun.comm.library.ui.LibFragment;
import org.wcy.android.utils.RxDataTool;

/**
 * Created by wcy on 2018/1/18.
 */

public abstract class BaseFragment extends LibFragment {

    @Override
    protected void initTitle(String title) {
        if (!RxDataTool.isNullString(title)) {
            if (getHeaderLayout() != null) {
                getHeaderLayout().setTitleText(title);
                getHeaderLayout().getNavigationView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishFramager();
                    }
                });
            }
        }

    }
    /**
     * fragment切换
     *
     * @param toFragment 目标fragment
     * @param bundle     参数
     */
    public void startFragment(BaseFragment toFragment, Bundle bundle) {
        toFragment.setArguments(bundle);
        start(toFragment);
    }

    public void startFragmentForResult(BaseFragment toFragment, Bundle bundle, int requestCode) {
        toFragment.setArguments(bundle);
        startForResult(toFragment, requestCode);
    }

    @Override
    public void onNext(BaseResult result) {
        //super.onNext(result);
    }
}
