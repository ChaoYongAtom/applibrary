package com.ruiyun.comm.library.mvvm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ruiyun.comm.library.mvvm.interfaces.StateConstants;
import com.ruiyun.comm.library.utils.ParameterizedTypeUtil;

import org.wcy.android.utils.RxActivityTool;

/**
 * @author：wcy on 18/7/26 16:15
 */
public class BaseViewModel<T extends AbsRepository> extends AndroidViewModel {

    public MutableLiveData<String> loadState;

    public T mRepository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        loadState = new MutableLiveData<>();
        mRepository = ParameterizedTypeUtil.getNewInstance(this, 0);
        if (mRepository != null) mRepository.setmContext(RxActivityTool.currentActivity());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mRepository != null) {
            mRepository.unSubscribe();
        }
    }

    /**
     * 封装错误返回信息
     *
     * @param
     * @return
     */
    public String getStateError(int state, String msg) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(StateConstants.ERROR_STATE).append(",").append(state).append(",").append(msg == null ? "操作失败" : msg);
        return stringBuffer.toString();
    }

    public String getStateSuccess() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(StateConstants.SUCCESS_STATE).append(",").append(StateConstants.SUCCESS_STATE);
        return stringBuffer.toString();
    }

    public String getStateSuccess(int state, String msg) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(StateConstants.SUCCESS_STATE).append(",").append(state).append(",").append(msg == null ? "-" : msg);
        return stringBuffer.toString();
    }
}
