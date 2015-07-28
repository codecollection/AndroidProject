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

@ContentView(R.layout.layout_setnickname)
public class SetNickNameActivity extends BaseActivity {

	private AppContext appContext;// 全局Context
	private String usid;
	private Integer type;
	
	@ViewInject(R.id.main_head_back)
	private ImageView main_head_back;
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;
	
	@ViewInject(R.id.setnickname_nickName)
	private EditText setnickname_nickName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);

		appContext = (AppContext) getApplication();
		usid = getIntent().getExtras().getString("usid");
		type = getIntent().getExtras().getInt("type");
		
		main_head_title.setText("填写昵称");
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
	
	@OnClick(R.id.setnickname_submit)
	private void setnickname_submit(View v) {
		String nickName = setnickname_nickName.getText().toString();
		
		if (UtilString.isBlank(nickName)) {
			UiUtil.showToast(this, "请输入昵称");
			setnickname_nickName.requestFocus();
		} else {
			HttpUtilsHandler.send(this, HttpUrlUtil.URL_USER_CREGISTER, 
					HttpUrlUtil.user_cregister(nickName, usid, type),
					new HttpCallback() {
						@Override
						public void success(Object d, String result) {
							try {
								User obj = ServiceResult.GSON_DT.fromJson(result, SrUserObj.class).getD();
								appContext.login(obj.getUserName(), obj.getPassWord(), obj);
								UiUtil.showToast(SetNickNameActivity.this, "设置成功");
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
	
}
