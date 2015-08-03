package com.thanone.zgps;

import android.util.Log;

public class MyConfig {

	public static String URLBASE = BuildConfig.URLBASE;

	public static void log(String string) {
		if(BuildConfig.LOG_DEBUG) {
			Log.v("zgps", string);
		}
	}
}
