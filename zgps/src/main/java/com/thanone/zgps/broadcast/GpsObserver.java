package com.thanone.zgps.broadcast;

import android.database.ContentObserver;
import android.os.Handler;

import com.thanone.zgps.MyApplication;
import com.thanone.zgps.MyConfig;
import com.thanone.zgps.bean.Logs;
import com.thanone.zgps.util.HttpUrlUtil;
import com.zcj.android.util.UtilAndroid;
import com.zcj.android.web.HttpUtilsHandler;

public class GpsObserver extends ContentObserver {

	private MyApplication context;

	public GpsObserver(Handler handler, MyApplication context2) {
		super(handler);
		this.context = context2;
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);

		boolean enabled = UtilAndroid.isGpsEnabled(context);
		Long userId = context.getLoginUserId();

		if (userId != null) {
			HttpUtilsHandler.send(HttpUrlUtil.URL_USER_SAVELOGS,
					HttpUrlUtil.user_saveLogs(userId, Logs.TYPE_GPS, enabled ? Logs.CONTENT_GPSOPEN : Logs.CONTENT_GPSCLOSE));
			MyConfig.log("GPS:" + enabled);
		}

	}

}
