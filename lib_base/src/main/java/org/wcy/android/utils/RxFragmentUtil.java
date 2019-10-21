package org.wcy.android.utils;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.wcy.android.R;

import java.util.List;

public class RxFragmentUtil {

    /**
     * 切换tab
     *
     * @param idx
     */
    public static int showTab(int frameId, int idx, int currentTab, List<? extends Fragment> fragments, AppCompatActivity activity) {
        return showTab(frameId, idx, currentTab, fragments, activity.getSupportFragmentManager(), true);
    }

    public static int showTab(int frameId, int idx, int currentTab, List<? extends Fragment> fragments, FragmentManager fragmentManager) {
        return showTab(frameId, idx, currentTab, fragments, fragmentManager, true);
    }

    /**
     * 切换tab
     *
     * @param idx
     */
    public static int showTab(int frameId, int idx, int currentTab, List<? extends Fragment> fragments, FragmentManager fragmentManager, boolean isShoWanimation) {
        if (idx != currentTab) {
            if (idx == -1) {
                FragmentTransaction ftshow = fragmentManager.beginTransaction();
                ftshow.add(frameId, fragments.get(0));
                ftshow.commitAllowingStateLoss();
            } else {
                Fragment cufragment = fragments.get(currentTab);
                cufragment.onPause(); // 暂停当前tab
                FragmentTransaction fthide = obtainFragmentTransaction(idx, currentTab, fragmentManager, isShoWanimation);
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
    public static FragmentTransaction obtainFragmentTransaction(int index, int index2, FragmentManager fragmentManager) {
        return obtainFragmentTransaction(index, index2, fragmentManager, true);
    }

    public static FragmentTransaction obtainFragmentTransaction(int index, int index2, FragmentManager fragmentManager, boolean isShoWanimation) {
        // FragmentTransaction ft = appCompatActivity.getSupportFragmentManager().beginTransaction();
        FragmentTransaction ft = fragmentManager.beginTransaction();
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
