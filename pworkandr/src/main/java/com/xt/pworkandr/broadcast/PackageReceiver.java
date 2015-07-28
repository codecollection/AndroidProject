package com.xt.pworkandr.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xt.pworkandr.util.DataCleanManager;

public class PackageReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if(Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction()) || Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())){
			if ("package:com.xt.pworkandr".equals(intent.getDataString())) {
				Intent mIntent = context.getPackageManager().getLaunchIntentForPackage("com.xt.pworkandr");
				if (mIntent != null) {
					context.startActivity(mIntent);
				}
			}
		} else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
			if ("package:com.xt.pworkandr".equals(intent.getDataString())) {
				Log.v("zouchongjin", "开始清理旧数据");
				DataCleanManager.clean(context, true, true, true, true, true);
				Log.v("zouchongjin", "清理旧数据完成");
			}
		}
	}
}