package com.ruiyun.comm.library.live;

import android.app.Application;

import androidx.annotation.NonNull;

import com.ruiyun.comm.library.live.interfaces.CallBack;

import org.wcy.android.live.AbsViewModel;
import com.wcy.app.lib.network.exception.ApiException;


/**
 * @author：wcy on 18/7/26 16:15
 */
public class BaseViewModel<T extends BaseRepository> extends AbsViewModel<T> implements CallBack {
    public BaseViewModel(@NonNull Application application) {
        super(application);
        if (mRepository != null) {
            mRepository.setCallBack(this);
        }
    }

    @Override
    public void onNext(RxResult result) {
        if (result.getResult() == null) {
            succeed(result.getMsg());
        } else {
            postData(result);
        }
    }

    @Override
    public void onError(ApiException e) {
        error(e.getDisplayMessage());
    }
}
