package com.thanone.palc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.palc.MyApplication;
import com.thanone.palc.R;

public class UserinfoFragment extends Fragment {

    private MyApplication application;
    private MainsActivity activity;

    @ViewInject(R.id.header_title)
    private TextView header_title;
    @ViewInject(R.id.header_back)
    private ImageView header_back;

    @ViewInject(R.id.userinfo_phone)
    private TextView userinfo_phone;
    @ViewInject(R.id.userinfo_regtime)
    private TextView userinfo_regtime;
    @ViewInject(R.id.userinfo_logintime)
    private TextView userinfo_logintime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_userinfo, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        application = (MyApplication) getActivity().getApplication();
        activity = (MainsActivity) getActivity();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (application.getLoginUserId() == null) {
                activity.toFragment(R.id.main_footer_4);
            } else {
                header_title.setText("用户信息");
                header_back.setVisibility(View.GONE);
                initDate();
            }
        }
    }

    private void initDate() {
        // TODO
        userinfo_phone.setText("手机号：13888888888");
        userinfo_regtime.setText("注册时间：2015-07-01 16:00:00");
        userinfo_logintime.setText("最后登录时间：2015-09-01 16:00:00");
    }

    @OnClick(R.id.userinfo_logout)
    private void userinfo_logout(View v) {
        application.logout();
        activity.toFragment(R.id.main_footer_4);
    }

}
