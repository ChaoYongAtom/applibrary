package com.ruiyun.comm.library.utils;

import com.ruiyun.comm.library.mvp.BaseModel;
import com.ruiyun.comm.library.mvp.BasePresenter;
import com.ruiyun.comm.library.mvp.BaseView;
import com.ruiyun.comm.library.ui.BaseActivity;
import com.trello.rxlifecycle.LifecycleProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * ParameterizedTypeUtil
 *
 * @auth wangchaoyong
 * @time 16:48
 * @description 友商云V3.0
 */

public class ParameterizedTypeUtil {
    public static int getParameterizedSize(Object o) {
        Type genericClassType = o.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genericClassType).getActualTypeArguments();
        if (params == null) return 0;
        return params.length;
    }
    public static <T> T getNewInstance(Object object, int i) {
        if(object!=null){
            try {
                return ((Class<T>) ((ParameterizedType) (object.getClass()
                        .getGenericSuperclass())).getActualTypeArguments()[i])
                        .newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
    public static <T> T getInstance(Object object, int i) {
        if (object != null) {
            return (T) ((ParameterizedType) object.getClass()
                    .getGenericSuperclass())
                    .getActualTypeArguments()[i];
        }
        return null;

    }
    public static <T extends BasePresenter, M extends BaseModel, V extends BaseView> T init(Object o, V pView, BaseActivity appCompatActivity,LifecycleProvider lifecycleProvider,boolean isActivity) {
        M mModel = null;
        T presenter;
        try {
            Type genericClassType = o.getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genericClassType).getActualTypeArguments();
            if (params.length >= 2) {
                presenter = ((Class<T>) params[0]).newInstance();
                mModel = ((Class<M>) params[1]).newInstance();
            } else {
                presenter = (T) new BasePresenter();
            }
        } catch (Exception e) {
            presenter = (T) new BasePresenter();
        }
        //使得P层绑定M层和V层，持有M和V的引用
        presenter.attachModelView(pView, mModel, appCompatActivity, lifecycleProvider,isActivity);
        return presenter;
    }
}
