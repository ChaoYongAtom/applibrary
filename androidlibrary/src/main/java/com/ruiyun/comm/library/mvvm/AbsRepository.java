package com.ruiyun.comm.library.mvvm;


import android.content.Context;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author：wct on 18/7/26 16:15
 */
public abstract class AbsRepository {

    private CompositeSubscription mCompositeSubscription;
    private Context mContext;

    public AbsRepository() {

    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public Context getmContext() {
        return mContext;
    }

    protected void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public void unSubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
        }
    }
}
