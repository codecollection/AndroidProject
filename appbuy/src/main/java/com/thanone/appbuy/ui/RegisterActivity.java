package com.thanone.appbuy.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.bean.User;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.thanone.appbuy.web.ServiceResult;
import com.thanone.appbuy.web.SrUserObj;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;
import com.zcj.android.util.UtilString;

@ContentView(R.layout.layout_register)
public class RegisterActivity extends BaseActivity {

	private AppContext appContext;// 全局Context
	
	@ViewInject(R.id.main_head_back)
	private ImageView main_head_back;
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;
	
	@ViewInject(R.id.register_userName)
	private EditText register_userName;
	@ViewInject(R.id.register_passWord)
	private EditText register_passWord;
	@ViewInject(R.id.register_passWord2)
	private EditText register_passWord2;
	@ViewInject(R.id.register_nickName)
	private EditText register_nickName;
	@ViewInject(R.id.register_mailBox)
	private EditText register_mailBox;
	@ViewInject(R.id.register_introduce)
	private EditText register_introduce;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);

		appContext = (AppContext) getApplication();
		
		main_head_title.setText("注册");
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
	
	@OnClick(R.id.register_submit)
	private void register_submit(View v) {
		final String userName = register_userName.getText().toString();
		final String passWord = register_passWord.getText().toString();
		String passWord2 = register_passWord2.getText().toString();
		String nickName = register_nickName.getText().toString();
		String mailBox = register_mailBox.getText().toString();
		String introduce = register_introduce.getText().toString();
		
		if (UtilString.isBlank(userName)) {
			UiUtil.showToast(this, "请输入账号");
			register_userName.requestFocus();
		} else if (UtilString.isBlank(passWord)) {
			UiUtil.showToast(this, "请输入新密码");
			register_passWord.requestFocus();
		} else if (passWord.length() < 8) {
			UiUtil.showToast(this, "密码最短8个字符");
			register_passWord.requestFocus();
		} else if (UtilString.isBlank(passWord2)) {
			UiUtil.showToast(this, "请输入确认新密码");
			register_passWord2.requestFocus();
		} else if (!passWord.equals(passWord2)) {
			UiUtil.showToast(this, "两次输入的密码不一致，请重新输入");
			register_passWord2.requestFocus();
		} else if (UtilString.isBlank(nickName)) {
			UiUtil.showToast(this, "请输入昵称");
			register_nickName.requestFocus();
		} else if (UtilString.isBlank(mailBox)) {
			UiUtil.showToast(this, "请输入邮箱");
			register_mailBox.requestFocus();
		} else {
			HttpUtilsHandler.send(this, HttpUrlUtil.URL_USER_REGISTER, 
					HttpUrlUtil.user_register(userName, passWord, nickName, mailBox, introduce),
					new HttpCallback() {
						@Override
						public void success(Object d, String result) {
							try {
								User obj = ServiceResult.GSON_DT.fromJson(result, SrUserObj.class).getD();
								appContext.login(userName, passWord, obj);
								UiUtil.showToast(RegisterActivity.this, "注册成功");
								finish();
							} catch (JsonSyntaxException e) {
								e.printStackTrace();
							}
						}
					}, true);
		}
	}
	
	@OnClick(R.id.main_head_back)
	private void main_head_back(View v) {
		finish();
	}
	
	@OnClick(R.id.register_syxy)
	private void register_syxy(View v) {
		UiUtil.toWebView(this, "代团服务使用协议", HttpUrlUtil.URL_WEB_DTFWSYXY);
	}
	
	@OnClick(R.id.register_ystk)
	private void register_ystk(View v) {
		UiUtil.toWebView(this, "隐私条款", HttpUrlUtil.URL_WEB_YSTK);
	}

}
