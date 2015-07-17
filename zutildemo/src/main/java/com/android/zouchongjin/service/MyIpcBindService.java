package com.android.zouchongjin.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;
import com.android.zouchongjin.MyConfig;

/**
 * 基于Messenger的Bind服务
 * <p>
 * 1、调用者和服务可以是“同应用”、“不同进程”。
 * <p>
 * 2、客户端所需做的只是基于IBinder创建一个Messenger并使用send()方法发送一条消息。详见FirstActivity.java
 * 
 * @author ZCJ
 * @data 2013-3-11
 */
@SuppressLint("HandlerLeak")
public class MyIpcBindService extends Service {

	static final int MSG_SAY_HELLO = 1;// 客户端和服务端相互传值

	// 第一步：服务实现了一个Handler，用来接收每一次被客户端调用的事件，并处理。
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SAY_HELLO:
				Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	// 第二步：Handler被用来创建一个Messenger对象（其为Handler的一个引用）。
	private final Messenger mMessenger = new Messenger(new IncomingHandler());

	// 第三步：Messenger创建一个IBinder，服务从onBind()方法将其返回给客户端。
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(MyConfig.TAG, "MyIpcBindService onbind");
		return mMessenger.getBinder();
	}

}
