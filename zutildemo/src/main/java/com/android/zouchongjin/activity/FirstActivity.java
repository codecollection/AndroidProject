package com.android.zouchongjin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.android.zouchongjin.MyConfig;
import com.android.zouchongjin.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.layout_activity_first)
public class FirstActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(MyConfig.TAG, "zouchongjin:onCreate");
		
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
	}
	
	@OnClick(R.id.button11)
	private void button11(View v) {
		Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
		intent.putExtra("myKey", "显示传过来的值");
		startActivity(intent);
		// startActivityForResult(intent, 1); // 有返回的发起请求
	}
	
	// 第二种添加Fragment的方案：通过代码动态添加。
	@OnClick(R.id.button_activity_2)
	private void button_activity_2(View v) {
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		MyFragment fragment = new MyFragment();
		fragmentTransaction.add(R.id.layout_activity_1, fragment);
		fragmentTransaction.commit();
		
//		fragmentTransaction.addToBackStack(null);// 添加此事务到返回栈（点击返回按钮可以恢复到之前的状态）
//		fragmentManager.popBackStack();// 把Fragment从返回栈中弹出(模拟用户的返回命令).
		
//		fragmentTransaction.add(theFragment, "zzz");// 添加一个没有UI的fragment
//		fragmentTransaction.replace(R.id.layout_activity_1, fragment);// 用新的fragment替换旧的
//		fragmentTransaction.hide(theFragment);// 隐藏某fragment
//		fragmentTransaction.show(theFragment);// 显示某fragment
//		fragmentManager.findFragmentById(R.id.activity_myfragment);// 一般用于 第一种添加Fragment的方案中
//		fragmentManager.findFragmentByTag("zzz");
	}
	
	
	// 有返回时调用的方法
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == 1 && resultCode == 2) {
//			Toast.makeText(FirstActivity.this, data.getStringExtra("back"), Toast.LENGTH_LONG).show();
//		}
//	}

		@Override
		protected void onStart() {
			super.onStart();
			// 状态：变成可见
			Log.i(MyConfig.TAG, "zouchongjin:onstart");
		}
		
		@Override
		protected void onRestart() {
			super.onRestart();
			// 状态：stop到start的过度阶段
			Log.i(MyConfig.TAG, "zouchongjin:onRestart");
		}
	
			@Override
			protected void onResume() {
				super.onResume();
				// 状态：获得焦点
				// 1、Foreground process
				Log.i(MyConfig.TAG, "zouchongjin:onResume");
			}
		
			@Override
			protected void onPause() {
				super.onPause();
				// 状态：部分可见，睡眠或者有弹出框出现时，内存严重不足时也可以被kill。
				// 你可以：保存一些需要持久化的数据。
				// 2、Visible process
				Log.i(MyConfig.TAG, "zouchongjin:onPause");
			}
	
		@Override
		protected void onStop() {
			super.onStop();
			// 点击“首页”的时候调用
			// 状态：被另一个Activity完全覆盖，不可见的，活的，内存不足会被kill
			// 4、Background process
			Log.i(MyConfig.TAG, "zouchongjin:onStop");
		}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 5、Empty process
		// 点击“返回”的时候调用
		// 你可以：释放所有资源。比如在这个Activity里，你用onCreate创建一个后台线程在下载网络资源，则你需要在这里停止这个线程。
		Log.i(MyConfig.TAG, "zouchongjin:onDestroy");
	}

	
	@Override
	protected void onSaveInstanceState(Bundle outState) {		
		// 被摧毁前(屏幕方向改变/内存不足/Home键)缓存一些数据。
		// onPause之前调用。不能保证每次销毁的时候都调用此方法，比如用户使用“返回”按钮主动离开此界面不会调用此方法。所以此方法适合记录一些UI上的状态，而不适合保存该持久化的数据。
		outState.putString("abc", "myabc");
		
		Log.i(MyConfig.TAG, "zouchongjin:onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// 被重新创建后恢复缓存的数据
		String abc = savedInstanceState.getString("abc");
		
		Log.i(MyConfig.TAG, "zouchongjin:onRestoreInstanceState.----abc="+abc);
		super.onRestoreInstanceState(savedInstanceState);
	}
	
}
