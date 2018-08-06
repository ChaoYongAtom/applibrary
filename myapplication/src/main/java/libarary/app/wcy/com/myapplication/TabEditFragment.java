package libarary.app.wcy.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import libarary.app.wcy.com.myapplication.uibase.base.BaseFragment;


/**
 * @author wcy
 * @date 2017/6/23  11:22
 */
public class TabEditFragment extends BaseFragment {
    @BindView(R.id.btn_ok)
    Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        return setView(inflater, R.layout.fragment_tabedit);
    }

    public static TabEditFragment newInstance() {
        TabEditFragment newFragment = new TabEditFragment();
        return newFragment;
    }

    @Override
    protected void initView() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResult(0, null);
                pop();
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        toast("点击了返回");
        finishFramager();
        return true;
    }
}
