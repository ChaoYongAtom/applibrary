package libarary.app.wcy.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wcy.android.view.EmptyLayout;
import org.wcy.android.view.HeaderLayout;

/**
 * @author wcy
 * @date 2017/6/23  11:22
 */
public class TabFragment extends Fragment {
    public static final String CONTENT = "content";
    HeaderLayout headerLayout;
    EmptyLayout emptyLayout;
    /**
     * The loading state
     */
    private final int TYPE_LOADING = 2;

    int type = TYPE_LOADING;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.tabfragment, null);
        headerLayout = view.findViewById(R.id.headerlayout);
        emptyLayout = view.findViewById(R.id.emptylayout);
        emptyLayout.setErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        emptyLayout.showEmpty();
        headerLayout.getMenuView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type > 5) {
                    type = 1;
                } else {
                    type++;
                }
                if (type == 1) {
                    emptyLayout.showEmpty();
                } else if (type == 2) {
                    emptyLayout.showLoading();
                } else if (type == 3) {
                    emptyLayout.showError();
                } else if (type == 4) {
                    emptyLayout.showView();
                } else if (type == 5) {
                    emptyLayout.showNet();
                }
            }
        });
        return view;
    }


}
