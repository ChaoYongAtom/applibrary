package org.wcy.android.live.event;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件总线
 * LiveBus.getDefault().postEvent("LiveData","hi LiveData");
 * <p>
 * <p>
 * LiveBus.getDefault().subscribe("LiveData").observe(this, new Observer<Object>() {
 *
 * @Override public void onChanged(@Nullable Object o) {
 * Log.e("onChanged",((String)o));
 * }
 * });
 * @author：王朝勇 on 19/1/31 17:22
 */
public class LiveBus {

    private static volatile LiveBus instance;

    private final ConcurrentHashMap<String, LiveBusData<Object>> mLiveBus;

    private LiveBus() {
        mLiveBus = new ConcurrentHashMap<>();
    }

    public static LiveBus getDefault() {
        if (instance == null) {
            synchronized (LiveBus.class) {
                if (instance == null) {
                    instance = new LiveBus();
                }
            }
        }
        return instance;
    }


    public <T> MutableLiveData<T> subscribe(String key) {
        if (!mLiveBus.containsKey(key)) {
            mLiveBus.put(key, new LiveBusData<>(true));
        } else {
            LiveBusData liveBusData = mLiveBus.get(key);
            liveBusData.isFirstSubscribe = false;
        }

        return (MutableLiveData<T>) mLiveBus.get(key);
    }

    public <T> MutableLiveData<T> postEvent(String eventKey, T value) {
        MutableLiveData<T> mutableLiveData = subscribe(eventKey);
        mutableLiveData.postValue(value);
        return mutableLiveData;
    }


    public static class LiveBusData<T> extends MutableLiveData<T> {

        private boolean isFirstSubscribe;

        LiveBusData(boolean isFirstSubscribe) {
            this.isFirstSubscribe = isFirstSubscribe;
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            super.observe(owner, new ObserverWrapper<>(observer, isFirstSubscribe));
        }
    }

    private static class ObserverWrapper<T> implements Observer<T> {

        private Observer<T> observer;

        private boolean isChanged;

        private ObserverWrapper(Observer<T> observer, boolean isFirstSubscribe) {
            this.observer = observer;
            isChanged = isFirstSubscribe;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (isChanged) {
                if (observer != null) {
                    observer.onChanged(t);
                }
            } else {
                isChanged = true;
            }
        }

    }

    public void clear(String eventKey) {
        if (mLiveBus.size() > 0) {
            mLiveBus.remove(eventKey);
        }

    }

}
