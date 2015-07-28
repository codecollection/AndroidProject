package com.thanone.palc.service;

import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.lidroid.xutils.exception.DbException;
import com.thanone.palc.MyApplication;
import com.thanone.palc.MyConfig;
import com.thanone.palc.bean.LocationBean;

/**
 * 服务：百度定位服务
 * 
 * 有的移动设备锁屏后为了省电会自动关闭网络连接，此时网络定位模式的定位失效。
 * <p>
 * 此外，锁屏后移动设备若进入CPU休眠，定时定位功能也失效。
 * <p>
 * 若您需要实现在CPU休眠状态仍需定时定位，可以用alarmManager 实现1个CPU可叫醒的timer，定时请求定位。
 * 
 * @author ZCJ
 * @data 2015-4-29
 */
public class LocationService extends Service {

	private static int SCANSPAN_FIRST = 2 * 1000;// 定位未成功前的定位时间间隔：2秒
	private static int SCANSPAN_OK = 10 * 60 * 1000;// 定位成功后的定位时间间隔：10分钟

	private static boolean OPEN_GPS = false;// 设置是否打开GPS；使用GPS前提是用户硬件打开GPS；默认是不打开GPS的；如果打开，则会在状态栏一直显示GPS图标。
	private static boolean OPEN_WAKELOCK = false;// 是否开启手机电源锁（保持CPU运转）
	private static boolean SAVE_NO_CHANGE = true;// 如果位置没变化是否保存
	private static boolean RESTART_LOCATIONSERVICE = true;// 如果定位服务进程被kill掉,系统是否尝试重新启动服务

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListenner();
	private boolean isOpenLocation = false;
	private WakeLock wakeLock;

	private String successTime = "";// 成功获取位置信息的时间，如果第二次获取的位置不变，则时间会是上次获取位置的时间
	private MyApplication application;

	@Override
	public IBinder onBind(Intent intent) {
		MyConfig.log("LocationService--onBind");
		return null;
	}

	@Override
	public void onCreate() {
		MyConfig.log("LocationService--onCreate");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		MyConfig.log("LocationService--onStartCommand");
		application = (MyApplication) getApplication();
		if (OPEN_WAKELOCK) {
			acquireWakeLock();
		}
		startLocation();
		if (RESTART_LOCATIONSERVICE) {
			return START_STICKY;// 如果service进程被kill掉,随后系统会尝试重新创建service,但是intent会丢失。
		} else {
			return START_NOT_STICKY;// 如果系统在onStartCommand()方法返回之后杀死这个服务，那么直到接受到新的Intent对象，这个服务才会被重新创建。这是最安全的选项，用来避免在不需要的时候运行你的服务。
		}
	}

	@Override
	public void onDestroy() {
		MyConfig.log("LocationService--onDestroy");
		if (OPEN_WAKELOCK) {
			releaseWakeLock();
		}
		stopLocation();
		super.onDestroy();
	}

	private void startLocation() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getApplicationContext());// 须在主线程中声明
			mLocationClient.registerLocationListener(myListener);
			setLocationOption(SCANSPAN_FIRST);
		}
		if (mLocationClient != null) {
			if (!isOpenLocation) {
				mLocationClient.start();// 启动定位SDK
				isOpenLocation = true;
				MyConfig.log("locationClient.start()[启动定位SDK]");
			} else {
				mLocationClient.requestLocation();
				MyConfig.log("locationClient.requestLocation()[调用一次定位SDK]");
			}
		}
	}

	private void stopLocation() {
		if (isOpenLocation) {
			if (mLocationClient != null && mLocationClient.isStarted()) {
				mLocationClient.stop();// 关闭定位SDK(设置的参数LocationClientOption仍然保留)
			}
			isOpenLocation = false;
			MyConfig.log("locationClient.stop()[关闭定位SDK]");
		}
	}

	private class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			float radius = location.getRadius();// 获取定位精度半径，单位是米
			float direction = location.getDirection();// 获得手机方向，范围【0-360】，手机上部正朝向北的方向为0°方向
			int operators = location.getOperators();// 获得运营商(0:未知运营商;1:中国移动;2:中国联通;3:中国电信)
			float speed = 0;// 速度
			int satelliteNumber = 0;// 卫星数
			String locType = "";// 是通过哪种方式定位成功的(wf:wifi定位结果;cl:cell定位结果;GPS:gps定位结果)
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				speed = location.getSpeed();
				satelliteNumber = location.getSatelliteNumber();
				locType = "GPS";
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				locType = location.getNetworkLocationType();
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
				locType = "离线定位结果";
			} else if (location.getLocType() == BDLocation.TypeCacheLocation) {
				locType = "定位缓存的结果";
			}

			/*
			 * 61 ： GPS定位结果。 62 ： 扫描整合定位依据失败。此时定位结果无效。 63 ：
			 * 网络异常，没有成功向服务器发起请求。此时定位结果无效。 65 ： 定位缓存的结果。 66 ：
			 * 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果 67 ：
			 * 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果 68 ：
			 * 网络连接失败时，查找本地离线定位时对应的返回结果 161： 表示网络定位结果 162~167： 服务端定位失败
			 * 502：key参数错误 505：key不存在或者非法 601：key服务被开发者自己禁用 602：key mcode不匹配
			 * 501～700：key验证失败
			 */
			int code = location.getLocType();
			if (code == 61 || code == 65 || code == 66 || code == 68 || code == 161) {
				MyConfig.log("定位成功：" + code + ":" + location.getLatitude() + ":" + location.getLongitude() + ":" + location.getAddrStr()
						+ ":" + location.getTime() + ":精度半径" + radius + ":手机方向" + direction + ":速度" + speed + ":卫星数" + satelliteNumber
						+ ":运营商" + operators + ":定位方式" + locType);
				setLocationOption(SCANSPAN_OK);
				saveData(location.getTime(), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()),
						location.getAddrStr());
			} else {
				MyConfig.log("定位失败：" + code + ",重新发起定位请求");
				mLocationClient.requestLocation();
			}
		}
	}

	private void setLocationOption(int scanspan) {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式(Hight_Accuracy[GPS+网络]、Battery_Saving[网络]、Device_Sensors[GPS])
		option.setCoorType("bd09ll");// bd09(百度墨卡托坐标系)、bd09ll(百度经纬度坐标系)、gcj02(国测局经纬度坐标系)
		option.setScanSpan(scanspan);// 设置发起定位请求的间隔时间（当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。当不设此项，或者所设的整数值小于1000（ms）时，采用一次定位模式。设定了定时定位后，可以热切换成一次定位，需要重新设置时间间隔小于1000（ms）即可。）
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		option.setOpenGps(OPEN_GPS);// 设置是否打开GPS，使用GPS前提是用户硬件打开GPS。默认是不打开GPS的。
		mLocationClient.setLocOption(option);
	}

	private void saveData(String time, String latitude, String longitude, String address) {

		// 如果位置没有变
		if (!SAVE_NO_CHANGE) {
			if (time != null && time.trim() != "" && successTime.equals(time)) {
				return;
			}
		}

		// 保存到本地数据库
		try {
			application.getDbUtils().saveBindingId(new LocationBean(latitude, longitude, address, time, new Date()));
			successTime = time;
		} catch (DbException e1) {
			e1.printStackTrace();
		}
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
				MyConfig.log("电源锁已开启");
			}
		}
	}

	// 释放电源锁(通常在Activity的 onPause中被调用)
	private void releaseWakeLock() {
		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock = null;
			MyConfig.log("电源锁已关闭");
		}
	}
}
