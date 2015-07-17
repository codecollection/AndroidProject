package com.android.zouchongjin.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.zouchongjin.MyConfig;
import com.android.zouchongjin.service.PhoneService;

/**
 * 拨打电话的广播接收者
 * 
 * @author ZCJ
 * @data 2013-3-20
 */
public class BroadcastReceiverForPhone extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
			
			String number = getResultData();
			
			Log.v(MyConfig.TAG, "拨打了电话"+number);
			
			PhoneService.outCallPhoneNumber = number;
			
//			setResultData(null);// 阻止拨打电话
//			setResultData("12593"+number);// 修改拨打的电话号码
		}
	}
	
}

//	<uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/><!-- 拨打电话权限 -->

//	<receiver android:name="com.zandroid.googlerun.broadcast.BroadcastReceiverForPhone">
//		<intent-filter android:priority="1000"><!-- 优先于系统打电话应用 -->
//		    <action android:name="android.intent.action.NEW_OUTGOING_CALL"/><!-- 拨打电话广播 -->
//		</intent-filter>
//	</receiver>
