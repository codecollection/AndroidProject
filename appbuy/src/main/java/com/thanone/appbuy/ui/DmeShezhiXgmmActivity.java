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
import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;
import com.zcj.android.util.UtilDialog;
import com.zcj.android.util.UtilDialog.DialogCallback;
import com.zcj.android.util.UtilString;

@ContentView(R.layout.layout_dmeshezhixgmm)
public class DmeShezhiXgmmActivity extends BaseActivity {

	private AppContext appContext;// 全局Context
	
	@ViewInject(R.id.main_head_back)
	private ImageView main_head_back;
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;
	
	@ViewInject(R.id.dmeshezhixgmm_old)
	private EditText dmeshezhixgmm_old;
	@ViewInject(R.id.dmeshezhixgmm_new)
	private EditText dmeshezhixgmm_new;
	@ViewInject(R.id.dmeshezhixgmm_new2)
	private EditText dmeshezhixgmm_new2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);

		appContext = (AppContext) getApplication();
		
		main_head_title.setText("修改密码");
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
	
	@OnClick(R.id.dmeshezhixgmm_submit)
	private void dmeshezhixgmm_submit(View v) {
		String old = dmeshezhixgmm_old.getText().toString();
		final String new1 = dmeshezhixgmm_new.getText().toString();
		String new2 = dmeshezhixgmm_new2.getText().toString();
		if (UtilString.isBlank(old)) {
			UiUtil.showToast(this, "请输入旧密码");
			dmeshezhixgmm_old.requestFocus();
		} else if (UtilString.isBlank(new1)) {
			UiUtil.showToast(this, "请输入新密码");
			dmeshezhixgmm_new.requestFocus();
		} else if (new1.length() < 8) {
			UiUtil.showToast(this, "密码最短8个字符");
			dmeshezhixgmm_new.requestFocus();
		} else if (UtilString.isBlank(new2)) {
			UiUtil.showToast(this, "请输入确认新密码");
			dmeshezhixgmm_new2.requestFocus();
		} else if (!new1.equals(new2)) {
			UiUtil.showToast(this, "两次输入的密码不一致，请重新输入");
			dmeshezhixgmm_new2.requestFocus();
		} else {
			HttpUtilsHandler.send(this, HttpUrlUtil.URL_USER_UPDATEPASSWORD, HttpUrlUtil.user_updatepassword(old, new1, appContext.getLoginUserID()), 
					new HttpCallback() {
						@Override
						public void success(Object d, String result) {
							// 更新配置里的密码
							appContext.updatepassword(new1);
							// 密码修改成功
							UtilDialog.builderAlertDialog(DmeShezhiXgmmActivity.this, null, "密码修改成功", new DialogCallback() {
								@Override
								public void doSomething_ChickOK() {
									finish();
								}
							});
						}
					}, true);
		}
	}
	
	@OnClick(R.id.main_head_back)
	private void main_head_back(View v) {
		finish();
	}

}
