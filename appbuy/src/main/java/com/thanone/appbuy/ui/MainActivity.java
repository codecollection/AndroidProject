package com.thanone.appbuy.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.common.FinalUtil;
import com.thanone.appbuy.common.UiUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.zcj.android.app.AppManager;
import com.zcj.android.app.DoubleClickExitHelper;
import com.zcj.android.util.UtilAndroid;
import com.zcj.android.util.UtilSharedPreferences;

@ContentView(R.layout.main)
public class MainActivity extends FragmentActivity {

	private AppContext appContext;// 全局Context
	private Timer unreadTimer;

	private Fragment[] mFragments;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	@ViewInject(R.id.main_radiogroup)
	private RadioGroup main_radiogroup;
	@ViewInject(R.id.main_footer_ashow)
	private RadioButton main_footer_ashow;
	@ViewInject(R.id.main_footer_bshow)
	private RadioButton main_footer_bshow;
	@ViewInject(R.id.main_footer_ctopic)
	private RadioButton main_footer_ctopic;
	@ViewInject(R.id.main_footer_dme)
	private RadioButton main_footer_dme;

	private DoubleClickExitHelper mDoubleClickExitHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UtilSharedPreferences.save(this, FinalUtil.XML_NAME, FinalUtil.XML_FIRST, "111");

		AppManager.getAppManager().addActivity(this);

		ViewUtils.inject(this);

		mDoubleClickExitHelper = new DoubleClickExitHelper(this);

		appContext = (AppContext) getApplication();

		mFragments = new Fragment[4];
		fragmentManager = getSupportFragmentManager();
		mFragments[0] = fragmentManager.findFragmentById(R.id.main_fragment_1);
		mFragments[1] = fragmentManager.findFragmentById(R.id.main_fragment_2);
		mFragments[2] = fragmentManager.findFragmentById(R.id.main_fragment_3);
		mFragments[3] = fragmentManager.findFragmentById(R.id.main_fragment_4);

		setRadioGroupChangeListener();
		main_radiogroup.check(R.id.main_footer_ashow);

		if (!UtilAndroid.isNetworkConnected(appContext)) {// 网络连不上
			UiUtil.showNetworkFailureToast(MainActivity.this);
		}

		unreadTimer = new Timer();
		unreadTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				if (appContext.getUnread() != null && appContext.getUnread().booleanValue()) {
					message.what = 1;
				} else {
					message.what = 0;
				}
				handler.sendMessage(message);
			}
		}, 100, 1000);

		// 检测更新
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
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

	private void setRadioGroupChangeListener() {
		main_radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2])
						.hide(mFragments[3]);
				switch (checkedId) {
				case R.id.main_footer_ashow:
					fragmentTransaction.show(mFragments[0]).commit();
					break;
				case R.id.main_footer_bshow:
					fragmentTransaction.show(mFragments[1]).commit();
					break;
				case R.id.main_footer_ctopic:
					fragmentTransaction.show(mFragments[2]).commit();
					break;
				case R.id.main_footer_dme:
					fragmentTransaction.show(mFragments[3]).commit();
					break;
				default:
					break;
				}
			}
		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				main_footer_ctopic.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.d_widget_bar_ctopic_unread, 0, 0);
				break;
			case 0:
				main_footer_ctopic.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.d_widget_bar_ctopic, 0, 0);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 监听返回--是否退出程序
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean flag = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return mDoubleClickExitHelper.onKeyDown(keyCode, event);
		} else {
			flag = super.onKeyDown(keyCode, event);
		}
		return flag;
	}

}
