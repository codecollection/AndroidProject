package com.android.zouchongjin.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.android.zouchongjin.MyConfig;
import com.android.zouchongjin.R;

/**
 * 启动一个新的线程一个个处理请求，防止主线程阻塞
 * <p>
 * 	1、额外创建一个固定的新线程，新线程中创建工作队列，一个一个处理被启动的此服务，所有服务处理完之后自动stopSelf()
 * 	2、如果被多次调用，则需要等待上次请求处理完毕后才开始处理新的请求（单个新线程）
 * @author zouchongjin@sina.com
 * @data 2015年6月17日
 */
public class MyIntentService extends IntentService {

	public MyIntentService() {
		super("MyIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(MyConfig.TAG, "当前运行的线程ID：" + Thread.currentThread().getId());
		Log.i(MyConfig.TAG, "开始下载....");
		showNotification(false);
		try {
			Thread.sleep(10000);
			showNotification(true);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(MyConfig.TAG, "程序下载完毕");
	}

	@SuppressWarnings("deprecation")
	private void showNotification(boolean finish) {
		
		Notification notification;
		PendingIntent contentIntent = PendingIntent.getActivity(this, 1234, new Intent(this, FirstActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		if (!finish) {
			notification = new Notification(R.drawable.ic_launcher, "开始下载", System.currentTimeMillis());
			notification.setLatestEventInfo(this, "下载", "正在下载中", contentIntent);
		} else {
			notification = new Notification(R.drawable.ic_launcher, "下载完毕", System.currentTimeMillis());
			notification.setLatestEventInfo(this, "下载", "程序下载完毕", contentIntent);
		}
		notification.defaults = Notification.DEFAULT_ALL;// 定义通知的提醒方式（DEFAULT_ALL:所有;DEFAULT_LIGHTS:闪光提示;DEFAULT_SOUNDS:声音提示;DEFAULT_VIBRATE:震动提示,需要加<uses-permission android:name="android.permission.VIBRATE"/>）
		notification.flags = Notification.FLAG_AUTO_CANCEL;// 通知被点击后自动取消

		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		manager.notify(123, notification);

	}
}
