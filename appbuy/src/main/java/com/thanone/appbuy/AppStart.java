package com.thanone.appbuy;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.thanone.appbuy.common.FinalUtil;
import com.thanone.appbuy.common.UiUtil;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.util.UtilSharedPreferences;
import com.zcj.android.util.UtilString;

@ContentView(R.layout.start)
public class AppStart extends Activity {

	@ViewInject(R.id.start_main)
	private LinearLayout start_main;
	@ViewInject(R.id.start_left)
	private ImageView start_left;
	@ViewInject(R.id.start_right)
	private ImageView start_right;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);

		// 禁止默认的页面统计方式
		MobclickAgent.openActivityDurationTrack(false);

		Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_left_in);
		start_left.startAnimation(animation);

		Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.translate_right_in);
		animation2.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				String first = UtilSharedPreferences.get(AppStart.this, FinalUtil.XML_NAME, FinalUtil.XML_FIRST);
				if (UtilString.isBlank(first)) {
					UiUtil.toWelcome(AppStart.this);
				} else {
					UiUtil.toMain(AppStart.this);
				}
				finish();
			}
		});
		start_right.startAnimation(animation2);

	}

}