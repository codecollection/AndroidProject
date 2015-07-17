package com.zandroid.zloc.activity;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.umeng.update.UmengUpdateAgent;
import com.zandroid.zloc.MyApplication;
import com.zandroid.zloc.MyConfig;
import com.zandroid.zloc.R;

public class MainActivity extends Activity {

	BMapManager mBMapMan = null;
	MapView mMapView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		
		((MyApplication)getApplication()).init();
		
		initMap();
	}
	
	private void initMap() {
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init(MyConfig.LOCATION_AK, null);
		setContentView(R.layout.layout_main);

		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(true);

		MapController mMapController = mMapView.getController();
		mMapController.setCenter(new GeoPoint((int) (28.0218 * 1E6), (int) (120.68696 * 1E6)));
		mMapController.setZoom(12);
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		if (mBMapMan != null) {
			mBMapMan.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		if (mBMapMan != null) {
			mBMapMan.start();
		}
		super.onResume();
	}

}


//	<!-- 使用网络功能所需权限 -->
//	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
//	<uses-permission android:name="android.permission.INTERNET" />
//	<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
//	<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
//	
//	<!-- SDK离线地图和cache功能需要读写外部存储器 -->
//	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
//	
//	<!-- 获取设置信息和详情页直接拨打电话需要以下权限 -->
//	<uses-permission android:name="android.permission.READ_PHONE_STATE" />
//	<uses-permission android:name="android.permission.CALL_PHONE" />
//	
//	<!-- 使用定位功能所需权限,demo已集成百度定位SDK,不使用定位功能可去掉以下6项 -->
//	<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
//	<permission android:name="android.permission.BAIDU_LOCATION_SERVICE" />
//	<uses-permission android:name="android.permission.BAIDU_LOCATION_SERVICE" />
//	<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
//	<uses-permission android:name="android.permission.ACCESS_MOCK_LOCATION" />
//	<uses-permission android:name="android.permission.ACCESS_GPS" />
//	
//	<uses-permission android:name="android.permission.WRITE_SETTINGS" />
//	
//	<!-- 添加屏幕支持 -->
//	<supports-screens
//	    android:anyDensity="true"
//	    android:largeScreens="true"
//	    android:normalScreens="true"
//	    android:resizeable="true"
//	    android:smallScreens="true" />
//	
//	<application
//	android:allowBackup="true"
//	android:icon="@drawable/ic_launcher"
//	android:label="@string/app_name"
//	android:theme="@style/AppTheme" >
//	<activity
//	    android:name=".activity.MainActivity"
//	    android:configChanges="orientation|keyboardHidden"
//	    android:screenOrientation="sensor" >
//	    <intent-filter>
//	        <action android:name="android.intent.action.MAIN" />
//	        <category android:name="android.intent.category.LAUNCHER" />
//	    </intent-filter>
//	</activity>
//	</application>
