package libarary.app.wcy.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.wcy.android.adapter.CommonRecyclerAdapter;
import org.wcy.android.adapter.ViewRecyclerHolder;
import org.wcy.android.view.EmptyLayout;
import org.wcy.android.view.HeaderLayout;
import org.wcy.android.view.refresh.MaterialRefreshLayout;
import org.wcy.android.view.refresh.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

import libarary.app.wcy.com.myapplication.uibase.base.BaseFragment;

/**
 * @author wcy
 * @date 2017/6/23  11:22
 */
public class TabFragment extends BaseFragment {
    public static final String CONTENT = "content";
    HeaderLayout headerLayout;
    TextView textmsg;
    MyViewAndCircle circle;
    /**
     * The loading state
     */
    private final int TYPE_LOADING = 2;

    int type = TYPE_LOADING;
    CommonRecyclerAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        rootView = setView(inflater, R.layout.tabfragment, null);
        headerLayout = rootView.findViewById(R.id.headerlayout);
        circle = rootView.findViewById(R.id.circle);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public int setCreatedLayoutViewId() {
        return 0;
    }

    @Override
    public String setTitle() {
        return "dasasdf";
    }

    protected void initView() {
        circle.setTitleText("adsfadsf");
        circle.setIsVisiable(true);
        headerLayout.setmMenuOneText("历史数据");
//        headerLayout.getMenuOneView().setBackgroundResource(R.color.warning_stroke_color);
        headerLayout.getMenuOneView().setTipVisibility(true);
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            list.add("数据" + i);
//        }
    }
}
