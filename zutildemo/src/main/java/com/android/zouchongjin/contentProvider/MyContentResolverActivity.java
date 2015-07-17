package com.android.zouchongjin.contentProvider;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * 获取其他APP的数据,如读取通讯录列表、增删改查其他应用的数据。
 * 监听其他应用的数据变化
 * @author ZCJ
 * @data 2013-3-20
 */
public class MyContentResolverActivity extends Activity {

	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// -----------------------------------------获取系统应用提供的内容BEGIN-------------------------------------------
		this.initBookListView();
		// -----------------------------------------获取系统应用提供的内容END-------------------------------------------
		
		setContentView(listView);
		
		// ------------------------C应用：接收A应用的数据变化通知BEGIN----------------------------------------------------------
		this.studentRegisterContentObserver();
		// -------------------------C应用：接收A应用的数据变化通知END--------------------------------------------------------
	}
	
	// -----------------------------------------获取系统应用提供的内容BEGIN-------------------------------------------
	@SuppressWarnings("deprecation")
	private void initBookListView() {
		listView = new ListView(this);
		
		Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		startManagingCursor(cursor);// 将获得的cursor交由Activity管理，这样cursor的生命周期和Activity便自动同步
		ListAdapter listAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_expandable_list_item_1, cursor,
				new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME }, new int[] { android.R.id.text1 });

		listView.setAdapter(listAdapter);
	}
	// -----------------------------------------获取系统应用提供的内容END-------------------------------------------
	
	
	// ------------------------C应用：接收A应用的数据变化通知BEGIN----------------------------------------------------------
	private void studentRegisterContentObserver() {
		Uri uri = Uri.parse("content://sundy.android.demo.provider/student");
		this.getContentResolver().registerContentObserver(uri, true, new StudentContentObserver(new Handler()));
	}
	private class StudentContentObserver extends ContentObserver {
		public StudentContentObserver(Handler handler) {
			super(handler);
		}
		@Override
		public void onChange(boolean selfChange) {
			Uri uri = Uri.parse("content://sundy.android.demo.provider/student");
			Cursor cursor = getContentResolver().query(uri, null, null, null, "id desc limit 1");
			while(cursor.moveToFirst()) {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				Log.i("zzz", name);
			}
			cursor.close();
		}
	}
	// -------------------------C应用：接收A应用的数据变化通知END--------------------------------------------------------
	
	
	// ------------------------B应用：调用A应用的方法BEGIN--------------------------------------------------------------
	/** 客户端调用服务器的MyContentProvider内容提供者中的student的insert方法 */
	public void studentInsertTest() {
		Uri uri = Uri.parse("content://sundy.android.demo.provider/student");
		ContentValues values = new ContentValues();
		values.put("id", 1);
		values.put("name", "zcj");
		values.put("age", 18);
		Uri resultUri = this.getContentResolver().insert(uri, values);
		Log.i("zzz", ContentUris.parseId(resultUri)+"");
	}
	/** 客户端调用服务器的MyContentProvider内容提供者中的student的update方法 */
	public void studentUpdateTest() {
		Uri uri = Uri.parse("content://sundy.android.demo.provider/student");
		ContentValues values = new ContentValues();
		values.put("name", "yiyi");
		this.getContentResolver().update(uri, values, "id=?", new String[]{"1"});
		
//		// 第二种方法
//		Uri uri = Uri.parse("content://sundy.android.demo.provider/student/1");
//		ContentValues values = new ContentValues();
//		values.put("name", "yiyi");
//		this.getContentResolver().update(uri, values, null, null);
	}
	/** 客户端调用服务器的MyContentProvider内容提供者中的student的delete方法 */
	public void studentDeleteTest() {
		Uri uri = Uri.parse("content://sundy.android.demo.provider/student");
		this.getContentResolver().delete(uri, "id=?", new String[]{"1"});
		
//		// 第二种方法
//		Uri uri = Uri.parse("content://sundy.android.demo.provider/student/1");
//		this.getContentResolver().delete(uri, null, null);
	}
	/** 客户端调用服务器的MyContentProvider内容提供者中的student的query方法 */
	public void studentQueryTest() {
		Uri uri = Uri.parse("content://sundy.android.demo.provider/student");
		Cursor cursor = this.getContentResolver().query(uri, null, null, null, "id desc");
		while(cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex("name"));
			Log.i("zzz", name);
		}
		cursor.close();
	}
	// ------------------------B应用：调用A应用的方法END--------------------------------------------------------------
	
}