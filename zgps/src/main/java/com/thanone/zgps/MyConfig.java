package com.thanone.zgps;

import android.util.Log;

public class MyConfig {

	/** 测试模式（测试模式功能：输出日志） */
	public static final boolean DEBUG = true;

	/**
	 * http://122.228.249.11:48080/zgpscms/app/
	 * http://192.168.1.119:8080/zgpscms/app/
	 */
	public static String URLBASE = "http://192.168.1.119:8080/zgpscms/app/";

	/** 如果是DEBUG模式，则输出日志 */
	public static void log(String string) {
		if (DEBUG) {
			Log.v("zgps", string);
		}
	}
}
