package com.android.zouchongjin.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.zouchongjin.MyConfig;

public class BroadcastReceiverForNetChange extends BroadcastReceiver {

	private static boolean wifi = false;
	private static boolean gprs = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo activeNetInfo = manager.getActiveNetworkInfo();
			if (activeNetInfo != null) {
				if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI && !wifi) {
					wifi = true;
					gprs = false;
					Log.v(MyConfig.TAG, "WIFI连接成功");
					// 如果需要调用网络请求，建议新建开启一个服务，在服务里调用网络请求
					return;
				} else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE && !gprs) {
					wifi = false;
					gprs = true;
					Log.v(MyConfig.TAG, "GPRS连接成功");
					return;
				}
			}
		}
		wifi = false;
		gprs = false;
	}
	
}

//	<receiver android:name="com.zandroid.googlerun.broadcast.BroadcastReceiverForNetChange">
//		<intent-filter>
//			<action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
//		</intent-filter>
//	</receiver>
