package com.thanone.palc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.thanone.palc.MyApplication;
import com.thanone.palc.MyConfig;
import com.thanone.palc.R;

public class HouseFragment extends Fragment {

    private MyApplication application;
    private MainsActivity activity;

    @ViewInject(R.id.header_title)
    private TextView header_title;
    @ViewInject(R.id.header_back)
    private ImageView header_back;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MyConfig.log("HouseFragment onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyConfig.log("HouseFragment onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyConfig.log("HouseFragment onCreateView");
        View view = inflater.inflate(R.layout.layout_house, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        application = (MyApplication) getActivity().getApplication();
        activity = (MainsActivity) getActivity();

        header_title.setText("出租房登记");
        header_back.setVisibility(View.GONE);

        MyConfig.log("HouseFragment onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        MyConfig.log("HouseFragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        MyConfig.log("HouseFragment onResume");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            MyConfig.log("HouseFragment onHiddenChanged 隐藏了");
        } else {
            MyConfig.log("HouseFragment onHiddenChanged 显示了");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MyConfig.log("HouseFragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        MyConfig.log("HouseFragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MyConfig.log("HouseFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyConfig.log("HouseFragment onDestroy");
    }
}
