package com.xt.pworkandr.util;

import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AndroidConfig {

	private static String path_url_base;// web项目的主地址
	private static String path_url_androidApkFile;// android文件存放的web路径

	private static String name_apkFile;// android文件名
	private static Integer version_android;// android客户端的最新版本号

	public static final void initData(String serverUrl) throws ClientProtocolException, IOException, JSONException {
		Log.v("pworkandr", serverUrl);
		String serverResult = MyApacheHttpClient.httpGet(serverUrl+"/0000");
		Log.v("pworkandr", serverResult);
		JSONObject jsonobject = new JSONObject(serverResult);
		if (jsonobject.getInt("s") == 1) {
			AndroidConfig.setName_apkFile("pworkandr.apk");
			AndroidConfig.setPath_url_androidApkFile(serverUrl+"/android/pworkandr.apk");
			AndroidConfig.setPath_url_base(serverUrl);
			AndroidConfig.setVersion_android(jsonobject.getInt("d"));
		}
	}

	public static String getPath_url_base() {
		return path_url_base;
	}

	public static void setPath_url_base(String path_url_base) {
		AndroidConfig.path_url_base = path_url_base;
	}

	public static String getPath_url_androidApkFile() {
		return path_url_androidApkFile;
	}

	public static void setPath_url_androidApkFile(String path_url_androidApkFile) {
		AndroidConfig.path_url_androidApkFile = path_url_androidApkFile;
	}

	public static String getName_apkFile() {
		return name_apkFile;
	}

	public static void setName_apkFile(String name_apkFile) {
		AndroidConfig.name_apkFile = name_apkFile;
	}

	public static Integer getVersion_android() {
		return version_android;
	}

	public static void setVersion_android(Integer version_android) {
		AndroidConfig.version_android = version_android;
	}

}
