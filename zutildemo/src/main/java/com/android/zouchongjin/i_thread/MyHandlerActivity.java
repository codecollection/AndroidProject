package com.android.zouchongjin.i_thread;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.android.zouchongjin.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Handler：用于定时任务调度、线程间通信. 本例：子线程中用主线程的Handler来给主线程发送Message.
 * 额外：子线程默认是没有消息队列的，所以子线程中new Handler()不可行，需要使用HandlerThread.
 * 
 * @author ZCJ
 * @data 2013-4-2
 */
@SuppressLint("HandlerLeak")
@ContentView(R.layout.layout_i_handler)
public class MyHandlerActivity extends Activity {

	Handler mainHandler1;

	// 新建消息
	Message newMessage(int theWhat, Object theObj) {
		Message message = new Message();
		message.what = theWhat;
		message.obj = theObj;
		return message;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		// 初始化主线程的Handler.
		mainHandler1 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					Toast.makeText(MyHandlerActivity.this, "mainHandler1:" + String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};
	}

	@OnClick(R.id.button_i_1)
	private void button_i_1(View v) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mainHandler1.sendMessage(newMessage(1, "123123321321"));
			}
		}).start();
	}

}
