package libarary.app.wcy.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.wcy.android.adapter.CommonRecyclerAdapter;
import org.wcy.android.adapter.ViewRecyclerHolder;
import org.wcy.android.view.EmptyLayout;
import org.wcy.android.view.HeaderLayout;
import org.wcy.android.view.ImageZoomPopupWindow;
import org.wcy.android.view.refresh.MaterialRefreshLayout;
import org.wcy.android.view.refresh.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wcy
 * @date 2017/6/23  11:22
 */
public class TabEditFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_tabedit, null);
        return view;
    }

}
