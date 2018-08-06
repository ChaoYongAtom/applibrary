package com.ruiyun.comm.library.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ruiyun.comm.library.api.entitys.BaseResult;
import com.ruiyun.comm.library.mvp.BaseView;

import org.wcy.android.R;
import org.wcy.android.retrofit.exception.ApiException;
import org.wcy.android.utils.RxActivityTool;
import org.wcy.android.utils.RxDataTool;
import org.wcy.android.utils.RxKeyboardTool;
import org.wcy.android.utils.StatusBarUtil;
import org.wcy.android.view.HeaderLayout;
import org.wcy.android.view.toast.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

/**
 * 公共baseactivity
 * Created by wcy on 2018/1/18.
 */

public abstract class BaseActivity extends SwipeBackActivity implements BaseView {
    Unbinder unbinder;
    private HeaderLayout headerLayout;
    @Override
    public boolean swipeBackPriority() {
        return super.swipeBackPriority();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxActivityTool.setScreenVertical(this);//竖屏显示
        finishInputWindow();//隐藏输入法
        RxActivityTool.addActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        init();
    }

    @Override
    public void setContentView(View view) {
        // TODO Auto-generated method stub
        super.setContentView(view);
        init();
    }


    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
          finishActivity();
        }
    }
    /**
     * 限制SwipeBack的条件,默认栈内Fragment数 <= 1时 , 优先滑动退出Activity , 而不是Fragment
     *
     * @return true: Activity优先滑动退出;  false: Fragment优先滑动退出
     */
    public void setView(int layoutID, String title) {
        setContentView(layoutID);
        headerLayout = findViewById(R.id.headerlayout);
        if (headerLayout != null) {
            if (!RxDataTool.isNullString(title)) headerLayout.setTitleText(title);
            headerLayout.getNavigationView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishActivity();
                }
            });
        }
        init();
    }

    private void init() {
        setStatusBar();
        unbinder = ButterKnife.bind(this);
    }


    /**
     * 关闭当前activity
     */
    public void finishActivity() {
        finishInputWindow();
        RxActivityTool.finishActivity(this);
    }

    /**
     * 关闭软键盘
     **/
    public void finishInputWindow() {
        RxKeyboardTool.hideSoftInput(this);
    }


    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white40), 40);
    }


    @Override
    public void onNext(BaseResult result) {

    }

    @Override
    public void onError(ApiException e, String mothead) {
        toastError(e.getDisplayMessage());
    }

    private void showToast(Object obj, int type) {
        if (obj != null && !RxDataTool.isEmpty(obj)) {
            ToastUtils.show(this, obj.toString(), type);
        } else {
            ToastUtils.show(this, "数据异常", ToastUtils.ERROR_TYPE);
        }
    }
    //设置所有Fragment的转场动画
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }
    public void toastError(Object obj) {
        showToast(obj, ToastUtils.ERROR_TYPE);
    }

    public void toastSuccess(Object obj) {
        showToast(obj, ToastUtils.SUCCESS_TYPE);
    }

    public void toast(Object obj) {
        showToast(obj, ToastUtils.NO);
    }

    @Override
    public Context getThisContext() {
        return this;
    }


    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }

    public HeaderLayout getHeaderLayout() {
        return headerLayout;
    }
}
