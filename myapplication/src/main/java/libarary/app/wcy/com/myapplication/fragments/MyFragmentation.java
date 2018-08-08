package libarary.app.wcy.com.myapplication.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import libarary.app.wcy.com.myapplication.R;
import libarary.app.wcy.com.myapplication.uibase.base.BaseFragment;

public class MyFragmentation extends BaseFragment {
    public MyFragmentation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return setView(inflater,R.layout.fragment_my_fragmentation);
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

    }

}
