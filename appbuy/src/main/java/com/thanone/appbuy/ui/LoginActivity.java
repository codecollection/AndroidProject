package com.thanone.appbuy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.bean.User;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.common.UmengUtil;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.thanone.appbuy.web.ServiceResult;
import com.thanone.appbuy.web.SrUserObj;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;
import com.zcj.android.util.UtilString;

@ContentView(R.layout.layout_login)
public class LoginActivity extends BaseActivity {

	private AppContext appContext;// 全局Context
	
	private UmengUtil umeng;

	@ViewInject(R.id.login_username)
	private EditText login_username;
	@ViewInject(R.id.login_password)
	private EditText login_password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);

		appContext = (AppContext) getApplication();
		
		umeng = new UmengUtil(this);
		
		login_username.requestFocus();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (appContext.getLoginUser() != null) {
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		umeng.onActivityResult(requestCode, resultCode, data);
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

	@OnClick(R.id.login_register)
	private void login_register(View v) {
		UiUtil.toRegister(this);
	}

	@OnClick(R.id.login_back)
	private void login_back(View v) {
		finish();
	}

	@OnClick(R.id.login_forgetpassword)
	private void login_forgetpassword(View v) {
		UiUtil.toResetPassword(this);
	}

	@OnClick(R.id.login_renren)
	private void login_renren(View v) {
		umeng.loginByRenren();
	}
	
	@OnClick(R.id.login_weibo)
	private void login_weibo(View v) {
		umeng.loginBySina();
	}

	@OnClick(R.id.login_qq)
	private void login_qq(View v) {
		umeng.loginByQq();
	}

	@OnClick(R.id.login_logining)
	private void login_logining(View v) {
		final String username = login_username.getText().toString();
		final String password = login_password.getText().toString();
		if (UtilString.isBlank(username)) {
			UiUtil.showToast(this, "请输入账号");
			login_username.requestFocus();
		} else if (UtilString.isBlank(password)) {
			UiUtil.showToast(this, "请输入密码");
			login_password.requestFocus();
		} else {
			HttpUtilsHandler.send(this, HttpUrlUtil.URL_USER_LOGIN, HttpUrlUtil.user_login(username, password), new HttpCallback() {
				@Override
				public void success(Object d, String result) {
					try {
						User obj = ServiceResult.GSON_DT.fromJson(result, SrUserObj.class).getD();
						appContext.login(username, password, obj);
						UiUtil.showToast(LoginActivity.this, "登陆成功");
						finish();
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
					}
				}
			}, true);
		}
	}

}
