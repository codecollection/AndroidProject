package com.thanone.palc.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.thanone.palc.MyConfig;
import com.thanone.palc.service.UploadService;

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
					MyConfig.log("WIFI连接成功");
					context.startService(new Intent(context, UploadService.class));
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
}
