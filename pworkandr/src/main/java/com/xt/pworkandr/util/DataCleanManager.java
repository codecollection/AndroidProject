package com.xt.pworkandr.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.File;

/** 数据清除管理器 */
@SuppressLint("SdCardPath")
public class DataCleanManager {
	
	public static void clean(Context context, boolean cache, boolean databases, boolean shared_prefs, boolean files, boolean externalCache) {
		if (cache) {cleanInternalCache(context);}
		if (databases) {cleanDatabases(context);}
		if (shared_prefs) {cleanSharedPreference(context);}
		if (files) {cleanFiles(context);}
		if (externalCache) {cleanExternalCache(context);}
	}

	/** 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) */
	private static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	/** 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) */
	private static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
	}

	/** 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) */
	private static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
	}

	/** 按名字清除本应用数据库 */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	/** 清除/data/data/com.xxx.xxx/files下的内容 */
	private static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	/** 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) */
	private static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	/** 删除指定文件夹里的所有文件，保留空文件夹 */
	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				if (item.isFile()) {
					item.delete();
				} else {
					deleteFilesByDirectory(item);
				}
			}
		}
	}
}