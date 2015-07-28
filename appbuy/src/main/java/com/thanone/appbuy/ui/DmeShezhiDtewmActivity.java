package com.thanone.appbuy.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.common.UiUtil;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;
import com.zcj.android.util.UtilImage;

@ContentView(R.layout.layout_dmeshezhidtewm)
public class DmeShezhiDtewmActivity extends BaseActivity {

	@ViewInject(R.id.main_head_back)
	private ImageView main_head_back;
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);

		main_head_title.setText("二维码");
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

	// TODO
//	@OnClick(R.id.dmeshezhidtewm_weixin)
//	private void dmeshezhidtewm_weixin(View v) {
//		UiUtil.openEwmOper(this, AppContext.FILESAVEPATH_IMG, UtilImage.getBitmapByResourceId(this, R.drawable.ewm_weixin));
//	}
	
	@OnClick(R.id.dmeshezhidtewm_weibo)
	private void dmeshezhidtewm_weibo(View v) {
		UiUtil.openEwmOper(this, AppContext.FILESAVEPATH_IMG, UtilImage.getBitmapByResourceId(this, R.drawable.ewm_weibo));
	}
	
	@OnClick(R.id.dmeshezhidtewm_wsq)
	private void dmeshezhidtewm_wsq(View v) {
		UiUtil.openEwmOper(this, AppContext.FILESAVEPATH_IMG, UtilImage.getBitmapByResourceId(this, R.drawable.ewm_wsq));
	}
	
	@OnClick(R.id.main_head_back)
	private void main_head_back(View v) {
		finish();
	}

}
