package com.zandroid.zway.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zandroid.zway.MyApplication;
import com.zandroid.zway.MyConfig;

public class BroadcastReceiverForNetChange extends BroadcastReceiver {

	private static boolean wifi = false;
	private static boolean gprs = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		MyConfig.log("BroadcastReceiverForNetChange--onReceive");

		startAll(context);

		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo activeNetInfo = manager.getActiveNetworkInfo();
			if (activeNetInfo != null) {
				if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI && !wifi) {
					wifi = true;
					gprs = false;
					MyConfig.log("WIFI连接成功");
					// 如果需要调用网络请求，建议新建开启一个服务，在服务里调用网络请求
					return;
				} else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE && !gprs) {
					wifi = false;
					gprs = true;
					MyConfig.log("GPRS连接成功");
					return;
				}
			}
		}
		wifi = false;
		gprs = false;
	}

	private void startAll(Context context) {
		MyApplication application = (MyApplication) context.getApplicationContext();
		if (application != null) {
			application.startAll();
		}
	}
	
}