package com.thanone.appbuy.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.appbuy.R;
import com.thanone.appbuy.common.FinalUtil;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;

@ContentView(R.layout.layout_webview)
public class WebViewActivity extends BaseActivity {

	@ViewInject(R.id.main_head_back)
	private ImageView main_head_back;
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;
	@ViewInject(R.id.webview_daituan)
	private WebView webview_daituan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewUtils.inject(this);
		
		main_head_title.setText(getIntent().getExtras().getString(FinalUtil.INTENT_EXTRAS_TITLE));
		main_head_back.setVisibility(View.VISIBLE);
		
		webview_daituan.loadUrl(getIntent().getExtras().getString(FinalUtil.INTENT_EXTRAS_URL));
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
