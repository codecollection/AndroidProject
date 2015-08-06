package com.android.zouchongjin.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zouchongjin.R;

public class FirstActivity extends Activity {

    private EditText ui_first_et_password;
    private AutoCompleteTextView ui_first_at_colors;

    // 初始化自动提醒的TextView
    private void initAutoTextView() {
        ui_first_at_colors = (AutoCompleteTextView) findViewById(R.id.ui_first_at_colors);
        String[] countries = getResources().getStringArray(R.array.colors);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        ui_first_at_colors.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ui_first);

        // 输入框的键盘按钮处理
        ui_first_et_password = (EditText) findViewById(R.id.ui_first_et_password);
        ui_first_et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Toast.makeText(FirstActivity.this, "登录", Toast.LENGTH_SHORT).show();
                    handled = true;
                }
                return handled;
            }
        });

        // 初始化自动提醒的TextView
        initAutoTextView();

        // 下拉框
        String[] citys = {"北京", "上海"};
        ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, citys);
        aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_ui_1);
        spinner.setAdapter(aAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(FirstActivity.this, ((TextView) view).getText().toString() + ":" + String.valueOf(position) + ":" + String.valueOf(id), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(FirstActivity.this, "nothingSelected", Toast.LENGTH_SHORT).show();
            }
        });

        // 列表页1
        Button b_listview = (Button) findViewById(R.id.button_listview_ui_1);
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

}
