package org.wcy.android.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


import org.wcy.android.R;

import java.util.List;


/**
 * Fragment管理工具类
 *
 * @author wcy
 * @date 2017/3/14
 */
public class RxFragmentUtil {


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

    /**
     * 切换tab
     *
     * @param idx
     */
    public static int showTab(int frameId, int idx, int currentTab, List<? extends Fragment> fragments, AppCompatActivity activity) {
        return showTab(frameId, idx, currentTab, fragments, activity, true);
    }

    /**
     * 切换tab
     *
     * @param idx
     */
    public static int showTab(int frameId, int idx, int currentTab, List<? extends Fragment> fragments, AppCompatActivity activity, boolean isShoWanimation) {
        if (idx != currentTab) {
            if (idx == -1) {
                FragmentTransaction ftshow = activity.getSupportFragmentManager().beginTransaction();
                ftshow.add(frameId, fragments.get(0));
                ftshow.commitAllowingStateLoss();
            } else {
                Fragment cufragment = fragments.get(currentTab);
                cufragment.onPause(); // 暂停当前tab
                FragmentTransaction fthide = obtainFragmentTransaction(idx, currentTab, activity, isShoWanimation);
                fthide.hide(cufragment);
                Fragment fragment = fragments.get(idx);
                if (fragment.isAdded()) {
                    fragment.onResume(); // 启动目标tab的onResume()
                    fthide.show(fragment);
                } else {
                    fthide.add(frameId, fragment);
                }
                fthide.commitAllowingStateLoss();
                return idx;
            }
        }
        return currentTab;
    }

    /**
     * 获取一个带动画的FragmentTransaction
     *
     * @param index
     * @return
     */
    public static FragmentTransaction obtainFragmentTransaction(int index, int index2, AppCompatActivity appCompatActivity) {
        return obtainFragmentTransaction(index, index2, appCompatActivity, true);
    }

    public static FragmentTransaction obtainFragmentTransaction(int index, int index2, AppCompatActivity appCompatActivity, boolean isShoWanimation) {
        FragmentTransaction ft = appCompatActivity.getSupportFragmentManager().beginTransaction();
        if (isShoWanimation) {
            //        // 设置切换动画
            if (index > index2) {
                ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
            } else {
                ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        }
        return ft;
    }


}