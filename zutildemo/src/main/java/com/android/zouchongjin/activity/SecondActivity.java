package com.android.zouchongjin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.zouchongjin.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.layout_activity_second)
public class SecondActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		
		// 取得传递过来的值
		String theValue = this.getIntent().getStringExtra("myKey");
		TextView editText = (TextView)findViewById(R.id.textView1_activity);
		editText.setText(theValue);
		
		// 其他取值方式
		// System.out.println(this.getIntent().getExtras().getString("myKey"));
		// System.out.println((ArrayList<HashMap<String, Object>>)this.getIntent().getExtras().getSerializable("myList"));
	}
	
	// 关闭按钮
	@OnClick(R.id.button12)
	private void button12(View v) {
		
		// 返回
		// Intent intent = new Intent();
		// intent.putExtra("back", "Back Data");
		// setResult(2, intent);
		
		finish();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i("z2", "zouchongjin:onstart");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("z2", "zouchongjin:onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("z2", "zouchongjin:onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("z2", "zouchongjin:onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i("z2", "zouchongjin:onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("z2", "zouchongjin:onDestroy");
	}
	
}
