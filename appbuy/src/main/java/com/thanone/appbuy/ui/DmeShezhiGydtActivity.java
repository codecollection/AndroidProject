package com.thanone.appbuy.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.appbuy.R;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;

@ContentView(R.layout.layout_dmeshezhigydt)
public class DmeShezhiGydtActivity extends BaseActivity {

	@ViewInject(R.id.main_head_back)
	private ImageView main_head_back;
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;

	@ViewInject(R.id.dmeshezhigydt_version)
	private TextView dmeshezhigydt_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);

		main_head_title.setText("关于代团");
		main_head_back.setVisibility(View.VISIBLE);

		// 获取客户端版本信息
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
			dmeshezhigydt_version.setText("版本：" + info.versionName);
		} catch (NameNotFoundException e) {
			dmeshezhigydt_version.setText("版本：未知");
		}
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

	@OnClick(R.id.main_head_back)
	private void main_head_back(View v) {
		finish();
	}

}
