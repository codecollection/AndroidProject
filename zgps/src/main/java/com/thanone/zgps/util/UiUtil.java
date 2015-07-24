package com.thanone.zgps.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.thanone.zgps.activity.MapActivity;
import com.zcj.android.util.UtilAndroid;
import com.zcj.android.util.UtilDialog;
import com.zcj.android.util.UtilDialog.DialogCallback;
import com.zcj.android.util.UtilIntent;

public class UiUtil {

	/** 提示 */
	public static void showToast(Context from, String value) {
		Toast.makeText(from, value, Toast.LENGTH_SHORT).show();
	}

	public final static String MAP_TYPE_KEY = "mapType";
	/** 所有员工的位置 */
	public final static Integer MAP_TYPE_ALL = 1;
	/** 个人的轨迹 */
	public final static Integer MAP_TYPE_ONE = 2;

	public static void toMap(Context from, Integer mapType, Long userId) {
		Intent intent = new Intent(from, MapActivity.class);
		intent.putExtra(MAP_TYPE_KEY, mapType);
		if (userId != null) {
			intent.putExtra("userId", userId);
		}
		from.startActivity(intent);
	}
	
	public static boolean gpsCheck(final Context from) {
		boolean gpsopen = UtilAndroid.isGpsEnabled(from);
		if (!gpsopen) {
			UtilDialog.builderAlertDialog(from, "提示", "请开启GPS", new DialogCallback() {
				@Override
				public void doSomething_ChickOK() {
					UtilIntent.openGpsSetting(from);
				}
			});
		}
		return gpsopen;
	}

}
