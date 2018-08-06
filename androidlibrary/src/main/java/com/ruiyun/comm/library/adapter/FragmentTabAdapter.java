package com.ruiyun.comm.library.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.ruiyun.comm.library.utils.TurnFragmentUtil;

import java.util.List;

/**
 * Author: wcy
 */
public class FragmentTabAdapter implements RadioGroup.OnCheckedChangeListener {
    private List<Fragment> fragments; // 一个tab页面对应一个Fragment
    private RadioGroup rgs; // 用于切换tab
    private FragmentManager mfragmentManager; // Fragment所属的Activity
    private int fragmentContentId; // Activity中所要被替换的区域的id
    private int currentTab = 0; // 当前Tab页面索引
    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 用于让调用者在切换tab时候增加新的功能
    private boolean isShoWanimation = true;

    public FragmentTabAdapter(FragmentManager fragmentManager, List<Fragment> fragments, int
            fragmentContentId, RadioGroup rgs) {
        init(fragmentManager, fragments, fragmentContentId, rgs);
    }

    public FragmentTabAdapter(AppCompatActivity appCompatActivity, List<Fragment> fragments, int
            fragmentContentId, RadioGroup rgs) {
        init(appCompatActivity.getSupportFragmentManager(), fragments, fragmentContentId, rgs);
    }

    public FragmentTabAdapter(FragmentManager fragmentManager, List<Fragment> fragments, int
            fragmentContentId, RadioGroup rgs, int currentTab) {
        this.currentTab = currentTab;
        init(fragmentManager, fragments, fragmentContentId, rgs);
    }

    public FragmentTabAdapter(AppCompatActivity appCompatActivity, List<Fragment> fragments, int
            fragmentContentId, RadioGroup rgs, int currentTab) {
        this.currentTab = currentTab;
        init(appCompatActivity.getSupportFragmentManager(), fragments, fragmentContentId, rgs);
    }

    private void init(FragmentManager fragmentManager, List<Fragment> fragments, int fragmentContentId,
                      RadioGroup rgs) {
        this.fragments = fragments;
        this.rgs = rgs;
        this.mfragmentManager = fragmentManager;
        this.fragmentContentId = fragmentContentId;
        currentTab = TurnFragmentUtil.showTab(fragmentContentId, -1, currentTab, fragments, fragmentManager, isShoWanimation);
        rgs.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (onRgsExtraCheckedChangedListener != null) {
            onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(radioGroup, checkedId, currentTab);
        }
        for (int i = 0; i < rgs.getChildCount(); i++) {
            if (rgs.getChildAt(i).getId() == checkedId) {
                currentTab = TurnFragmentUtil.showTab(fragmentContentId, i, currentTab, fragments, mfragmentManager, isShoWanimation);
            }
        }
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }

    public void setOnRgsExtraCheckedChangedListener(OnRgsExtraCheckedChangedListener
                                                            onRgsExtraCheckedChangedListener) {
        this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
    }

    /**
     * 切换tab额外功能功能接口
     */
    public interface OnRgsExtraCheckedChangedListener {
        public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index);
    }

    public void setShoWanimation(boolean shoWanimation) {
        isShoWanimation = shoWanimation;
    }
}