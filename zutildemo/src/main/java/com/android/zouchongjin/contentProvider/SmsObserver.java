package com.android.zouchongjin.contentProvider;

import java.util.Date;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.android.zouchongjin.MyConfig;
import com.zcj.util.UtilDate;

/**
 * 监听短信信箱变化，有变化时马上运行onChange方法，观察者模式。
 * <p>
 * 		通过getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, new SmsObserver(new Handler(), this));注册此服务
 * 
 * @author zouchongjin@sina.com
 * @data 2015年6月15日
 */
public class SmsObserver extends ContentObserver {
	
	private Context context;

	public SmsObserver(Handler handler, Context context2) {
		super(handler);
		this.context = context2;
	}

	@Override
	public void onChange(boolean selfChange) {

		// content://sms/inbox 收件箱
		// content://sms/sent 已发送
		// content://sms/draft 草稿
		// content://sms/outbox 发件中
		// content://sms/failed 发送失败
		// content://sms/queued 待发送列表

		Uri uri = Uri.parse("content://sms/outbox");// 正在发送中的短信
		Cursor cursor = context.getContentResolver().query(uri, new String[] { "date", "address", "body" }, null, null, "_id desc limit 1");
		if (cursor.moveToNext()) {
			long ms = cursor.getLong(0);
			String date = UtilDate.format(new Date(ms));
			String address = cursor.getString(1);
			String body = cursor.getString(2);
			Log.v(MyConfig.TAG, date + ":" + address + ":" + body + "out");
			Log.v(MyConfig.TAG, "记录短信变化成功"+address);
		}
	}
	
}
