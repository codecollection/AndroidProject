package com.thanone.palc;

import android.util.Log;

public class MyConfig {

    public static String URL_BASE = BuildConfig.URLBASE;

    public static void log(String string) {
        if (BuildConfig.LOG_DEBUG) {
            Log.v("palc", string);
        }
    }

}
