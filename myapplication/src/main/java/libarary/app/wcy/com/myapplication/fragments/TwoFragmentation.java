package libarary.app.wcy.com.myapplication.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import libarary.app.wcy.com.myapplication.R;
import libarary.app.wcy.com.myapplication.uibase.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragmentation extends BaseFragment {


    public TwoFragmentation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setView(inflater, R.layout.fragment_two_fragmentation);
    }

    @Override
    protected void initView() {

    }

}
