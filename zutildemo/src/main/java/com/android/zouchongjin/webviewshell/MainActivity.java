package com.android.zouchongjin.webviewshell;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.zouchongjin.MyConfig;
import com.android.zouchongjin.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zcj.android.util.UtilAndroid;
import com.zcj.android.view.webviewshell.OnQuitListener;
import com.zcj.android.view.webviewshell.WebViewUtil;

@ContentView(R.layout.layout_webviewshell)
public class MainActivity extends Activity {

	@ViewInject(R.id.webview)
	private WebView webView;
	@ViewInject(R.id.webviewshell_error)
	private LinearLayout error;

	private WebViewUtil webViewUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		webViewUtil = new WebViewUtil(this, webView);

		initDate();
	}

	/** 处理返回事件 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Boolean result = webViewUtil.onKeyDown(keyCode, event, new OnQuitListener() {
			@Override
			public void webViewQuit() {
				allQuit();
			}
		});
		if (result != null) {
			return result;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@OnClick(R.id.webviewshell_error)
	private void reload(View v) {
		initDate();
	}

	private void initDate() {
		boolean connected = UtilAndroid.isNetworkConnected(this);
		if (connected) {
			hiddenError();
			// 初始化WebView内容
			webViewUtil.init(new MyJavascriptInterface(), "http://www.baidu.com/");
		} else {
			Toast.makeText(this, "网络无法连接", Toast.LENGTH_LONG).show();
			showError();
		}
	}

	// 注意：与JS代码绑定的的这个Java对象运行在另一个线程中，与创建它的线程不是一个线程。
	private class MyJavascriptInterface {

		/**
		 * 在JS里使用 Android.readInfo();调用。如果需要传参，则需要加上final关键字
		 * <p>
		 * 在JS里使用function AndroidReadInfoResult(v) {alert(v);}接收返回值
		 * <p>
		 * targetSdkVersion在17及以上，需添加@JavascriptInterface注解
		 */
		@JavascriptInterface
		public void readInfo() {
			Log.v(MyConfig.TAG, "调用Android方法");
			webView.post(new Runnable() {
				@Override
				public void run() {
					webView.loadUrl("javascript:AndroidReadInfoResult('" + 123123 + "')");
				}
			});
		}
	}

	private void showError() {
		webView.setVisibility(View.GONE);
		error.setVisibility(View.VISIBLE);
	}

	private void hiddenError() {
		webView.setVisibility(View.VISIBLE);
		error.setVisibility(View.GONE);
	}

	/** 退出所有 */
	private void allQuit() {
		System.exit(0);
	}

}

// res/layout/layout_webviewshwll.xml

// res/drawable-xhdpi/webviewshell_error.png
