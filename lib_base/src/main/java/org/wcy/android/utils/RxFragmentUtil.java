package org.wcy.android.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.wcy.android.R;
import org.wcy.android.ui.CommonActivity;

import java.util.List;

public class RxFragmentUtil {
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
     * 切换tab
     *
     * @param idx
     */
    @Deprecated
    public static int showTab(int frameId, int idx, int currentTab, List<? extends Fragment> fragments, AppCompatActivity activity) {
        return showTab(frameId, idx, currentTab, fragments, activity.getSupportFragmentManager(), true);
    }
    @Deprecated
    public static int showTab(int frameId, int idx, int currentTab, List<? extends Fragment> fragments, FragmentManager fragmentManager) {
        return showTab(frameId, idx, currentTab, fragments, fragmentManager, true);
    }

    /**
     * 切换tab
     *
     * @param idx
     */
    @Deprecated
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
    @Deprecated
    public static FragmentTransaction obtainFragmentTransaction(int index, int index2, FragmentManager fragmentManager) {
        return obtainFragmentTransaction(index, index2, fragmentManager, true);
    }
    @Deprecated
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
