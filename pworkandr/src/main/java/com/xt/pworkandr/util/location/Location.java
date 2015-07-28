package com.xt.pworkandr.util.location;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Application;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xt.pworkandr.activity.MainActivity;
import com.xt.pworkandr.util.MyApacheHttpClient;

public class Location extends Application {

	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	private boolean isOpenLocation = false;
	private static String loginUserId;
	private static final int SCANSPAN_BEGIN = 2000;// 定位成功前：2秒请求定位1次
	private static final int SCANSPAN_UPDATE = 60000;// 定位成功后：一分钟更新定位一次
	public static String TAG = "zouchongjin";

	@Override
	public void onCreate() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(this);
			mLocationClient.setAK("8edd9480742d2862732e562c40ec1f10");
			mLocationClient.registerLocationListener(myListener);
			setLocationOption(SCANSPAN_BEGIN);
		}
		super.onCreate();
	}

	public void startLocation(String userId) {
		if (userId != null && userId != "") {
			loginUserId = userId;
			if (mLocationClient != null && !isOpenLocation) {
				mLocationClient.start();
				isOpenLocation = true;
				mLocationClient.requestLocation();
			}
		}
	}

	public void stopLocation() {
		if (isOpenLocation) {
			if (mLocationClient != null && mLocationClient.isStarted()) {				
				mLocationClient.stop();
			}
			isOpenLocation = false;
		}
	}

	private void setLocationOption(int scanspan) {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// all表示返回的定位结果包含地址信息，其他值都表示不返回地址信息。
		option.setCoorType("gcj02");// bd09(百度墨卡托坐标系)、bd09ll(百度经纬度坐标系)、gcj02(国测局经纬度坐标系)
		option.setScanSpan(scanspan);// 设置发起定位请求的间隔时间
		option.disableCache(false);// 禁止启用缓存定位
		option.setPoiNumber(1);// 最多返回POI个数
		option.setPoiExtraInfo(false); // 是否需要POI的电话和地址等详细信息
		mLocationClient.setLocOption(option);
	}

	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			int code = location.getLocType();
			if (code == 61 || code == 65 || code == 66 || code == 68 || code == 161) {
				if (submitLocation(location.getLatitude(), location.getLongitude(), loginUserId, location.getAddrStr())) {					
					setLocationOption(SCANSPAN_UPDATE);
				} else {
					mLocationClient.requestLocation();
				}
			} else {
				mLocationClient.requestLocation();
			}
		}

		@Override
		public void onReceivePoi(BDLocation location) {

		}
	}
	
	private boolean submitLocation(final double latitude, final double longitude, final String userId, final String address) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
		params.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
		params.add(new BasicNameValuePair("userId", userId));
		params.add(new BasicNameValuePair("address", address));
		try {
			String result = MyApacheHttpClient.httpPost(MainActivity.serverUrl+"/0001", params);
			if (!MyApacheHttpClient.ERROR.equals(result)) {					
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

}