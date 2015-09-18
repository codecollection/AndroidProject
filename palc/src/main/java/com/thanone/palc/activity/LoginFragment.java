package com.thanone.palc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.palc.MyApplication;
import com.thanone.palc.R;
import com.thanone.palc.bean.Member;
import com.thanone.palc.util.HttpUrlUtil;
import com.thanone.palc.util.UiUtil;
import com.zcj.android.web.HttpCallback;
import com.zcj.android.web.HttpUtilsHandler;
import com.zcj.android.web.ServiceResult;

public class LoginFragment extends Fragment {

    private MyApplication application;
    private MainsActivity activity;

    @ViewInject(R.id.header_title)
    private TextView header_title;
    @ViewInject(R.id.header_back)
    private ImageView header_back;

    @ViewInject(R.id.login_username)
    private EditText login_username;
    @ViewInject(R.id.login_password)
    private EditText login_password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_login, container, false);
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
    public void onStart() {
        if (!isHidden() && application.getLoginUserId() != null) {
            activity.toFragment(R.id.main_footer_4);
        }
        super.onStart();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (application.getLoginUserId() == null) {
                header_title.setText("登录");
                header_back.setVisibility(View.GONE);
                login_username.setText("");
                login_password.setText("");
            } else {
                activity.toFragment(R.id.main_footer_4);
            }
        }
    }

    @OnClick(R.id.login_reg)
    private void login_reg(View v) {
        UiUtil.toReg(getActivity());
    }

    @OnClick(R.id.login_submit)
    private void login_submit(View v) {
        String username = login_username.getText().toString();
        String password = login_password.getText().toString();
        httpLogin(username, password, application.getPhoneId());
    }

    private void httpLogin(final String username, final String password, String phoneId) {
        HttpUtilsHandler.send(getActivity(), HttpUrlUtil.URL_LOGIN, HttpUrlUtil.url_login(username, password, phoneId), new HttpCallback() {
            @Override
            public void success(String dataJsonString) {
                Member loginUser = ServiceResult.GSON_DT.fromJson(dataJsonString, Member.class);
                if (loginUser != null) {
                    application.setLoginUser(loginUser);
                    application.setLoginUserId(loginUser.getId());
                    application.saveUserInfo(username, password);

                    activity.toFragment(R.id.main_footer_4);
                }
            }
        }, true);
    }

}
