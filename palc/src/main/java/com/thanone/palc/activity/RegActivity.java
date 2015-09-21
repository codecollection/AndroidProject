package com.thanone.palc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.palc.MyApplication;
import com.thanone.palc.R;
import com.thanone.palc.bean.Member;
import com.thanone.palc.util.HttpUrlUtil;
import com.thanone.palc.util.UiUtil;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;
import com.zcj.android.util.UtilDialog;
import com.zcj.android.web.HttpCallback;
import com.zcj.android.web.HttpUtilsHandler;
import com.zcj.android.web.ServiceResult;
import com.zcj.util.UtilString;

@ContentView(R.layout.layout_reg)
public class RegActivity extends BaseActivity {

    private MyApplication application;

    @ViewInject(R.id.header_title)
    private TextView header_title;
    @ViewInject(R.id.header_back)
    private ImageView header_back;

    @ViewInject(R.id.reg_idcard)
    private EditText reg_idcard;
    @ViewInject(R.id.reg_tel)
    private EditText reg_tel;
    @ViewInject(R.id.reg_password)
    private EditText reg_password;
    @ViewInject(R.id.reg_password2)
    private EditText reg_password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        application = (MyApplication) getApplication();

        header_title.setText("注册");
        header_back.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick(R.id.reg_submit)
    private void submit(View v) {
        String idcard = reg_idcard.getText().toString();
        String tel = reg_tel.getText().toString();
        String password = reg_password.getText().toString();
        String password2 = reg_password2.getText().toString();

        if (UtilString.isBlank(idcard)) {
            UiUtil.alert(this, "请输入身份证号码");
            reg_idcard.requestFocus();
        } else if (UtilString.isBlank(tel)) {
            UiUtil.alert(this, "请输入手机号码");
            reg_tel.requestFocus();
        } else if (UtilString.isBlank(password)) {
            UiUtil.alert(this, "请输入登录密码");
            reg_password.requestFocus();
        } else if (UtilString.isBlank(password2)) {
            UiUtil.alert(this, "请输入确认密码");
            reg_password2.requestFocus();
        } else if (!password.equals(password2)) {
            UiUtil.alert(this, "两次输入的密码不一致");
            reg_password.requestFocus();
        } else {
            httpReg(tel, password, idcard, application.getPhoneId());
        }
    }

    private void httpReg(final String username, final String password, String idcard, String phoneId) {
        HttpUtilsHandler.send(this, HttpUrlUtil.URL_REG, HttpUrlUtil.url_reg(username, password, idcard, phoneId), new HttpCallback() {
            @Override
            public void success(String dataJsonString) {
                Member loginUser = ServiceResult.GSON_DT.fromJson(dataJsonString, Member.class);
                if (loginUser != null) {
                    application.setLoginUser(loginUser);
                    application.setLoginUserId(loginUser.getId());
                    application.saveUserInfo(username, password);
                    UtilDialog.builderAlertDialog(RegActivity.this, null, "注册成功", new UtilDialog.DialogCallback() {
                        @Override
                        public void doSomething_ChickOK() {
                            finish();
                        }
                    });
                }
            }
        }, true);
    }

    @OnClick(R.id.header_back)
    private void back(View v) {
        finish();
    }

}
