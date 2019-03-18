package com.ruiyun.comm.library.mvvm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ruiyun.comm.library.mvvm.event.LiveBus;
import com.ruiyun.comm.library.mvvm.interfaces.CallBack;
import com.ruiyun.comm.library.mvvm.interfaces.StateConstants;
import com.ruiyun.comm.library.utils.ParameterizedTypeUtil;

import org.wcy.android.retrofit.exception.ApiException;
import org.wcy.android.utils.RxActivityTool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：wcy on 18/7/26 16:15
 */
public class BaseViewModel<T extends AbsRepository> extends AndroidViewModel implements CallBack {

    public MutableLiveData<String> loadState;
    private String fragmentName = "";
    public T mRepository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        loadState = new MutableLiveData<>();
        mRepository = ParameterizedTypeUtil.getNewInstance(this, 0);
        if (mRepository != null) {
            mRepository.setmContext(RxActivityTool.currentActivity());
            mRepository.setCallBack(this);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mRepository != null) {
            mRepository.unSubscribe();
        }
    }

    protected void postData(RxResult rxResult) {
        postData(rxResult.getResult(), rxResult.getClassName());
    }

    protected void postData(Object object, String tag) {
        if (object instanceof List) {
            BaseListVo baseListVo = new BaseListVo();
            baseListVo.data = (List) object;
            LiveBus.getDefault().postEvent(fragmentName.concat(tag).concat("list"), baseListVo);
        } else {
            LiveBus.getDefault().postEvent(fragmentName.concat(tag), object);
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
        stringBuffer.append(StateConstants.ERROR_STATE).append("@").append(state).append("@").append(msg == null ? "操作失败" : msg);
        return stringBuffer.toString();
    }

    public String getStateError() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(StateConstants.ERROR_STATE).append("@").append("1").append("@").append("操作失败");
        return stringBuffer.toString();
    }

    public String getStateSuccess() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(StateConstants.SUCCESS_STATE).append("@").append(StateConstants.SUCCESS_STATE);
        return stringBuffer.toString();
    }

    public String getStateSuccess(int state, String msg) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(StateConstants.SUCCESS_STATE).append("@").append(state).append("@").append(msg == null ? "@" : msg);
        return stringBuffer.toString();
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
        if (mRepository != null) mRepository.setFragmentName(fragmentName);
    }

    @Override
    public void onNext(RxResult result) {
        if (result.getResult() == null) {
            loadState.postValue(getStateSuccess(1, result.getMsg()));
        } else {
            postData(result);
        }

    }

    @Override
    public void onError(ApiException e) {
        loadState.postValue(getStateError(1, e.getDisplayMessage()));
    }
}
