package com.android.zouchongjin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.android.zouchongjin.contentProvider.MyContentResolverActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zcj.android.view.download.DownloadUtils;

@ContentView(R.layout.layout_main)
public class MainActivity extends Activity {

	@OnClick(R.id.button0)
	private void button0(View v) {
		Intent intent = new Intent(MainActivity.this, com.android.zouchongjin.activity.FirstActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.button_mybroadcast)
	private void button_mybroadcast(View v) {
		Intent intent = new Intent();
		intent.setAction("com.android.demo.HiMessage");
		intent.putExtra("message", "这是传给你的东西");
		sendBroadcast(intent);

		//发送有序广播：sendOrderedBroadcast()
		//前面的接收者有权终止广播：BroadcaseReceiver.abortBroadcast()
		//前面的接收者可以将数据存放在结果对象中：setResultExtras(Bundle)
		//下一个接收者可以获取前面接收者存入的数据：Bundle bundle = getResultExtras(true)
		//优先级别在<intent-filter>的android:priority属性中声明，数值越大优先级别越高（-1000~1000）
	}
	
	@OnClick(R.id.button_db_5)
	private void button_db_5(View v) {
		Intent intent = new Intent(MainActivity.this, MyContentResolverActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.button02)
	private void button02(View v) {
		Intent intent = new Intent(MainActivity.this, com.android.zouchongjin.service.FirstActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.button03)
	private void button03(View v) {
		Intent intent = new Intent(MainActivity.this, com.android.zouchongjin.ui.FirstActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.button05)
	private void button05(View v) {
		Intent intent = new Intent(MainActivity.this, com.android.zouchongjin.e_sensor.MySensorActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.button_shortcut_1)
	private void button_shortcut_1(View v) {
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "YY管理系统");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(this, MainActivity.class));
		sendBroadcast(intent);
	}

	@OnClick(R.id.button09)
	private void button09(View v) {
		Intent intent = new Intent(MainActivity.this, com.android.zouchongjin.i_thread.MyHandlerActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.button10)
	private void button10(View v) {
		Intent intent = new Intent(MainActivity.this, com.android.zouchongjin.i_thread.MyAsyncTaskActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.button11)
	private void button11(View v) {
		Intent intent = new Intent(MainActivity.this, com.android.zouchongjin.demo.zxing.BarCodeTestActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.button12)
	private void button12(View v) {
		Intent intent = new Intent(MainActivity.this, com.android.zouchongjin.webviewshell.MainActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.button_main_loader)
	private void button_main_loader(View v) {
		Intent intent = new Intent(MainActivity.this, com.android.zouchongjin.activity.MyLoader.class);
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		DownloadUtils dh = new DownloadUtils(this, "http://gdown.baidu.com/data/wisegame/cbe0fb2ba1d1dc55/baidushurufa.apk",
				Environment.getExternalStorageDirectory() + "/download");
		dh.checkUpdate(2, false);
	}

}
