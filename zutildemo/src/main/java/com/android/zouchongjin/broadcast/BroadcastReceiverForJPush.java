package com.android.zouchongjin.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 接收到推送的自定义消息，则没有被处理
 * 2) 可以正常收到通知，用户点击打开应用主界面
 */
public class BroadcastReceiverForJPush extends BroadcastReceiver {
	
	private static final String TAG = "zouchongjin";

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        	
        }else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())){
        	
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {// 推送消息
        	Log.d(TAG, "消息标题--title--标题：" + bundle.getString(JPushInterface.EXTRA_TITLE));
        	Log.d(TAG, "消息内容--message--消息内容：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	Log.d(TAG, "附加字段--extras--自定义内容：" + bundle.getString(JPushInterface.EXTRA_EXTRA));
        	Log.d(TAG, "内容类型--content_type--：" + bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {// 推送通知
            Log.d(TAG, "通知的标题--n_title--通知标题：" + bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
            Log.d(TAG, "通知内容--n_content--通知内容：" + bundle.getString(JPushInterface.EXTRA_ALERT));
            Log.d(TAG, "附加字段--n_extras--自定义内容：" + bundle.getString(JPushInterface.EXTRA_EXTRA));
            Log.d(TAG, "通知栏的Notification ID，可以用于清除Notification：" + bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID));
            Log.d(TAG, "内容类型--content_type--：" + bundle.getInt(JPushInterface.EXTRA_CONTENT_TYPE));
            Log.d(TAG, "富媒体通知推送下载的HTML的文件路径,用于展现WebView：" + bundle.getInt(JPushInterface.EXTRA_RICHPUSH_HTML_PATH));
            Log.d(TAG, "富媒体通知推送下载的图片资源的文件名,多个文件名用 “，” 分开：" + bundle.getInt(JPushInterface.EXTRA_RICHPUSH_HTML_RES));
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {// 用户点击了通知
            Log.d(TAG, "通知的标题--n_title--通知标题：" + bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
            Log.d(TAG, "通知内容--n_content--通知内容：" + bundle.getString(JPushInterface.EXTRA_ALERT));
            Log.d(TAG, "附加字段--n_extras--自定义内容：" + bundle.getString(JPushInterface.EXTRA_EXTRA));
            Log.d(TAG, "通知栏的Notification ID，可以用于清除Notification：" + bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID));
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {

        } else {

        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
}

//	<receiver
//		android:name="com.zandroid.googlerun.broadcast.BroadcastReceiverForJPush"
//		android:enabled="true"><!-- 接收推送信息的广播 -->
//		<intent-filter>
//		    <action android:name="cn.jpush.android.intent.REGISTRATION" /> <!--Required  用户注册SDK的intent-->
//		    <action android:name="cn.jpush.android.intent.UNREGISTRATION" />  
//		    <action android:name="cn.jpush.android.intent.MESSAGE_RECEIVED" /> <!--Required  用户接收SDK消息的intent-->
//		    <action android:name="cn.jpush.android.intent.NOTIFICATION_RECEIVED" /> <!--Required  用户接收SDK通知栏信息的intent-->
//		    <action android:name="cn.jpush.android.intent.NOTIFICATION_OPENED" /> <!--Required  用户打开自定义通知栏的intent-->
//		    <action android:name="cn.jpush.android.intent.ACTION_RICHPUSH_CALLBACK" /> <!--Optional 用户接受Rich Push Javascript 回调函数的intent-->
//		    <category android:name="com.zandroid.googlerun" />
//		</intent-filter>
//	</receiver>


//<!-- 
//极光推送服务
//	1、注释掉的内容是其他功能已经加过的
//-->
//<!-- <permission android:name="com.zandroid.googlerun.permission.JPUSH_MESSAGE" android:protectionLevel="signature" /> -->
//<!-- 已经存在的权限 -->
//<!-- <uses-permission android:name="android.permission.INTERNET" /> -->
//<!-- <uses-permission android:name="android.permission.READ_PHONE_STATE" /> -->
//<!-- <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> -->
//<!-- <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" /> -->
//<!-- <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> -->
//<!-- <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" /> -->
//<!-- <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /> -->
//<!-- <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" /> -->
//<!-- <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /> -->
//<!-- 
//<uses-permission android:name="com.zandroid.googlerun.permission.JPUSH_MESSAGE" />
//<uses-permission android:name="android.permission.RECEIVE_USER_PRESENT" />
//<uses-permission android:name="android.permission.WAKE_LOCK" />
//<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
//<uses-permission android:name="android.permission.VIBRATE" />
//<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>  
//<uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS" />
//<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" /> -->


//<!-- 极光推送服务 -->
//<activity
//android:name="cn.jpush.android.ui.PushActivity"
//android:theme="@android:style/Theme.Translucent.NoTitleBar"
//android:configChanges="orientation|keyboardHidden" >
//<intent-filter>
//    <action android:name="cn.jpush.android.ui.PushActivity" />
//    <category android:name="android.intent.category.DEFAULT" />
//    <category android:name="com.zandroid.googlerun" />
//</intent-filter>
//</activity>
//<service
//	android:name="cn.jpush.android.service.DownloadService"
//	android:enabled="true"
//	android:exported="false" >
//</service>
//<service
//	android:name="cn.jpush.android.service.PushService"
//	android:enabled="true"
//	android:exported="false">
//<intent-filter>
//    <action android:name="cn.jpush.android.intent.REGISTER" />
//    <action android:name="cn.jpush.android.intent.REPORT" />
//    <action android:name="cn.jpush.android.intent.PushService" />
//    <action android:name="cn.jpush.android.intent.PUSH_TIME" />
//</intent-filter>
//</service>
//<receiver
//	android:name="cn.jpush.android.service.PushReceiver"
//	android:enabled="true" >
// <intent-filter android:priority="1000">
//    <action android:name="cn.jpush.android.intent.NOTIFICATION_RECEIVED_PROXY" />
//    <category android:name="com.zandroid.googlerun" />
//</intent-filter>
//<intent-filter>
//    <action android:name="android.intent.action.USER_PRESENT" />
//    <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
//</intent-filter>
//<intent-filter>
//    <action android:name="android.intent.action.PACKAGE_ADDED" />
//    <action android:name="android.intent.action.PACKAGE_REMOVED" />
//    <data android:scheme="package" />
//</intent-filter>
//</receiver>
//<receiver android:name="cn.jpush.android.service.AlarmReceiver" />
//<meta-data android:name="JPUSH_CHANNEL" android:value="developer-default"/>
//<meta-data android:name="JPUSH_APPKEY" android:value="b64c2d5ad620ba43b9b4a5dd" />