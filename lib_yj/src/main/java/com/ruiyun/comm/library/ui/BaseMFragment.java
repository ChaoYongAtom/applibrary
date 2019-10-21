package com.ruiyun.comm.library.ui;

import android.os.Bundle;

import com.baidu.mobstat.StatService;
import com.ruiyun.comm.library.live.BaseViewModel;

import com.ruiyun.comm.library.utils.TurnFragmentUtil;

import org.wcy.android.utils.RxDataTool;


public class BaseMFragment<T extends BaseViewModel> extends org.wcy.android.ui.BaseMFragment<T> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatService.onPageStart(getThisContext(), getClassName() + (RxDataTool.isNullString(setTitle()) ? "" : setTitle()));
    }

    /**
     * 跳转到一个新的activiy的fragment
     *
     * @param cl
     * @param bundle
     */
    public void startActivityToFragment(Class cl, Bundle bundle) {
        TurnFragmentUtil.startFragment(getThisContext(), cl, bundle);
    }


    /**
     * 跳转到一个新的activiy带返回的fragment
     *
     * @param cl
     * @param bundle
     * @param requestCode
     */
    public void startActivityToFragmentForResult(Class cl, Bundle bundle, Integer requestCode) {
        TurnFragmentUtil.startFragmentForResult(getThisActivity(), cl, bundle, requestCode);
    }

    @Override
    public void onDestroy() {
        StatService.onPageEnd(getThisContext(), getClassName() + (RxDataTool.isNullString(setTitle()) ? "" : setTitle()));
        super.onDestroy();
    }

}
