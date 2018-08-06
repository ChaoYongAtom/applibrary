package libarary.app.wcy.com.myapplication.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruiyun.comm.library.ui.SwipeBackFragment;

import org.wcy.android.view.bottomBar.BottomBarItem;
import org.wcy.android.view.bottomBar.BottomBarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import libarary.app.wcy.com.myapplication.R;
import libarary.app.wcy.com.myapplication.uibase.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragmentation extends BaseFragment {
    private List<SwipeBackFragment> mFragmentList = new ArrayList<>();
    @BindView(R.id.bottom_bar)
    BottomBarLayout mBottomBarLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return setView(inflater, R.layout.fragment_home_fragmentation);
    }

    @Override
    protected void initView() {
        mFragmentList.add(new TwoFragmentation());
        mFragmentList.add(new MyFragmentation());
        //loadRootFragment(R.id.bottom_bar_delegate_container, new BottomFragmentation());
        final SwipeBackFragment[] fragmentArray = mFragmentList.toArray(new SwipeBackFragment[mFragmentList.size()]);
        loadMultipleRootFragment(R.id.bottom_fragment, 0, fragmentArray);
        mBottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int previousPosition, int currentPosition) {
                showHideFragment(mFragmentList.get(currentPosition), mFragmentList.get(previousPosition));
            }
        });
    }

}
