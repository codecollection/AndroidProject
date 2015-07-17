package com.zandroid.zloc.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zandroid.zloc.MyApplication;
import com.zandroid.zloc.MyConfig;

/**
 * 监听开机启动的广播
 * 
 * @author ZCJ
 * @data 2013-9-27
 */
public class BroadcastReceiverForBoot extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (MyConfig.DEBUG) {
			Log.v("z", "BroadcastReceiverForBoot--onReceive");
		}

		MyApplication application = (MyApplication) context.getApplicationContext();
		if (application != null) {
			application.init();
		}
		
	}

}
