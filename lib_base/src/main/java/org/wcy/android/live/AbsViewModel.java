package org.wcy.android.live;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.wcy.android.interfaces.StateConstants;
import org.wcy.android.live.event.LiveBus;
import org.wcy.android.utils.ParameterizedTypeUtil;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.utils.RxDataTool;

import java.util.List;

/**
 * @author：wcy on 18/7/26 16:15
 */
public class AbsViewModel<T extends AbsRepository> extends AndroidViewModel {

    public MutableLiveData<String> loadState;
    private String fragmentName = "";
    public T mRepository;

    public AbsViewModel(@NonNull Application application) {
        super(application);
        loadState = new MutableLiveData<>();
        mRepository = ParameterizedTypeUtil.getNewInstance(this, 0);
        if (mRepository != null) {
            mRepository.setmContext(RxActivityTool.currentActivity());
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }
    public void unSubscribe(){
        if (mRepository != null) {
            mRepository.unSubscribe();
        }
    }
    protected void postData(Object object, String tag) {
        if (!RxDataTool.isNullString(tag)) {
            if (object instanceof List) {
                BaseListVo baseListVo = new BaseListVo();
                baseListVo.data = (List) object;
                LiveBus.getDefault().postEvent(fragmentName.concat(tag).concat("list"), baseListVo);
            } else {
                LiveBus.getDefault().postEvent(fragmentName.concat(tag), object);
            }
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
    protected void postData(BaseResult rxResult) {
        postData(rxResult.getResult(), rxResult.getClassName());
    }
    public void succeed(String result) {
        if (!RxDataTool.isNullString(result)) {
            loadState.postValue(getStateSuccess(1, result));
        }
    }

    public void error(String e) {
        if (!RxDataTool.isNullString(e)) {
            loadState.postValue(getStateError(1, e));
        }
    }
}
