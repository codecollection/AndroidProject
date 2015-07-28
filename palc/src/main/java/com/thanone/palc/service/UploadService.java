package com.thanone.palc.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.thanone.palc.MyApplication;
import com.thanone.palc.MyConfig;

public class UploadService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		MyConfig.log("UploadService--onCreate");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		MyConfig.log("UploadService--onStartCommand");

		MyApplication app = (MyApplication) getApplication();
		app.uploadInfoToWeb(null, null, null, null, null, null);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		MyConfig.log("UploadService--onDestroy");
		super.onDestroy();
	}

}
