package com.ruiyun.comm.library.utils;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

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
        if (object != null) {
            try {
                return ((Class<T>) ((ParameterizedType) (object.getClass()
                        .getGenericSuperclass())).getActualTypeArguments()[i])
                        .newInstance();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static <T> T getInstance(Object object, int i) {
        if (object != null) {
            try {
                return (T) ((ParameterizedType) object.getClass()
                        .getGenericSuperclass())
                        .getActualTypeArguments()[i];
            }catch (Exception e){
            }
        }
        return null;

    }

    public static <T extends BasePresenter, M extends BaseModel, V extends BaseView> T init(Object o, V pView, BaseActivity appCompatActivity, LifecycleProvider lifecycleProvider, boolean isActivity) {
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
        presenter.attachModelView(pView, mModel, appCompatActivity, lifecycleProvider, isActivity);
        return presenter;
    }

    public static <T extends ViewModel> T VMProviders(Object object) {
        try {
            Class<T> tClass = ParameterizedTypeUtil.getInstance(object, 0);
            if (tClass != null) {
                if (object instanceof AppCompatActivity) {
                    return ViewModelProviders.of((AppCompatActivity) object).get(tClass);
                } else if (object instanceof Fragment) {
                    return ViewModelProviders.of((Fragment) object).get(tClass);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
