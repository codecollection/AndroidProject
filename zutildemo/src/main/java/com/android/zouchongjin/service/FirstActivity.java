package com.android.zouchongjin.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.zouchongjin.MyConfig;
import com.android.zouchongjin.R;
import com.android.zouchongjin.service.MyBindService.MyBinder;
import com.android.zouchongjin.service.remote.client.MyRemoteActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.layout_service_first)
public class FirstActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
	}
	
//---------------------------------------1、基本服务--------------------------------------------------
	
	private Intent serviceIntent;
	
	// 启动start服务
	@OnClick(R.id.button_service_1)
	private void button_service_1(View v) {
		serviceIntent = new Intent(FirstActivity.this, MyService.class);
		serviceIntent.putExtra("myname", "zcj");
		startService(serviceIntent);
	}
	
	// 停止start服务
	@OnClick(R.id.button_service_2)
	private void button_service_2(View v) {
		if (serviceIntent != null) {
			stopService(serviceIntent);
		}
	}
	
//---------------------------------------1、基本服务--------------------------------------------------

	
//---------------------------------------2、Bind服务（同应用同线程）-------------------------------------------------
	
	private boolean bound = false;
	private MyBindService myBindService;
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MyBinder binder = (MyBinder)service;
			myBindService = binder.getService();
			bound = true;
		}
		
		// 当与服务的连接发生了不可预期的丢失，Android系统调用这个方法，例如，服务发生了冲突或被杀死。在服务被解绑时并不会调用这个方法。
		@Override
		public void onServiceDisconnected(ComponentName name) {
			myBindService = null;
			bound = false;
		}
	};
	
	@OnClick(R.id.button_service_3)
	private void button_service_3(View v) {
		
		// 绑定服务（多客户端可以绑定同一个服务）
		// 绑定和解绑的时机：
		// 	1、当你的活动可见时，如果你需要做的只是与服务交互，你应该在onStart()中绑定，在onStop()中解绑。
		// 	2、如果你希望即使活动在后台停止时也接收响应，那么你可以在onCreate()方法中绑定，并在onDestroy()方法中解绑。
		bindService(new Intent(FirstActivity.this, MyBindService.class), conn, Context.BIND_AUTO_CREATE);
		
		// 调用服务
		if (bound) {
			Toast.makeText(FirstActivity.this, myBindService.query(), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(FirstActivity.this, "服务正在启动中,请稍后再调用...", Toast.LENGTH_SHORT).show();
		}
	}
	
	@OnClick(R.id.button_service_4)
	private void button_service_4(View v) {
		// 解绑服务（当所有的客户端解绑之后，此服务自动销毁）
		if (bound) {
			unbindService(conn);
			bound = false;
		}
	}
	
//---------------------------------------2、Bind服务（同应用同线程）-------------------------------------------------	
	
	
//---------------------------------------3、IPC（同应用不同进程）---------------------------------------------------------
	
	private boolean bound2 = false;
	private Messenger messengerService = null;
	private ServiceConnection conn2 = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			messengerService = new Messenger(service);
			bound2 = true;
		}
		
		// 当与服务的连接发生了不可预期的丢失，Android系统调用这个方法，例如，服务发生了冲突或被杀死。在服务被解绑时并不会调用这个方法。
		@Override
		public void onServiceDisconnected(ComponentName name) {
			messengerService = null;
			bound2 = false;
		}
	};
	
	@OnClick(R.id.button_service_5)
	private void button_service_5(View v) {

		// 绑定IPC服务
		bindService(new Intent(FirstActivity.this, MyIpcBindService.class), conn2, Context.BIND_AUTO_CREATE);
		
		// 发送Messenger到服务
		if (bound2) {
			Message msg = Message.obtain(null, MyIpcBindService.MSG_SAY_HELLO, 0, 0);
	        try {
	        	messengerService.send(msg);
	        } catch (RemoteException e) {
	            e.printStackTrace();
	        }
		} else {
			Toast.makeText(FirstActivity.this, "服务正在启动中,请稍后再调用...", Toast.LENGTH_SHORT).show();
		}
	}
	
	@OnClick(R.id.button_service_6)
	private void button_service_6(View v) {
		// 解绑IPC服务
		if (bound2) {
			unbindService(conn2);
			bound2 = false;
		}
	}
		
//---------------------------------------3、IPC（同应用不同进程）---------------------------------------------------------	
		
	
//---------------------------------------4、IntentService（IntentService服务里使用新线程处理所有启动请求）-------------------------
	
	@OnClick(R.id.button_service_7)
	private void button_service_7(View v) {
		Intent intent = new Intent(FirstActivity.this, MyIntentService.class);
		Log.i(MyConfig.TAG, "主线程ID：" + Thread.currentThread().getId());
		startService(intent);
	}
	
//---------------------------------------4、IntentService（IntentService服务里使用新线程处理所有启动请求）-------------------------
		
	
//---------------------------------------5、Remote----------------------------------------------------------------------
	
	// 转到remote的Activity
	@OnClick(R.id.button_service_remote)
	private void button_service_remote(View v) {
		startActivity(new Intent(FirstActivity.this, MyRemoteActivity.class));
	}
			
//---------------------------------------5、Remote----------------------------------------------------------------------
	
	
//------------------  AIDL（用于进程间通信）之 跨应用调用Service  -----------------------------------------------------
	
	// 启动方代码
	@OnClick(R.id.button_service_aidl)
	private void aidl(View v) {
		Log.v("zouchongjin", "点击了aidl按钮");
		Intent i = new Intent();
		i.setComponent(new ComponentName("com.thanone.palc", "com.thanone.palc.service.UploadService"));
		startService(i);
	}
	// 被启动方代码
		//	<service
		//	    android:name="com.thanone.palc.service.UploadService"
		//	    android:exported="true" >
		//	</service>
	
//------------------  AIDL（用于进程间通信）之 跨应用调用Service  -----------------------------------------------------
	
	
}
