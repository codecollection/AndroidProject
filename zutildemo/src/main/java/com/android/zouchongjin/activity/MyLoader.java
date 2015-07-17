package com.android.zouchongjin.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

/**
 * 加载器
 * <p>
 * 1、在每个Activity和Fragment都可用
 * <p>
 * 2、实现异步加载数据
 * <p>
 * 3、监控源数据的变化，当数据发生变化的时候获取新的数据
 * <p>
 * 4、配置更改后自动重新连接，因此，不需要重新查询自己的数据
 * 
 * @author zouchongjin@sina.com
 * @data 2015年6月23日
 */
public class MyLoader extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentManager fm = getSupportFragmentManager();

		if (fm.findFragmentById(android.R.id.content) == null) {
			CursorLoaderListFragment list = new CursorLoaderListFragment();
			fm.beginTransaction().add(android.R.id.content, list).commit();
		}
	}

	public static class CursorLoaderListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

		SimpleCursorAdapter mAdapter;

		String mCurFilter;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, null, new String[] {
					Contacts.DISPLAY_NAME, Contacts.CONTACT_STATUS }, new int[] { android.R.id.text1, android.R.id.text2 }, 0);
			setListAdapter(mAdapter);

			// 可以用来管理一个或者多个Loader实例（能有一个LoaderManager.但是一个LoaderManager可以有多个Loaders）
			// 0：一个唯一的Id标识
			getLoaderManager().initLoader(0, null, this);
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i("FragmentComplexList", "Item clicked: " + id);
		}

		static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] { Contacts._ID, Contacts.DISPLAY_NAME, Contacts.CONTACT_STATUS,
				Contacts.CONTACT_PRESENCE, Contacts.PHOTO_ID, Contacts.LOOKUP_KEY, };

		// 实现你要请求的耗时操作
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Uri baseUri;
			if (mCurFilter != null) {
				baseUri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI, Uri.encode(mCurFilter));
			} else {
				baseUri = Contacts.CONTENT_URI;
			}

			String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND (" + Contacts.HAS_PHONE_NUMBER + "=1) AND ("
					+ Contacts.DISPLAY_NAME + " != '' ))";
			return new CursorLoader(getActivity(), baseUri, CONTACTS_SUMMARY_PROJECTION, select, null, Contacts.DISPLAY_NAME
					+ " COLLATE LOCALIZED ASC");
		}

		// 当异步线程操作完成之后就会从onLoadFinished返回数据
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			mAdapter.swapCursor(data);
			if (isResumed()) {
				setListShown(true);
			} else {
				setListShownNoAnimation(true);
			}
		}

		// 先前创建的loader被重置时这个方法将会被调用
		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			mAdapter.swapCursor(null);
		}

	}
}
