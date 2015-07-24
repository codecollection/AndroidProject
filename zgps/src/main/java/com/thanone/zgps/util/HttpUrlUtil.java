package com.thanone.zgps.util;

import java.util.Date;

import com.lidroid.xutils.http.RequestParams;
import com.thanone.zgps.MyConfig;
import com.zcj.android.web.HttpUtilsHandler;

public class HttpUrlUtil {

	public static String URL_USER_LOGIN = MyConfig.URLBASE + "user_login.ajax";// 登录
	public static String URL_USER_SAVELOGS = MyConfig.URLBASE + "user_saveLogs.ajax";// 记录操作日志

	public static String URL_LOCATION_ALLLIST = MyConfig.URLBASE + "location_allList.ajax";// 管理员获取所有员工最新的位置信息
	public static String URL_LOCATION_ONELIST = MyConfig.URLBASE + "location_oneList.ajax";// 获取指定员工指定时间段的位置信息（beginTime默认今天0点；endTime默认当前时间）
	public static String URL_LOCATION_FINDUSERIDBYUSERNAME = MyConfig.URLBASE + "location_findUserIdByUsername.ajax";// 通过员工编号查询用户ID
	public static String URL_LOCATION_SAVE = MyConfig.URLBASE + "location_save.ajax";// 提交定位数据

	@Deprecated
	public static String URL_SETTING_LOCTIME = MyConfig.URLBASE + "setting_loctime.ajax";// 获取定位时间间隔

	public static RequestParams user_login(String username, String password, String phoneId) {
		return HttpUtilsHandler.initParams(new String[] { "userName", "passWord", "phoneId" }, new Object[] { username, password, phoneId });
	}

	public static RequestParams user_saveLogs(Long userId, String type, String content) {
		return HttpUtilsHandler.initParams(new String[] { "userId", "type", "content" }, new Object[] { userId, type, content });
	}

	public static RequestParams location_oneList(Long userId, Date beginTime, Date endTime) {
		return HttpUtilsHandler.initParams(new String[] { "userId", "beginTime", "endTime" }, new Object[] { userId, beginTime, endTime });
	}

	public static RequestParams location_findUserIdByUsername(String username) {
		return HttpUtilsHandler.initParams(new String[] { "username" }, new Object[] { username });
	}

	public static RequestParams location_save(Long userId, String locationTime, String lat, String lng, String address) {
		return HttpUtilsHandler.initParams(new String[] { "userId", "locationTime", "lat", "lng", "address" }, new Object[] { userId,
				locationTime, lat, lng, address });
	}

}
