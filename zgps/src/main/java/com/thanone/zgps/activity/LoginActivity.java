package com.thanone.zgps.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.zgps.MyApplication;
import com.thanone.zgps.MyConfig;
import com.thanone.zgps.R;
import com.thanone.zgps.bean.User;
import com.thanone.zgps.util.HttpUrlUtil;
import com.thanone.zgps.util.UiUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.zcj.android.util.UtilSharedPreferences;
import com.zcj.android.web.HttpCallback;
import com.zcj.android.web.HttpUtilsHandler;
import com.zcj.android.web.ServiceResult;
import com.zcj.util.UtilString;

@ContentView(R.layout.layout_login)
public class LoginActivity extends Activity {

	private MyApplication application;

	@ViewInject(R.id.login_input_username)
	private EditText login_input_username;

	@ViewInject(R.id.login_input_password)
	private EditText login_input_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyConfig.log("LoginActivity--onCreate");
		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);

		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);

		application = ((MyApplication) getApplication());
	}

	@Override
	protected void onStart() {
		MyConfig.log("LoginActivity--onStart");
		super.onStart();
		initLoginInfo();
		UiUtil.gpsCheck(this);
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

	/** 自动输入账号和密码 */
	private void initLoginInfo() {
		String username = UtilSharedPreferences.get(this, MyApplication.XML_NAME, MyApplication.XML_U);
		String password = UtilSharedPreferences.get(this, MyApplication.XML_NAME, MyApplication.XML_P);
		if (UtilString.isNotBlank(username)) {
			login_input_username.setText(username);
		} else {
			login_input_username.setText("");
		}
		if (UtilString.isNotBlank(password)) {
			login_input_password.setText(password);
		} else {
			login_input_password.setText("");
		}
	}

	@OnClick(R.id.login_button_login)
	private void main_button_login(View view) {
		if (UiUtil.gpsCheck(this)) {
			final String username = login_input_username.getText().toString();
			final String password = login_input_password.getText().toString();
			if (UtilString.isBlank(username)) {
				UiUtil.showToast(this, "请输入账号");
				login_input_username.requestFocus();
			} else if (UtilString.isBlank(password)) {
				UiUtil.showToast(this, "请输入密码");
				login_input_password.requestFocus();
			} else {
				HttpUtilsHandler.send(this, HttpUrlUtil.URL_USER_LOGIN,
						HttpUrlUtil.user_login(username, password, application.getPhoneId()), new HttpCallback() {
							@Override
							public void success(String result) {
								try {
									User user = ServiceResult.GSON_DT.fromJson(result, User.class);
									application.login(user);
									User theUser = application.getLoginUser();
									if (theUser != null && User.ROLE_ADMIN.equals(theUser.getRole())) {// 管理员
										UiUtil.toMap(LoginActivity.this, UiUtil.MAP_TYPE_ALL, null);
									} else {// 普通用户
										UiUtil.toMap(LoginActivity.this, UiUtil.MAP_TYPE_ONE, user.getId());
									}
								} catch (Exception e) {
									e.printStackTrace();
									UiUtil.showToast(LoginActivity.this, "用户数据解析出错");
								}
							}
						}, true);
			}
		}
	}

}
