package libarary.app.wcy.com.myapplication;

import android.os.Bundle;

import com.ruiyun.comm.library.ui.BaseActivity;

import org.wcy.android.view.bottomBar.BottomBarItem;
import org.wcy.android.view.bottomBar.BottomBarLayout;

import java.util.ArrayList;
import java.util.List;

import libarary.app.wcy.com.myapplication.fragments.HomeFragmentation;
import libarary.app.wcy.com.myapplication.fragments.MyFragmentation;
import libarary.app.wcy.com.myapplication.fragments.TwoFragmentation;
import me.yokeyword.fragmentation.SupportFragment;

public class MainTabActivity extends BaseActivity {
    private List<SupportFragment> mFragmentList = new ArrayList<>();
    private BottomBarLayout mBottomBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        mBottomBarLayout = findViewById(R.id.bottom_bar);
        mFragmentList.add(new HomeFragmentation());
        mFragmentList.add(new TwoFragmentation());
        mFragmentList.add(new MyFragmentation());
        //loadRootFragment(R.id.bottom_bar_delegate_container, new BottomFragmentation());
        final SupportFragment[] fragmentArray = mFragmentList.toArray(new SupportFragment[mFragmentList.size()]);
        loadMultipleRootFragment(R.id.bottom_bar_delegate_container, 0, fragmentArray);
        mBottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int previousPosition, int currentPosition) {
                showHideFragment(mFragmentList.get(currentPosition), mFragmentList.get(previousPosition));
            }
        });
        setSwipeBackEnable(false); // 是否允许滑动
    }

    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }
}
