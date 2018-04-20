package org.wcy.android.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.wcy.android.R;

import java.util.List;

public class RxFragmentUtil {

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
