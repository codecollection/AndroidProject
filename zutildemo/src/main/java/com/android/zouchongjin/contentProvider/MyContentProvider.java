package com.android.zouchongjin.contentProvider;

import com.lidroid.xutils.DbUtils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * 内容提供者
 * <p>
 * A应用
 * 内容提供者：供B应用调用并增删改查此APP的数据，B应用详见MyContextResolverActivity
 * 监听数据变化：当Insert方法被B应用调用时，此APP发出数据变化通知，C应用就可以接收到通知，C应用详见MyContextResolverActivity
 * 
 * @author ZCJ
 * @data 2013-3-20
 */
public class MyContentProvider extends ContentProvider {

	/** 用于URI匹配 */
	private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		MATCHER.addURI("sundy.android.demo.provider", "student", 1);
		MATCHER.addURI("sundy.android.demo.provider", "student/#", 2);
	}
	
	/** ContentProvider在其他应用第一次访问它时才被创建，实例创建之后调用此方法 */
	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		DbUtils dbUtils = DbUtils.create(this.getContext());
		SQLiteDatabase db = dbUtils.getDatabase();
		switch (MATCHER.match(uri)) {
		case 1:
			return db.query("student", projection, selection, selectionArgs, null, null, sortOrder);
		case 2:
			String where = "id="+uri.getLastPathSegment();
			if (selection != null && !"".equals(selection.trim())) {
				where = where + " and " + selection;
			}
			return db.query("student", projection, where, selectionArgs, null, null, sortOrder);
		default:
			throw new IllegalArgumentException("非法Uri："+uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		DbUtils dbUtils = DbUtils.create(this.getContext());
		SQLiteDatabase db = dbUtils.getDatabase();
		switch (MATCHER.match(uri)) {
		case 1:
			long rowid = db.insert("student", null, values);// 返回主键值1
			Uri insertUri = ContentUris.withAppendedId(uri, rowid);// content://sundy.android.demo.provider/student/1
			
			// --------------发出数据变化通知--------------------
			this.getContext().getContentResolver().notifyChange(uri, null);
			
			return insertUri;
		default:
			throw new IllegalArgumentException("非法Uri："+uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		DbUtils dbUtils = DbUtils.create(this.getContext());
		SQLiteDatabase db = dbUtils.getDatabase();
		switch (MATCHER.match(uri)) {
		case 1:// 所有条件都放在参数里
			int count = db.delete("student", selection, selectionArgs);
			return count;
		case 2:// ID放在URI里，其他条件放在参数里
			String where = "id="+uri.getLastPathSegment();
			if (selection != null && !"".equals(selection.trim())) {
				where = where + " and " + selection;
			}
			int count2 = db.delete("student", where, selectionArgs);
			return count2;
		default:
			throw new IllegalArgumentException("非法Uri："+uri);
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		DbUtils dbUtils = DbUtils.create(this.getContext());
		SQLiteDatabase db = dbUtils.getDatabase();
		switch (MATCHER.match(uri)) {
		case 1:// 所有条件都放在参数里
			int count = db.update("student", values, selection, selectionArgs);
			return count;
		case 2:// ID放在URI里，其他条件放在参数里
			String where = "id="+uri.getLastPathSegment();
			if (selection != null && !"".equals(selection.trim())) {
				where = where + " and " + selection;
			}
			int count2 = db.update("student", values, where, selectionArgs);
			return count2;
		default:
			throw new IllegalArgumentException("非法Uri："+uri);
		}
	}
	
	/** 返回MIME类型 */
	@Override
	public String getType(Uri uri) {
		switch (MATCHER.match(uri)) {
		case 1:// 集合类型
			return "vnd.android.cursor.dir/student";
		case 2:// 非集合类型
			return "vnd.android.cursor.item/student";
		default:
			throw new IllegalArgumentException("非法Uri："+uri);
		}
	}

}
