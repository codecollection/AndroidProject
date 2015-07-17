package com.android.zouchongjin.service;

import com.android.zouchongjin.MyConfig;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 基本的Bind服务
 * <p>
 * 1、调用者和服务必须是“同应用”和“同线程”。
 * <p>
 * 2、多客户端可以绑定同一个服务。当所有的客户端解绑之后，此服务自动销毁。
 * 
 * @author ZCJ
 * @data 2013-3-11
 */
public class MyBindService extends Service {

	private final IBinder mBinder = new MyBinder();

	public class MyBinder extends Binder {
		MyBindService getService() {
			return MyBindService.this;
		}
	}

	@Override
	public void onCreate() {
		Log.i(MyConfig.TAG, "MyBindService oncreate");
	}

	// 只在第一次绑定时调用。系统将同一个IBinder对象传递给后续绑定的客户
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(MyConfig.TAG, "MyBindService onbind");
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(MyConfig.TAG, "MyBindService onunbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		Log.i(MyConfig.TAG, "MyBindService ondestory");
	}

	public String query() {
		return "zzz";
	}

}
