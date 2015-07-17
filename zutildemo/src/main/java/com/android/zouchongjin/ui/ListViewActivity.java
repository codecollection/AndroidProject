package com.android.zouchongjin.ui;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 不需要layout.xml文件，使用android提供的layout
 * @author ZCJ
 * @data 2013-9-2
 */
public class ListViewActivity extends ListActivity {

	String[] datas = {"a","b","c","d","e","f","a","b","c","d","e","f"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_listview);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(ListViewActivity.this, "索引"+String.valueOf(position), Toast.LENGTH_SHORT).show();
		super.onListItemClick(l, v, position, id);
	}

}
