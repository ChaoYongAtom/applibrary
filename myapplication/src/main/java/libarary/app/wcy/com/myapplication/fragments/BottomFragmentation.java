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
import me.yokeyword.fragmentation.SupportFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class BottomFragmentation extends BaseFragment {
    private List<SwipeBackFragment> mFragmentList = new ArrayList<>();
    @BindView(R.id.bottom_bar)
    BottomBarLayout mBottomBarLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return setView(inflater, R.layout.fragment_bottom_fragmentation);
    }

    @Override
    public int setCreatedLayoutViewId() {
        return 0;
    }

    @Override
    public String setTitle() {
        return null;
    }

    @Override
    protected void initView() {
        mFragmentList.add(new HomeFragmentation());
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

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        toast("asdfasdf");
    }



}
