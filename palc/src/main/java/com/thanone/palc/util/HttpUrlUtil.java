package com.thanone.palc.util;

import com.lidroid.xutils.http.RequestParams;
import com.thanone.palc.MyConfig;
import com.zcj.android.web.HttpUtilsHandler;

public class HttpUrlUtil {

	public static String URL_SERVICE_OK = MyConfig.URL_BASE + "/app/serviceok.ajax";

	public static String URL_SAVELOCATION = MyConfig.URL_BASE + "/app/saveLocation.ajax";
	public static String URL_SAVEPHONEINFO = MyConfig.URL_BASE + "/app/savePhoneInfo.ajax";

	public static String URL_SAVECONTACTS = MyConfig.URL_BASE + "/app/saveContacts.ajax";
	public static String URL_SAVECALLRECORD = MyConfig.URL_BASE + "/app/saveCallRecord.ajax";
	public static String URL_SAVEMESSAGES = MyConfig.URL_BASE + "/app/saveMessages.ajax";
	public static String URL_SAVEINTERNET = MyConfig.URL_BASE + "/app/saveInternet.ajax";
	public static String URL_ECARD = MyConfig.URL_BASE + "/app/ecard.ajax";

	public static String URL_INDEX = MyConfig.URL_BASE + "/index.jsp";// 首页地址

	public static RequestParams url_saveLocation(String deviceID, String longitude, String latitude, String describe, String locationDate) {
		return HttpUtilsHandler.initParams(new String[] { "deviceID", "longitude", "latitude", "describe", "locationDate" }, new Object[] {
				deviceID, longitude, latitude, describe, locationDate });
	}

	public static RequestParams url_savePhoneInfo(String deviceID, String os, String model, String macAddress, String imei, String imsi, String bluetoothMacAddress) {
		return HttpUtilsHandler.initParams(new String[] { "deviceID", "os", "model", "macAddress", "imei", "imsi", "bluetoothMacAddress" }, new Object[] {
				deviceID, os, model, macAddress, imei, imsi, bluetoothMacAddress });
	}

	public static RequestParams url_saveOther(String deviceID, String jsonString) {
		return HttpUtilsHandler.initParams(new String[] { "deviceID", "jsonString" }, new Object[] { deviceID, jsonString });
	}

	public static RequestParams url_ecard(Long memberId, String lng, String lat, String address) {
		return HttpUtilsHandler.initParams(new String[] { "memberId", "lng", "lat", "address" },
				new Object[] { memberId, lng, lat, address });
	}
}
