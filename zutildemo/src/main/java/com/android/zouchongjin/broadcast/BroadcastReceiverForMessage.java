package com.android.zouchongjin.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import com.android.zouchongjin.MyConfig;
import com.zcj.android.util.bean.MessageBean;
import com.zcj.android.web.ServiceResult;

/**
 * 短信的广播接收者
 * 
 * @author ZCJ
 * @data 2013-3-20
 */
public class BroadcastReceiverForMessage extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
			Log.v(MyConfig.TAG, "发现有新短信");
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for (Object p : pdus) {
				byte[] pdu = (byte[]) p;
				
				SmsMessage message = SmsMessage.createFromPdu(pdu);
				
				MessageBean messageBean = MessageBean.getMessageBeanBySmsMessage(message);
				
				Log.v(MyConfig.TAG, ServiceResult.GSON_DT.toJson(messageBean));
				
//				abortBroadcast();// 终止此短信的广播
			}
		}
	}

}

//	<uses-permission android:name="android.permission.RECEIVE_SMS"/><!-- 接收短信权限 -->

//	<receiver android:name="com.zandroid.googlerun.broadcast.BroadcastReceiverForMessage">
//		<intent-filter android:priority="1000"><!-- 优先于系统短信应用 -->
//		    <action android:name="android.provider.Telephony.SMS_RECEIVED"/><!-- 收到短信广播 -->
//		</intent-filter>
//	</receiver>
