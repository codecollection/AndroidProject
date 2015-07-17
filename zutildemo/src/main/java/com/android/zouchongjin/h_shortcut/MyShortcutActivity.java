package com.android.zouchongjin.h_shortcut;

import com.android.zouchongjin.MainActivity;
import com.android.zouchongjin.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 程序外通过Launcher创建快捷方式
 * 
 * @author ZCJ
 * @data 2013-4-1
 */
public class MyShortcutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().getAction().equals(Intent.ACTION_CREATE_SHORTCUT)) {
			Intent intent = new Intent();
			intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "XX管理系统");
			intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));
			intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(this, MainActivity.class));
			setResult(RESULT_OK, intent);
			finish();
		}
	}
}
/** 
	<activity android:name=".h_shortcut.MyShortcutActivity">
	<intent-filter>
	    <action android:name="android.intent.action.CREATE_SHORTCUT"/>
	</intent-filter>
	</activity>
*/
