package com.android.zouchongjin.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.zouchongjin.MyConfig;

public class BroadcastReceiverForInstall extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if(Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())){
			if (("package:"+MyConfig.PACKAGE_NAME).equals(intent.getDataString())) {
				Log.v(MyConfig.TAG, "软件被覆盖安装");
				Intent mIntent = context.getPackageManager().getLaunchIntentForPackage(MyConfig.PACKAGE_NAME);
				if (mIntent != null) {
					context.startActivity(mIntent);
				}
			}
		} else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
			if (("package:"+MyConfig.PACKAGE_NAME).equals(intent.getDataString())) {
				Log.v(MyConfig.TAG, "软件被卸载");
			}
		}
	}
}

//	<receiver android:name="com.android.zouchongjin.broadcast.BroadcastReceiverForInstall" >
//		<intent-filter>
//		    <action android:name="android.intent.action.PACKAGE_ADDED" />
//		    <action android:name="android.intent.action.PACKAGE_REPLACED" />
//		    <action android:name="android.intent.action.PACKAGE_REMOVED" />
//		    <data android:scheme="package" /><!-- HERE! -->
//		</intent-filter>
//	</receiver>