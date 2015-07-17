package com.android.zouchongjin.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zouchongjin.R;

public class FirstActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_ui_first);
		
		// 单选
		RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup_ui_1);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radio_ui_0) {
					Toast.makeText(FirstActivity.this, "选中了男", Toast.LENGTH_SHORT).show();
				} else if (checkedId == R.id.radio_ui_1) {
					Toast.makeText(FirstActivity.this, "选中了女", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		
		// 多选
		CheckBox cb1 = (CheckBox)findViewById(R.id.checkBox_ui_1);
		CheckBox cb2 = (CheckBox)findViewById(R.id.checkBox_ui_2);
		OnCheckedChangeListener ccl = new MyOnChangeCheckBoxlistener();
		cb1.setOnCheckedChangeListener(ccl);
		cb2.setOnCheckedChangeListener(ccl);
		
		// 下拉框
		String[] citys = {"北京","上海"};
		ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, citys);
		Spinner spinner = (Spinner)findViewById(R.id.spinner_ui_1);
		spinner.setAdapter(aAdapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(FirstActivity.this, ((TextView)view).getText().toString()+":"+String.valueOf(position)+":"+String.valueOf(id), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Toast.makeText(FirstActivity.this, "nothingSelected", Toast.LENGTH_SHORT).show();
			}
		});
		
		// 列表页1
		Button b_listview = (Button)findViewById(R.id.button_listview_ui_1);
		b_listview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FirstActivity.this, ListViewActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	// 菜单列表
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.main, menu);
		menu.add("加一个");
		return super.onCreateOptionsMenu(menu);
	}
	// 点击菜单
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menuitem1) {
			// 弹出框
			AlertDialog ad = new AlertDialog.Builder(this)
					.setMessage("关于我的信息")
					.setView(this.getLayoutInflater().inflate(R.layout.layout_ui_dialog, null))
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
								}
							})
					.create();
			ad.show();
		} else if (item.getItemId() == R.id.menuitem2) {
			finish();// 退出当前Activity，进程还存在
			Process.killProcess(Process.myPid());// 退出程序，进程也不存在，不推荐使用
		}
		return super.onOptionsItemSelected(item);
	}

	// 用于多选
	private final class MyOnChangeCheckBoxlistener implements OnCheckedChangeListener {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (buttonView.getId() == R.id.checkBox_ui_1 && isChecked) {
				Toast.makeText(FirstActivity.this, "选中了中国", Toast.LENGTH_SHORT).show();
			} else if (buttonView.getId() == R.id.checkBox_ui_2 && isChecked) {
				Toast.makeText(FirstActivity.this, "选中了美国", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
}
