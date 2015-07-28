package com.thanone.palc;

import android.util.Log;

public class MyConfig {

	/** 测试模式（测试模式功能：输出日志） */
	public static final boolean DEBUG = true;

	public static final String URL_BASE = "http://192.168.1.119:8080/palccms";

	/** 如果是DEBUG模式，则输出日志 */
	public static void log(String string) {
		if (DEBUG) {
			Log.v("palc", string);
		}
	}
}
