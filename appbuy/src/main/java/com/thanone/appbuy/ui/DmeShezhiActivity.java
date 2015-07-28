package com.thanone.appbuy.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.zcj.android.app.BaseActivity;
import com.zcj.android.util.UtilDialog;
import com.zcj.android.util.UtilIntent;

@ContentView(R.layout.layout_dmeshezhi)
public class DmeShezhiActivity extends BaseActivity {

	private AppContext appContext;// 全局Context
	
	@ViewInject(R.id.main_head_back)
	private ImageView main_head_back;
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;
	
	@ViewInject(R.id.dmeshezhi_xgmm)
	private LinearLayout dmeshezhi_xgmm;
	@ViewInject(R.id.dmeshezhi_tcdl)
	private TextView dmeshezhi_tcdl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);
		
		appContext = (AppContext) getApplication();

		main_head_title.setText("设置");
		main_head_back.setVisibility(View.VISIBLE);
		checkLogin();
	}

	@Override
	protected void onStart() {
		checkLogin();
		super.onStart();
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
	
	private void checkLogin() {
		if (appContext.getLoginUser() != null) {
			dmeshezhi_xgmm.setVisibility(View.VISIBLE);
			dmeshezhi_tcdl.setVisibility(View.VISIBLE);
		} else {
			dmeshezhi_xgmm.setVisibility(View.GONE);
			dmeshezhi_tcdl.setVisibility(View.GONE);
		}
	}

	@OnClick(R.id.main_head_back)
	private void main_head_back(View v) {
		finish();
	}

	@OnClick(R.id.dmeshezhi_xgmm)
	private void dmeshezhi_xgmm(View v) {
		UiUtil.toDmeshezhiXgmm(this);
	}

	@OnClick(R.id.dmeshezhi_dtewm)
	private void dmeshezhi_dtewm(View v) {
		UiUtil.toDmeshezhiDtewm(this);
	}

	@OnClick(R.id.dmeshezhi_jcxbb)
	private void dmeshezhi_jcxbb(View v) {
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
		    @Override
		    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
		        switch (updateStatus) {
		        case UpdateStatus.Yes:
		            UmengUpdateAgent.showUpdateDialog(DmeShezhiActivity.this, updateInfo);
		            break;
		        case UpdateStatus.No: // has no update
		        	UiUtil.showToast(DmeShezhiActivity.this, "没有检测到新版本");
		            break;
		        case UpdateStatus.NoneWifi: // none wifi
		        	UmengUpdateAgent.showUpdateDialog(DmeShezhiActivity.this, updateInfo);
		            break;
		        case UpdateStatus.Timeout: // time out
		        	UiUtil.showToast(DmeShezhiActivity.this, "请求超时");
		            break;
		        }
		    }
		});
		UmengUpdateAgent.update(this);
	}

	@OnClick(R.id.dmeshezhi_gydt)
	private void dmeshezhi_gydt(View v) {
		UiUtil.toDmeshezhiGydt(this);
	}
	
	@OnClick(R.id.dmeshezhi_wypf)
	private void dmeshezhi_wypf(View v) {
		UtilIntent.startWeb(this, HttpUrlUtil.URL_QQAPP);
	}

	@OnClick(R.id.dmeshezhi_tcdl)
	private void dmeshezhi_tcdl(View v) {
		UtilDialog.builderAlertDialog2(this, "提示", "确定退出登录？", new UtilDialog.DialogCallback() {
			@Override
			public void doSomething_ChickOK() {
				appContext.logout();
				finish();
			}
		});
	}

}
