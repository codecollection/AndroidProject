package com.android.zouchongjin.service.remote.server.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.android.zouchongjin.MyConfig;
import com.android.zouchongjin.MyServiceInterface;

/**
 * 客户端和服务端不在同一个APP
 * 
 * @author ZCJ
 * @data 2013-9-28
 */
public class MyService extends Service {

	private IBinder binder = new MyBinder();

	private final class MyBinder extends MyServiceInterface.Stub {
		public String query() throws RemoteException {
			return "zzz";
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		Log.i(MyConfig.TAG, "MyService service oncreate");
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(MyConfig.TAG, "MyService service onunbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		Log.i(MyConfig.TAG, "MyService service ondestory");
	}

}
