package com.ruiyun.comm.library.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ruiyun.comm.library.ui.CommonActivity;

import org.wcy.android.R;
import org.wcy.android.utils.RxFragmentUtil;


/**
 * Fragment管理工具类
 *
 * @author wcy
 * @date 2017/3/14
 */
public class TurnFragmentUtil extends RxFragmentUtil {
    public static void startFragment(Context context, Class cl, Bundle bundle) {
        Intent intent = new Intent(context, CommonActivity.class);
        intent.putExtra(CommonActivity.EXTRA_FRAGMENT, cl.getName());
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }
    public static void startFragmentForResult(AppCompatActivity activity, Class cl, Bundle bundle, Integer requestCode) {

        Intent intent = new Intent(activity, CommonActivity.class);
        intent.putExtra(CommonActivity.EXTRA_FRAGMENT, cl.getName());
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 推送使用
     *
     * @param context
     * @param cl
     */
    @Deprecated
    public static void startFragment(Context context, Class cl) {
        startFragment(context, cl, null);
    }
    @Deprecated
    public static Fragment replaceFragment(AppCompatActivity activity,
                                           Class<? extends Fragment> fragmentClass, Bundle args) {
        FragmentManager fm = activity.getSupportFragmentManager();
        final String tag = fragmentClass.getName();
        Fragment fragment = fm.findFragmentByTag(tag);
        boolean isFragmentExist = true;
        if (fragment == null) {
            try {
                isFragmentExist = false;
                fragment = fragmentClass.newInstance();
                fragment.setArguments(new Bundle());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (fragment.isAdded()) {
            return fragment;
        }
        if (args != null && !args.isEmpty()) {
            fragment.getArguments().putAll(args);
        }
        FragmentTransaction ft = fm.beginTransaction();
        if (isFragmentExist) {
            ft.replace(R.id.common_frame, fragment);
        } else {
            ft.replace(R.id.common_frame, fragment, tag);
        }
        ft.commitAllowingStateLoss();
        return fragment;
    }
    @Deprecated
    public static void turnToFragment(AppCompatActivity activity, Class<? extends Fragment> fragmentClass, Bundle bundle) {
        turnToFragment(activity.getSupportFragmentManager(), R.id.common_frame, fragmentClass, bundle);
    }

    /**
     * Fragment跳转
     *
     * @param fm
     * @param fragmentClass
     * @param args
     */
    @Deprecated
    public static void turnToFragment(FragmentManager fm, int containerId,
                                      Class<? extends Fragment> fragmentClass, Bundle args) {
        final String tag = fragmentClass.getName();
        Fragment fragment = fm.findFragmentByTag(tag);
        boolean isFragmentExist = true;
        if (fragment == null) {
            try {
                isFragmentExist = false;
                fragment = fragmentClass.newInstance();
                fragment.setArguments(new Bundle());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (fragment.isAdded()) {
            return;
        }
        if (args != null && !args.isEmpty()) {
            fragment.getArguments().putAll(args);
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.back_left_in, R.anim.back_right_out);
        if (isFragmentExist) {
            ft.add(containerId, fragment);
        } else {
            ft.add(containerId, fragment, tag);
        }

        ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
    }

    /**
     * Fragment跳转
     *
     * @param fm
     * @param fragmentClass
     * @param args
     */
    @Deprecated
    public static void turnToFragment(FragmentManager fm, int containerId,
                                      Class<? extends Fragment> fragmentClass, Bundle args,
                                      boolean backAble) {
        final String tag = fragmentClass.getName();
        Fragment fragment = fm.findFragmentByTag(tag);
        boolean isFragmentExist = true;
        if (fragment == null) {
            try {
                isFragmentExist = false;
                fragment = fragmentClass.newInstance();
                fragment.setArguments(new Bundle());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (fragment.isAdded()) {
            return;
        }
        if (args != null && !args.isEmpty()) {
            fragment.getArguments().putAll(args);
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.back_left_in, R.anim.back_right_out);
        if (isFragmentExist) {
            ft.add(containerId, fragment);
        } else {
            ft.add(containerId, fragment, tag);
        }
        if (backAble) {
            ft.addToBackStack(tag);
        }
        ft.commitAllowingStateLoss();
    }


}