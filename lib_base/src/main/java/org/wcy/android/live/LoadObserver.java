package org.wcy.android.live;


import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import org.wcy.android.interfaces.StateConstants;


public class LoadObserver implements Observer<String> {
    LoadInterface loadInterface;

    public LoadObserver(LoadInterface loadInterface) {
        this.loadInterface = loadInterface;
    }

    @Override
    public void onChanged(@Nullable String result) {
        if (!TextUtils.isEmpty(result)) {
            String[] str = result.split("@");
            if (str != null) {
                int state = Integer.parseInt(str[0]);

                if (StateConstants.ERROR_STATE == state) {
                    int stateT = Integer.parseInt(str[1]);
                    if (str.length > 2) {
                        loadInterface.showError(stateT, str[2]);
                    } else {
                        loadInterface.showError(stateT, "操作失败");
                    }
                } else if (StateConstants.LOADING_STATE == state) {
                    loadInterface.showLoading();
                } else if (StateConstants.SUCCESS_STATE == state) {
                    int stateT = Integer.parseInt(str[1]);
                    if (str.length > 2) {
                        loadInterface.showSuccess(stateT, str[2]);
                    } else {
                        loadInterface.showSuccess(stateT, "");
                    }
                }
            }
        }
    }
}
