package com.android.zouchongjin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.zouchongjin.MyConfig;

/**
 * 基本的服务
 * <p>
 * 	1、服务运行于主线程中，如果服务执行任何cpu耗时操作或异步操作 (像MP3播放或联网)，你应该创建一个新线程执行这类操作
 * 	2、服务的停止：服务工作完成后，应该调用stopSelf()自己关闭自己,或者另外一个组件调用stopService()函数停止该服务
 * 
 * @author zouchongjin@sina.com
 * @data 2015年6月17日
 */
public class MyService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(MyConfig.TAG, "MyService onCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		final String myname = intent.getStringExtra("myname");

		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 50; i++) {
					try {
						Log.i(MyConfig.TAG, myname + i);
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();

//		return START_NOT_STICKY;// 服务停止后，不自动重启。这是最安全的选项，用来避免在不需要的时候运行你的服务。
//		return START_STICKY;// 服务停止后，自动onCreate并调用onStartCommand，但是intent会丢失。适合多媒体播放。
//		return START_REDELIVER_INTENT;// 服务停止后，使用最后传递给service的intent，自动onCreate并调用onStartCommand。任何未被处理的意图会接着循环处理。适合下载。
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.i(MyConfig.TAG, "MyService onDestroy");
	}

}
