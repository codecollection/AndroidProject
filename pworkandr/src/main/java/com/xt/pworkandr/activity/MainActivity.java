package com.xt.pworkandr.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.xt.pworkandr.R;
import com.xt.pworkandr.util.AndroidConfig;
import com.xt.pworkandr.util.BusinessException;
import com.xt.pworkandr.util.UpdateManager;
import com.xt.pworkandr.util.VPNSocket;
import com.xt.pworkandr.util.location.Location;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	
	
	// 开发环境
	public static final String serverUrl = "http://192.168.1.119:8080/pwork";
	private static final boolean debug = true;

	// 部署环境
//	public static final String serverUrl = "http://127.0.0.1:3306";
//	private static final boolean debug = false;
	
	private Intent vpnServiceIntent;
	private WebView myWebView;
	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;
	private Toast loadToast;
	private Timer myTimer = new Timer();
	private WakeLock wakeLock;
	
	/** 自动关闭程序任务 */
	private class MyTimeTask extends TimerTask {
		@Override
		public void run() {
			stopAll();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		
		try {
			this.initLoading();
			if (!debug) {this.initVpnServer();}
			this.initConfig();
			this.initWebView();
			this.initUpdateCheck();
		} catch (BusinessException e) {
			if (loadToast != null) {
				loadToast.cancel();
			}
			new AlertDialog.Builder(this).setMessage(e.getMessage()).setPositiveButton("关闭", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					stopAll();
				}
			}).create().show();
			return;
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if (myTimer != null) {			
			myTimer.cancel();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		myTimer = new Timer();
		myTimer.schedule(new MyTimeTask(), 1800000);
	}

	private void initLoading() {
		loadToast = Toast.makeText(this, "正在加载......", Toast.LENGTH_LONG);
		loadToast.setGravity(Gravity.CENTER, 0, 0);
		loadToast.show();
	}
	
	private void initVpnServer() throws BusinessException {
		String vpnResult = this.startVpnServer();
		if (vpnResult != null) {
			throw new BusinessException(vpnResult);
		}
	}
	
	private void initConfig() throws BusinessException {
		try {
			AndroidConfig.initData(serverUrl);
		} catch (Exception e) {
			throw new BusinessException("请求服务器失败");
		}
	}
	
	private void initUpdateCheck() {
		UpdateManager manager = new UpdateManager(this);
		manager.checkUpdate();
	}
	
	private void initWebView() {
		myWebView = (WebView) findViewById(R.id.webview);

		// 支持JS
		myWebView.getSettings().setJavaScriptEnabled(true);
		
		// 允许访问文件数据
		myWebView.getSettings().setAllowFileAccess(true);
		
		// 设置支持缩放
//		myWebView.getSettings().setBuiltInZoomControls(true);

		// 处理各种通知、请求事件(支持内部处理URL)
		myWebView.setWebViewClient(new MyWebViewClient());

		// 处理JS的对话框，网站图标，网站title，加载进度等
		myWebView.setWebChromeClient(new MyWebChromeClient());

		// 支持JS访问Android
		myWebView.addJavascriptInterface(new MyJavascriptInterface(this), "Android");

		// 调用外链接
		myWebView.loadUrl(AndroidConfig.getPath_url_base());
	}
	
	private void startLocation(String loginUserId) {
		acquireWakeLock();
		((Location) getApplication()).startLocation(loginUserId);
	}
	
	private void stopLocation() {
		((Location) getApplication()).stopLocation();
		releaseWakeLock();
	}
	
	// 申请电源锁(通常在Activity的 onResume中被调用)
	private void acquireWakeLock() {
		if (null == wakeLock) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, MainActivity.class.getName());
			if (wakeLock != null) {
				wakeLock.acquire();
			}
		}
	}

	// 释放电源锁(通常在Activity的 onPause中被调用)
	private void releaseWakeLock() {
		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock = null;
		}
	}
	
	private void stopVpn() {
		if (vpnServiceIntent != null) {
			stopService(vpnServiceIntent);
		}
	}
	
	private void stopAll() {
		this.stopLocation();
		this.stopVpn();
		finish();// 退出当前Activity，进程还存在
		Process.killProcess(Process.myPid());// 退出程序，进程也不存在，不推荐使用
	}

	private String startVpnServer() {
		byte[] vpnstate = new byte[100];
		int vpnstatelen = 0;

		// 安全客户端作为服务后台启动
		vpnServiceIntent = new Intent("com.xdja.safeclient.VpnService");
		// 安全网关地址和端口
		
		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
        String operator = telManager.getSimOperator(); 
        if (operator != null){
            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")){// 移动
            	vpnServiceIntent.putExtra("ServerIP", "10.120.138.98");
            } else if (operator.equals("46001")){// 联通
            	vpnServiceIntent.putExtra("ServerIP", "172.11.34.138");
            } else if (operator.equals("46003")){// 电信
            	Toast.makeText(this, "暂不支持中国电信SIM卡", Toast.LENGTH_SHORT).show();
            } else {
            	Toast.makeText(this, "不可识别的SIM卡"+operator, Toast.LENGTH_SHORT).show();
            }
        } else {
        	Toast.makeText(this, "读取SIM卡失败", Toast.LENGTH_SHORT).show();
        }
        
		vpnServiceIntent.putExtra("ServerPort", 3000);
		startService(vpnServiceIntent);

		// 连接安全客户端
		// VPNSocket类是一个简单的socket类
		VPNSocket vpnCS = new VPNSocket("127.0.0.1", 3001);
		int nres = vpnCS.connect();

		Long ctime = System.currentTimeMillis();
		while (nres != 0) {
			if (System.currentTimeMillis() - ctime > 20000) {
				return "连接vpn服务失败！";
			}
			nres = vpnCS.connect();
		}

		// 启动安全客户端
		vpnCS.sendData("VPNSTART"); // 发送启动命令
		vpnstatelen = vpnCS.recvData(vpnstate);

		// 通过判断返回的状态判断是否启动安全客户端成功
		String state = null;
		state = new String(vpnstate, 0, vpnstatelen);

		if (!state.startsWith("OK")) {
			return "vpn连接启动失败！";
		}

		// 循环发送getstatus命令检测安全通道是否已经建立，如果出错或者超//时将返回对应错误
		ctime = System.currentTimeMillis();
		while (!state.startsWith("OK 100")) {
			if (state.startsWith("FAILED")) {
				String[] temp = state.split(" ");
				System.out.println(temp[1]);
				return "vpn认证失败";
			}

			if (System.currentTimeMillis() - ctime > 30000) {
				return "vpn连接建立超时";
			}
			vpnCS.sendData("GETSTATUS");
			vpnstatelen = vpnCS.recvData(vpnstate);
			state = new String(vpnstate, 0, vpnstatelen);
		}
		return null;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }
	
	/** 创建菜单 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/** 绑定菜单事件 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menuitem1) {
			this.stopAll();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 处理手机返回按钮
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (myWebView.canGoBack()) {
				myWebView.goBack();
				return true;
			} else {
				// 弹出框
				AlertDialog ad = new AlertDialog.Builder(this)
					.setMessage("确定退出吗")
					.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							stopAll();
						}
					})
					.setNegativeButton("返回", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 不做任何操作
						}
					})
					.create();
				ad.show();
                return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressWarnings("unused")
	private class MyJavascriptInterface {
		Context mContext;

		MyJavascriptInterface(Context c) {
			mContext = c;
		}

		/** 在JS里使用 Android.location("123"); 调用 */
		// targetSdkVersion在17及以上，需添加@JavascriptInterface
		public void locationBegin(final String userId) {
			startLocation(userId);
		}
		public void locationBegin() {
			stopLocation();
		}
	}
	
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@SuppressWarnings("unused")
	private class MyWebChromeClient extends WebChromeClient {
		
		// For 3.0-
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
		}
		
		// For Android 3.0+
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
			openFileChooser(uploadMsg);
		}

		// For Android 4.1+
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			openFileChooser(uploadMsg);
		}
		
		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
			builder.setTitle("提示").setMessage(message).setPositiveButton("确定", null);
			builder.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					return true;
				}
			});
			builder.setCancelable(false);
			AlertDialog dialog = builder.create();
			dialog.show();
			result.confirm();
			return true;
		}
		
		@Override
		public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
			builder.setTitle("提示").setMessage(message).setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					result.confirm();
				}
			}).setNeutralButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					result.cancel();
				}
			});
			builder.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					result.cancel();
				}
			});

			builder.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					return true;
				}
			});
			builder.setCancelable(false);
			AlertDialog dialog = builder.create();
			dialog.show();
			return true;
		}
		
		@Override
		public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
			builder.setTitle("提示").setMessage(message);
			final EditText et = new EditText(view.getContext());
			et.setSingleLine();
			et.setText(defaultValue);
			builder.setView(et);
			builder.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					result.confirm(et.getText().toString());
				}
			}).setNeutralButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					result.cancel();
				}
			});

			// 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
			builder.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					return true;
				}
			});

			// 禁止响应按back键的事件
			builder.setCancelable(false);
			AlertDialog dialog = builder.create();
			dialog.show();
			return true;
		}
	}

}
