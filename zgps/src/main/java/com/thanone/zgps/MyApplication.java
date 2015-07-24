package com.thanone.zgps;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.lidroid.xutils.DbUtils;
import com.thanone.zgps.bean.Logs;
import com.thanone.zgps.bean.User;
import com.thanone.zgps.broadcast.GpsObserver;
import com.thanone.zgps.service.LocationService;
import com.thanone.zgps.util.HttpUrlUtil;
import com.zcj.android.util.UtilAndroid;
import com.zcj.android.util.UtilAppFile;
import com.zcj.android.util.UtilSharedPreferences;
import com.zcj.android.web.HttpUtilsHandler;

public class MyApplication extends Application {

	// XML配置文件信息
	public static final String XML_NAME = "config";// 配置文件名
	public static final String XML_U = "u";// 账号
	public static final String XML_P = "p";// 密码

	private String phoneId;// 手机编号
	private DbUtils dbUtils;// 数据库操作对象
	private Intent locationService;// 定位服务

	// 定位图标
	private BitmapDescriptor bitmap_begin;
	private BitmapDescriptor bitmap_end;
	private BitmapDescriptor bitmap_uncheck;
	private BitmapDescriptor bitmap_check;
	private BitmapDescriptor bitmap_error;

	// 登录用户
	private User loginUser;

	/** 文件保存的根目录 */
	public static String FILESAVEPATH;
	/** 截图的保存路径 */
	public static String FILESAVEPATH_SNAPSHOT;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		MyConfig.log("MyApplication--onCreate");

		// 如果是主进程
		if ("com.thanone.zgps".equals(UtilAndroid.getProcessName(getApplicationContext()))) {

			SDKInitializer.initialize(getApplicationContext());
			bitmap_begin = BitmapDescriptorFactory.fromResource(R.drawable.icon_loc_4);
			bitmap_end = BitmapDescriptorFactory.fromResource(R.drawable.icon_loc_5);
			bitmap_uncheck = BitmapDescriptorFactory.fromResource(R.drawable.icon_loc_1);
			bitmap_check = BitmapDescriptorFactory.fromResource(R.drawable.icon_loc_3);
			bitmap_error = BitmapDescriptorFactory.fromResource(R.drawable.icon_loc_2);

			if (UtilAppFile.sdcardExist()) {
				FILESAVEPATH = UtilAppFile.getSdcardPath();
			} else {
				FILESAVEPATH = UtilAppFile.getFilesDir(this);
			}
			FILESAVEPATH_SNAPSHOT = FILESAVEPATH + "Snapshot/";
			File file = new File(FILESAVEPATH_SNAPSHOT);
			if (!file.exists()) {
				file.mkdirs();
			}

			try {
				phoneId = UtilAndroid.getUdid(this, "zgps", "UUID");
				dbUtils = DbUtils.create(this);
				locationService = new Intent(this, LocationService.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			getContentResolver().registerContentObserver(Settings.Secure.getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED), false,
					new GpsObserver(new Handler(), this));
		}

		super.onCreate();
	}

	/** 登录（同时保存账号密码、记录日志(包括手机串号)、启动定位服务） */
	public void login(User obj) {

		Map<String, String> map = new HashMap<String, String>();
		map.put(XML_U, obj.getUsername());
		map.put(XML_P, obj.getPassword());
		UtilSharedPreferences.save(this, XML_NAME, map);
		setLoginUser(obj);

		HttpUtilsHandler.send(HttpUrlUtil.URL_USER_SAVELOGS, HttpUrlUtil.user_saveLogs(obj.getId(), Logs.TYPE_LOGIN, Logs.CONTENT_LOGIN+"("+UtilAndroid.getIMEI(this)+")"));

		if (locationService != null) {
			startService(locationService);
			MyConfig.log("--startService");
		}
	}

	/** 注销（同时记录日志、清除密码、关闭定位服务） */
	public void logout() {

		HttpUtilsHandler.send(HttpUrlUtil.URL_USER_SAVELOGS,
				HttpUrlUtil.user_saveLogs(getLoginUserId(), Logs.TYPE_LOGIN, Logs.CONTENT_LOGOUT));

		Map<String, String> map = new HashMap<String, String>();
		map.put(XML_P, null);
		UtilSharedPreferences.save(this, XML_NAME, map);
		setLoginUser(null);

		if (locationService != null) {
			stopService(locationService);
			MyConfig.log("--stopService");
		}
	}

	/** 获取登录用户的ID，如果未登录，则返回NULL */
	public Long getLoginUserId() {
		User user = getLoginUser();
		if (user != null) {
			return user.getId();
		}
		return null;
	}

	public String getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}

	public DbUtils getDbUtils() {
		return dbUtils;
	}

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	public void setDbUtils(DbUtils dbUtils) {
		this.dbUtils = dbUtils;
	}

	public BitmapDescriptor getBitmap_begin() {
		return bitmap_begin;
	}

	public void setBitmap_begin(BitmapDescriptor bitmap_begin) {
		this.bitmap_begin = bitmap_begin;
	}

	public BitmapDescriptor getBitmap_end() {
		return bitmap_end;
	}

	public void setBitmap_end(BitmapDescriptor bitmap_end) {
		this.bitmap_end = bitmap_end;
	}

	public BitmapDescriptor getBitmap_uncheck() {
		return bitmap_uncheck;
	}

	public void setBitmap_uncheck(BitmapDescriptor bitmap_uncheck) {
		this.bitmap_uncheck = bitmap_uncheck;
	}

	public BitmapDescriptor getBitmap_check() {
		return bitmap_check;
	}

	public void setBitmap_check(BitmapDescriptor bitmap_check) {
		this.bitmap_check = bitmap_check;
	}

	public BitmapDescriptor getBitmap_error() {
		return bitmap_error;
	}

	public void setBitmap_error(BitmapDescriptor bitmap_error) {
		this.bitmap_error = bitmap_error;
	}

}
