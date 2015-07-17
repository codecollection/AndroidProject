package com.zandroid.zloc.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zandroid.zloc.MyApplication;
import com.zandroid.zloc.MyConfig;

public class BroadcastReceiverForNetChange extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (MyConfig.DEBUG) {
			Log.v("z", "BroadcastReceiverForNetChange--onReceive");
		}

		MyApplication application = (MyApplication) context.getApplicationContext();
		if (application != null) {
			application.init();
		}
	}

}
