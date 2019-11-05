package org.wcy.android.live;


import android.content.Context;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author：wct on 18/7/26 16:15
 */
public class AbsRepository {

    private String fragmentName = "";
    private CompositeDisposable mCompositeSubscription;
    private Context mContext;



    public String getFragmentName() {
        return fragmentName;
    }

    public void addDisposable(Disposable disposable) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(disposable);
    }

    public void unSubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.isDisposed()) {
            mCompositeSubscription.clear();
            mCompositeSubscription = null;
        }
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }
}
