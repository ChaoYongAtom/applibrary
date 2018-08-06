package libarary.app.wcy.com.myapplication.uibase.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ruiyun.comm.library.api.entitys.BaseResult;
import com.ruiyun.comm.library.ui.BaseMVPActivity;

import org.wcy.android.retrofit.exception.ApiException;
import org.wcy.android.view.EmptyLayout;

import butterknife.BindView;
import libarary.app.wcy.com.myapplication.R;

/*
* Created by TY on 2017/7/26.
*/
public abstract class BaseEmptyActivity extends BaseMVPActivity {
    @BindView(R.id.emptylayout)
    protected EmptyLayout emptyLayout;


    @Override
    public void setView(int layoutID, String title) {
        super.setView(layoutID, title);
        emptyLayout.setErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyLayout.showLoading();
                fetchData();
            }
        });
        emptyLayout.showLoading();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        fetchData();
    }

    protected abstract void fetchData();

    @Override
    public void onNext(BaseResult result) {
        super.onNext(result);
        emptyLayout.showView();
        bindingData(result);

    }

    protected abstract void bindingData(BaseResult result);

    @Override
    public void onError(ApiException e, String mothead) {
        super.onError(e, mothead);
        emptyLayout.showError();
    }

}
