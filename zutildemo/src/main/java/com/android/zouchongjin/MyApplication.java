package com.android.zouchongjin;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.android.zouchongjin.service.PhoneService;
import com.lidroid.xutils.DbUtils;

public class MyApplication extends Application {
	
	/** 数据库操作对象 */
	private DbUtils dbUtils;
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.dbUtils = DbUtils.create(this);
		initAll();
	}
	
	public void initAll() {
		// initJpush();
		// initPhoneService();
	}

	@SuppressWarnings("unused")
	private void initJpush() {
		Log.v(MyConfig.TAG, "极光推送服务即将开启");
		JPushInterface.init(this); // 初始化 JPush
		Log.v(MyConfig.TAG, "极光推送服务启动成功");
	}
	
	@SuppressWarnings("unused")
	private void initPhoneService() {
		Log.v(MyConfig.TAG, "电话录音服务即将开启");
		startService(new Intent(this, PhoneService.class));
		Log.v(MyConfig.TAG, "电话录音服务启动成功");
	}

	public DbUtils getDbUtils() {
		return dbUtils;
	}

	public void setDbUtils(DbUtils dbUtils) {
		this.dbUtils = dbUtils;
	}
	
}