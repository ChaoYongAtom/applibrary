package libarary.app.wcy.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.wcy.android.adapter.CommonRecyclerAdapter;
import org.wcy.android.adapter.ViewRecyclerHolder;
import org.wcy.android.view.EmptyLayout;
import org.wcy.android.view.HeaderLayout;
import org.wcy.android.view.dialog.RxDialogSure;
import org.wcy.android.view.dialog.RxDialogSureCancel;
import org.wcy.android.view.refresh.MaterialRefreshLayout;
import org.wcy.android.view.refresh.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wcy
 * @date 2017/6/23  11:22
 */
public class TabFragment extends Fragment {
    public static final String CONTENT = "content";
    HeaderLayout headerLayout;
    TextView textmsg;
    EmptyLayout emptyLayout;
    /**
     * The loading state
     */
    private final int TYPE_LOADING = 2;

    int type = TYPE_LOADING;
    CommonRecyclerAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.tabfragment, null);
//        textmsg = view.findViewById(R.id.textmsg);
//        textmsg.setText(getArguments().getString(TabFragment.CONTENT));
        headerLayout = view.findViewById(R.id.headerlayout);
        emptyLayout = view.findViewById(R.id.emptylayout);
        emptyLayout.setErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        emptyLayout.showView();
        headerLayout.getMenuView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext());//提示弹窗
                rxDialogSureCancel.show();
            }
        });

        headerLayout.getMenu2View().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("数据" + i);
        }
        adapter = new CommonRecyclerAdapter<String>(getContext(), list, R.layout.layout_iteam) {
            @Override
            public void convert(ViewRecyclerHolder helper, String item) {
                helper.setText(R.id.tv_msg, item);
            }
        };
        emptyLayout.setAdapter(adapter);
        emptyLayout.getPullToRefreshView().setMore(MaterialRefreshLayout.Mode.BOTH);
        emptyLayout.setOnRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "onRefresh", Toast.LENGTH_LONG).show();
                emptyLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emptyLayout.onRefreshComplete();

                    }
                }, 3000);
            }

            @Override
            public void onRefreshLoadMore() {
                Toast.makeText(getContext(), "LoadMore", Toast.LENGTH_LONG).show();
                emptyLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emptyLayout.onRefreshComplete();
                    }
                }, 3000);
            }
        });
    }
}
