package com.ruiyun.comm.library.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

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
        }
        return null;

    }
}
