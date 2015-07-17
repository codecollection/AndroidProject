package com.zandroid.zloc.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.zandroid.zloc.MyApplication;
import com.zandroid.zloc.MyConfig;
import com.zandroid.zloc.bean.Location;
import com.zandroid.zloc.util.HttpCallback;
import com.zandroid.zloc.util.LbsSaveHelp;
import com.zcj.android.util.UtilAndroid;

import java.util.List;

/**
 * 服务：启动定位服务
 * 
 * 		默认首先使用GPS定位，然后是WIFI定位，最后基站定位。并且，百度地图定位SDK会根据环境自动调整定位策略。
 * 
 * 		有的移动设备锁屏后为了省电会自动关闭网络连接，此时网络定位模式的定位失效。
 * 		此外，锁屏后移动设备若进入cpu休眠，定时定位功能也失效。
 * 		若您需要实现在cpu休眠状态仍需定时定位，可以用alarmManager 实现1个cpu可叫醒的timer，定时请求定位。
 * 
 * @author ZCJ
 * @data 2013-9-28
 */
public class LocationService extends Service {

	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	private boolean isOpenLocation = false;
	private WakeLock wakeLock;
	
	private String successTime = "";// 成功获取位置信息的时间，如果第二次获取的位置不变，则时间会是上次获取位置的时间
	private String phoneId;
	private DbUtils dbUtils;
	
	// 初始化主线程的Handler.
	@SuppressLint("HandlerLeak")
	Handler mainHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			/* 
				61 ： GPS定位结果
				62 ： 扫描整合定位依据失败。此时定位结果无效。
				63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
				65 ： 定位缓存的结果。
				66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
				67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
				68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
				161： 表示网络定位结果
				162~167： 服务端定位失败。
			*/
			int code = msg.getData().getInt("code");
			if (code == 61 || code == 65 || code == 66 || code == 68 || code == 161) {
				if (MyConfig.DEBUG) {					
					Log.v(MyConfig.TAG, "定位成功：" + code + ":" + msg.getData().getString("latitude") + ":" + msg.getData().getString("longitude") + ":" + msg.getData().getString("address") + ":" + msg.getData().getString("time"));
				}
				setLocationOption(MyConfig.SCANSPAN_UPDATE);
				saveData(msg.getData().getString("time"), msg.getData().getString("latitude"), msg.getData().getString("longitude"), msg.getData().getString("address"));
			} else {
				if (MyConfig.DEBUG) {
					Log.v(MyConfig.TAG, "定位失败：" + code);
				}
				mLocationClient.requestLocation();
			}
			super.handleMessage(msg);
		}
	};
	
	private void saveData(String time, String latitude, String longitude, String address) {
		// 如果位置没有变
		if (time != null && time.trim() != "" && successTime.equals(time)) {
			return;
		}
		
		try {
			// 保存到本地数据库
			Location loc = new Location(time, latitude, longitude, address, phoneId);
			dbUtils.saveBindingId(loc);
			successTime = time;
			
			// 上传数据，包括以前的数据
			if (UtilAndroid.isNetworkConnected(this)) {
				List<Location> locationList = dbUtils.findAll(Location.class);
				if (locationList != null && locationList.size() > 0) {
					for (Location l : locationList) {
						final Long id = l.getId();
						if (MyConfig.DEBUG) {							
							Log.v(MyConfig.TAG, "准备上传id为"+id+"的定位数据！");
						}
						LbsSaveHelp.poiCreate(l.getLongitude(), l.getLatitude(), l.getAddress(), l.getTime() + "-" + android.os.Build.MODEL, phoneId, new HttpCallback() {
							@Override
							public void success(String resultJson) {
								try {
									dbUtils.deleteById(Location.class, id);
								} catch (DbException e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		
	}
	
	// 新建消息
	Message newMessage(int code, double latitude, double longitude, String address, String time) {
		Message message = new Message();
		Bundle data = new Bundle();
		data.putInt("code", code);
		data.putString("latitude", String.valueOf(latitude));
		data.putString("longitude", String.valueOf(longitude));
		data.putString("address", address);
		data.putString("time", time);
		message.setData(data);
		return message;
	}
	
	private void startLocation() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(this);// 须在主线程中声明
			mLocationClient.setAK(MyConfig.LOCATION_AK);
			mLocationClient.registerLocationListener(myListener);
			setLocationOption(MyConfig.SCANSPAN_BEGIN);
		}
		if (mLocationClient != null && !isOpenLocation) {
			mLocationClient.start();
			isOpenLocation = true;
		}
	}

	@SuppressWarnings("unused")
	private void stopLocation() {
		if (isOpenLocation) {
			if (mLocationClient != null && mLocationClient.isStarted()) {				
				mLocationClient.stop();
			}
			isOpenLocation = false;
		}
	}

	private void setLocationOption(int scanspan) {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(MyConfig.OPEN_GPS);
		option.setAddrType("all");// all表示返回的定位结果包含地址信息，其他值都表示不返回地址信息。
		option.setCoorType("bd09ll");// bd09(百度墨卡托坐标系)、bd09ll(百度经纬度坐标系)、gcj02(国测局经纬度坐标系)
		option.setScanSpan(scanspan);// 设置发起定位请求的间隔时间（当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。当不设此项，或者所设的整数值小于1000（ms）时，采用一次定位模式。设定了定时定位后，可以热切换成一次定位，需要重新设置时间间隔小于1000（ms）即可。）
		option.disableCache(false);// 启用缓存定位
		option.setPoiNumber(1);// 最多返回POI个数
		option.setPoiExtraInfo(false); // 是否需要POI的电话和地址等详细信息
		mLocationClient.setLocOption(option);
	}
	
	private class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) return;
			mainHandler.sendMessage(newMessage(location.getLocType(), location.getLatitude(), location.getLongitude(), location.getAddrStr(), location.getTime()));
		}

		@Override
		public void onReceivePoi(BDLocation location) {

		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		init();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		init();
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
//		releaseWakeLock();
//		stopLocation();
		init();
		super.onDestroy();
	}
	
	private void init() {
		if (phoneId == null) {
			phoneId = ((MyApplication)getApplication()).getPhoneId();
		}
		if (dbUtils == null) {
			dbUtils = ((MyApplication)getApplication()).getDbUtils();
		}
		if (MyConfig.OPEN_WAKELOCK) {
			acquireWakeLock();
		}
		startLocation();
	}

	// 申请电源锁(通常在Activity的 onResume中被调用)
	// <uses-permission android:name="android.permission.WAKE_LOCK" />
	private void acquireWakeLock() {
		if (null == wakeLock) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			// PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。
			// SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
			// SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
			// FULL_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
			// ACQUIRE_CAUSES_WAKEUP：强制使屏幕亮起，这种锁主要针对一些必须通知用户的操作.
			// ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, LocationService.class.getName());
			if (wakeLock != null) {
				wakeLock.acquire();
				if (MyConfig.DEBUG) {
					Log.v(MyConfig.TAG, "电源锁已开启");
				}
			}
		}
	}

	// 释放电源锁(通常在Activity的 onPause中被调用)
	@SuppressWarnings("unused")
	private void releaseWakeLock() {
		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock = null;
			if (MyConfig.DEBUG) {
				Log.v(MyConfig.TAG, "电源锁已关闭");
			}
		}
	}
}

//	<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
//	<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
//	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
//	<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
//	<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
//	<uses-permission android:name="android.permission.INTERNET"/>
//	<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
//	<uses-permission android:name="android.permission.READ_LOGS"/>
//	<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
//	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>