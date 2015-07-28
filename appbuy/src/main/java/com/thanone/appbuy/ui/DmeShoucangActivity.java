package com.thanone.appbuy.ui;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.adapter.ListViewDmeShoucangAdapter;
import com.thanone.appbuy.bean.Favorite;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.thanone.appbuy.web.ServiceResult;
import com.thanone.appbuy.web.SrFavorite;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;
import com.zcj.android.util.UtilDialog;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase.Mode;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase.OnRefreshListener2;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshListView;

@ContentView(R.layout.layout_dmeshoucang)
public class DmeShoucangActivity extends BaseActivity {

	private AppContext appContext;// 全局Context
	
	// 头部
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;
	@ViewInject(R.id.main_head_delete)
	private ImageView main_head_delete;
	@ViewInject(R.id.main_head_back)
	private ImageView main_head_back;

	@ViewInject(R.id.dmeshoucang_pull_refresh_list)
	private PullToRefreshListView mPullRefreshListView;
	private LinkedList<Favorite> mListItems;
	private BaseAdapter mAdapter;
	private int offset = 0;
	
	@ViewInject(R.id.sub_nodata)
	private LinearLayout sub_nodata;
	@ViewInject(R.id.sub_nodata_text)
	private TextView sub_nodata_text;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);
		
		initView();
		initDate();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@OnClick(R.id.main_head_back)
	private void main_head_back(View v) {
		finish();
	}
	
	@OnClick(R.id.main_head_delete)
	private void main_head_delete(View v) {
		UtilDialog.builderAlertDialog2(this, "提示", "确定清空收藏？", new UtilDialog.DialogCallback() {
			@Override
			public void doSomething_ChickOK() {
				httpClear();
			}
		});
	}

	private void initView() {
		appContext = (AppContext) getApplication();

		// 头部和底部
		main_head_title.setText("收藏");
		main_head_delete.setVisibility(View.VISIBLE);
		main_head_back.setVisibility(View.VISIBLE);
		sub_nodata.setVisibility(View.GONE);
		sub_nodata_text.setText("您暂未收藏过商品");
		
		mListItems = new LinkedList<Favorite>();
		mAdapter = new ListViewDmeShoucangAdapter(this, mListItems, R.layout.layout_item_project);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				httpData(true);
			}
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				httpData(false);
			}
		});
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				ListView lView = (ListView) parent;
				final long objId = lView.getItemIdAtPosition(position);
				UiUtil.openGoodsOper(DmeShoucangActivity.this, objId, new UiUtil.Callback() {
					@Override
					public void doSamething() {
						httpDel(objId, position);
					}
				});
			}
		});
		mPullRefreshListView.setAdapter(mAdapter);
	}
	
	private void initDate() {
		mPullRefreshListView.setRefreshing(false);
	}

	private void httpDel(Long goodsID, final int position) {
		HttpUtilsHandler.send(this, HttpUrlUtil.URL_FAVORITE_DEL,
				HttpUrlUtil.favorite_del(appContext.getLoginUserID(), goodsID), new HttpCallback() {
					@Override
					public void success(Object d, String result) {
						mListItems.remove(position-1);
						mAdapter.notifyDataSetChanged();
					}
				}, true);
	}
	
	private void httpData(final boolean reload) {
		if (reload) { offset = 0; }
		HttpUtilsHandler.send(this, HttpUrlUtil.URL_FAVORITE_LIST, HttpUrlUtil.favorite_list(appContext.getLoginUserID(), AppContext.PAGESIZE, offset), new HttpCallback() {
			@Override
			public void success(Object d, String result) {
				try {
					List<Favorite> nlist = ServiceResult.GSON_DT.fromJson(result, SrFavorite.class).getD();
					int what = nlist.size();
					if (what >= 0) {
						offset += what;
						if (reload) {
							mListItems.clear();
						}
						mListItems.addAll(nlist);
						mAdapter.notifyDataSetChanged();
						mPullRefreshListView.onRefreshComplete();
						if (what < AppContext.PAGESIZE) {
							mPullRefreshListView.setMode(Mode.PULL_FROM_START);
						} else {
							mPullRefreshListView.setMode(Mode.BOTH);
						}
					} else {
						mPullRefreshListView.setMode(Mode.PULL_FROM_START);
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
					mPullRefreshListView.onRefreshComplete();
				}
				if (reload && offset == 0) {
					mPullRefreshListView.setVisibility(View.GONE);
					sub_nodata.setVisibility(View.VISIBLE);
				} else {
					mPullRefreshListView.setVisibility(View.VISIBLE);
					sub_nodata.setVisibility(View.GONE);
				}
			}
			@Override
			public void error() {
				mPullRefreshListView.onRefreshComplete();
			}
		}, true);
	}

	private void httpClear() {
		HttpUtilsHandler.send(this, HttpUrlUtil.URL_FAVORITE_CLEAR,
				HttpUrlUtil.favorite_clear(appContext.getLoginUserID()), new HttpCallback() {
					@Override
					public void success(Object d, String result) {
						mListItems.clear();
						mAdapter.notifyDataSetChanged();
					}
				}, true);
	}
}
