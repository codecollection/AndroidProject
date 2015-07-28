package com.thanone.appbuy.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.appbuy.R;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;
import com.zcj.android.util.UtilString;

@ContentView(R.layout.layout_resetpassword)
public class ResetPasswordActivity extends BaseActivity {

	@ViewInject(R.id.main_head_back)
	private ImageView main_head_back;
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;
	
	@ViewInject(R.id.resetpassword_userName)
	private EditText resetpassword_userName;
	@ViewInject(R.id.resetpassword_mailBox)
	private EditText resetpassword_mailBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);

		main_head_title.setText("重置密码");
		main_head_back.setVisibility(View.VISIBLE);
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
	
	@OnClick(R.id.resetpassword_submit)
	private void resetpassword_submit(View v) {
		String userName = resetpassword_userName.getText().toString();
		final String mailBox = resetpassword_mailBox.getText().toString();
		
		if (UtilString.isBlank(userName)) {
			UiUtil.showToast(this, "请输入账号");
			resetpassword_userName.requestFocus();
		} else if (UtilString.isBlank(mailBox)) {
			UiUtil.showToast(this, "请输入邮箱");
			resetpassword_mailBox.requestFocus();
		} else {
			HttpUtilsHandler.send(this, HttpUrlUtil.URL_USER_RESETPASSWORD, 
					HttpUrlUtil.user_resetpassword(userName, mailBox),
					new HttpCallback() {
						@Override
						public void success(Object d, String result) {
							UiUtil.showToast(ResetPasswordActivity.this, "重置的密码已发至您的邮箱："+mailBox+",请注意查收！");
							finish();
						}
					}, true);
		}
	}
	
	@OnClick(R.id.main_head_back)
	private void main_head_back(View v) {
		finish();
	}
	
}
