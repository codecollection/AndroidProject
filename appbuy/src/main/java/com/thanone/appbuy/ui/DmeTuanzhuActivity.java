package com.thanone.appbuy.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnFocusChange;
import com.lidroid.xutils.view.annotation.event.OnKey;
import com.tencent.connect.auth.QQAuth;
import com.tencent.wpa.WPA;
import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.bean.User;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.common.UmengUtil;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;
import com.zcj.android.util.UtilIntent;
import com.zcj.android.util.UtilString;

@ContentView(R.layout.layout_dmetuanzhu)
public class DmeTuanzhuActivity extends BaseActivity {

	private AppContext appContext;// 全局Context
	
	@ViewInject(R.id.main_head_back)
	private ImageView main_head_back;
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;

	@ViewInject(R.id.dmetuanzhu_qq1)
	private TextView dmetuanzhu_qq1;
	@ViewInject(R.id.dmetuanzhu_qq2)
	private TextView dmetuanzhu_qq2;
	
	// 编辑器
	@ViewInject(R.id.project_comment)
	private LinearLayout project_comment;
	@ViewInject(R.id.project_editer)
	private EditText project_editer;
	@ViewInject(R.id.project_editer_submit)
	private Button project_editer_submit;
	private InputMethodManager imm;
	private Long targetUserID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);
		
		appContext = (AppContext) getApplication();
		targetUserID = User.USERID_ADMIN;
		
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		
		main_head_title.setText("团主");
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

	@OnClick(R.id.main_head_back)
	private void main_head_back(View v) {
		finish();
	}
	
	@OnClick(R.id.dmetuanzhu_weibo)
	private void dmetuanzhu_weibo(View v) {
		UtilIntent.startWeb(this, HttpUrlUtil.URL_WEIBO);
	}

	@OnClick(R.id.dmetuanzhu_qq1)
	private void dmetuanzhu_qq1(View v) {
		String qq = dmetuanzhu_qq1.getText().toString();
		QQAuth mqqAuth = QQAuth.createInstance(UmengUtil.QQ_APPID, this);
		WPA mWPA = new WPA(this, mqqAuth.getQQToken());
		int ret = mWPA.startWPAConversation(qq, "");
		if (ret != 0) {
			UiUtil.showToast(this, "抱歉，联系客服出现了错误~. error:" + ret);
		}
	}
	
	@OnClick(R.id.dmetuanzhu_qq2)
	private void dmetuanzhu_qq2(View v) {
		String qq = dmetuanzhu_qq2.getText().toString();
		QQAuth mqqAuth = QQAuth.createInstance(UmengUtil.QQ_APPID, this);
		WPA mWPA = new WPA(this, mqqAuth.getQQToken());
		int ret = mWPA.startWPAConversation(qq, "");
		if (ret != 0) {
			UiUtil.showToast(this, "抱歉，联系客服出现了错误~. error:" + ret);
		}
	}
	
	@OnClick(R.id.dmetuanzhu_fssx)
	private void dmetuanzhu_fssx(View v) {
		openToComment(targetUserID, "团主");
	}
	
	@OnClick(R.id.dmetuanzhu_ljdt)
	private void dmetuanzhu_ljdt(View v) {
		UiUtil.toWebView(this, "了解代团", HttpUrlUtil.URL_WEB_GYDT);
	}
	
	// --------------------------------------------评论----------------------------------------------
	private void openToComment(Long targetUserID, String targetUserNickname) {
		if (appContext.getLoginUser() == null) {
			UiUtil.toLogin(this);
			return;
		}
		if (targetUserID == null) {
			project_editer.setHint("");
			this.targetUserID = null;
		} else {
			project_editer.setHint("回复" + targetUserNickname);
			this.targetUserID = targetUserID;
		}
		openEditor(project_editer);
	}

	@OnKey(R.id.project_editer)
	private boolean editerOnKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			project_editer.clearFocus();
			return true;
		}
		return false;
	}

	@OnClick(R.id.project_editer_submit)
	private void editerSubmit(View v) {
		final String content = project_editer.getText().toString();
		if (UtilString.isBlank(content)) {
			UiUtil.showToast(this, "请输入评论内容");
			openEditor(project_editer);
		} else {
			HttpUtilsHandler.send(this, HttpUrlUtil.URL_COMMENT_ADDCOMMENT,
					HttpUrlUtil.comment_addcomment(appContext.getLoginUserID(), targetUserID, content), new HttpCallback() {
						@Override
						public void success(Object d, String result) {
							project_editer.clearFocus();
							UiUtil.showToast(DmeTuanzhuActivity.this, "发送成功");
						}
						@Override
						public void error() {
							project_editer.clearFocus();
						}
					}, true);
		}
	}

	private void openEditor(EditText v) {
		v.requestFocus();
		v.requestFocusFromTouch();
		project_comment.setVisibility(View.VISIBLE);
		imm.showSoftInput(v, 0);
	}
	@OnFocusChange(R.id.project_editer)
	private void editerOnFocus(View v, boolean hasFocus) {
		if (!hasFocus) {
			hideEditor(project_editer);
		}
	}
	private void hideEditor(EditText v) {
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			v.setText("");
			project_comment.setVisibility(View.GONE);
		}
	}
	// --------------------------------------------------------------------------------------------------

}
