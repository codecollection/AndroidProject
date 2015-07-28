package com.thanone.appbuy.ui;

import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.adapter.ListViewBshowAdapter;
import com.thanone.appbuy.bean.Goods;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.common.UmengUtil;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.thanone.appbuy.web.ServiceResult;
import com.thanone.appbuy.web.SrGoods;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase.Mode;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase.OnRefreshListener2;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshListView;

public class BshowActivity extends Fragment {

	private UmengUtil umeng;
	
	// 头部
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;
	@ViewInject(R.id.main_head_share)
	private ImageView main_head_share;

	// 预告
	@ViewInject(R.id.bshow_pull_refresh_list)
	private PullToRefreshListView mPullRefreshListView;
	private LinkedList<Goods> mListItems;
	private BaseAdapter mAdapter;
	private int offset = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_bshow, container, false);
	    ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// 头部和底部
		main_head_title.setText("预告");
		main_head_share.setVisibility(View.VISIBLE);
		
		umeng = new UmengUtil(getActivity());

		initDate();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		umeng.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("Bshow");
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("Bshow");
	}
	
	@OnClick(R.id.main_head_share)
	public void share(View v) {
		umeng.shareApp();
	}

	private void initDate() {
		mListItems = new LinkedList<Goods>();
		mAdapter = new ListViewBshowAdapter(getActivity(), mListItems, R.layout.layout_item_project);
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
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView lView = (ListView) parent;
				long objId = lView.getItemIdAtPosition(position);
				UiUtil.toGoods(getActivity(), objId);
			}
		});
		mPullRefreshListView.setAdapter(mAdapter);
		mPullRefreshListView.setRefreshing(false);
	}

	private void httpData(final boolean reload) {
		if (reload) { offset = 0; }
		HttpUtilsHandler.send(getActivity(), HttpUrlUtil.URL_GOODS_TOSELL, HttpUrlUtil.goods_tosell(AppContext.PAGESIZE, offset), new HttpCallback() {
			@Override
			public void success(Object d, String result) {
				try {
					List<Goods> nlist = ServiceResult.GSON_DT.fromJson(result, SrGoods.class).getD();
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
					mPullRefreshListView.onRefreshComplete();
					e.printStackTrace();
				}
			}
			@Override
			public void error() {
				mPullRefreshListView.onRefreshComplete();
			}
		}, true);
	}
	
}
