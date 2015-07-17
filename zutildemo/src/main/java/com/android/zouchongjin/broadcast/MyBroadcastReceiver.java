package com.android.zouchongjin.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 接收自定义的广播
 * 
 * @author ZCJ
 * @data 2013-3-20
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("com.android.demo.HiMessage")) {
			Toast.makeText(context, "广播接收的数据："+intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
		}
	}
	
}


//	AndroidManifest.xml
//	<receiver android:name=".broadcast.MyBroadcastReceiver">
//		<intent-filter>
//		    <action android:name="com.android.demo.HiMessage"/><!-- 拦截自定义的广播 -->
//		</intent-filter>
//	</receiver>


//	Activity.java(发起广播代码)
//	Intent intent = new Intent();
//	intent.setAction("com.android.demo.HiMessage");
//	intent.putExtra("message", "这是传给你的东西");
//	sendBroadcast(intent);

//发送有序广播：sendOrderedBroadcast()
//前面的接收者有权终止广播：BroadcaseReceiver.abortBroadcast()
//前面的接收者可以将数据存放在结果对象中：setResultExtras(Bundle)。下一个接收者可以获取前面接收者存入的数据：Bundle bundle = getResultExtras(true)
//优先级别在<intent-filter>的android:priority属性中声明，数值越大优先级别越高（-1000~1000）
