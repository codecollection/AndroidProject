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
import com.thanone.appbuy.adapter.ListViewDmeFenxiangGoodsAdapter;
import com.thanone.appbuy.adapter.ListViewDmeFenxiangSubjectAdapter;
import com.thanone.appbuy.bean.Share;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.thanone.appbuy.web.ServiceResult;
import com.thanone.appbuy.web.SrShare;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;
import com.zcj.android.util.UtilDialog;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase.Mode;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase.OnRefreshListener2;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshListView;

@ContentView(R.layout.layout_dmefenxiang)
public class DmeFenxiangActivity extends BaseActivity {

	private AppContext appContext;// 全局Context
	private int current;// 当前tab，1：商品分享；2：互动分享
	
	// 头部
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;
	@ViewInject(R.id.main_head_delete)
	private ImageView main_head_delete;
	@ViewInject(R.id.main_head_back)
	private ImageView main_head_back;
	
	// 菜单
	@ViewInject(R.id.dmefenxiang_sp)
	private TextView dmefenxiang_sp;
	@ViewInject(R.id.dmefenxiang_hd)
	private TextView dmefenxiang_hd;

	// 商品分享
	@ViewInject(R.id.dmefenxiang_pull_refresh_list)
	private PullToRefreshListView mPullRefreshListView;
	private LinkedList<Share> mListItems;
	private BaseAdapter mAdapter;
	private int offset = 0;
	
	// 互动分享
	@ViewInject(R.id.dmefenxiang_pull_refresh_list2)
	private PullToRefreshListView mPullRefreshListView2;
	private LinkedList<Share> mListItems2;
	private BaseAdapter mAdapter2;
	private int offset2 = 0;
	
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
		UtilDialog.builderAlertDialog2(this, "提示", "确定清空分享？", new UtilDialog.DialogCallback() {
			@Override
			public void doSomething_ChickOK() {
				if (1 == current) {
					httpGoodsClear();
				} else if (2 == current) {
					httpSubjectClear();
				}
			}
		});
	}

	@OnClick(R.id.dmefenxiang_sp)
	private void dmefenxiang_sp(View v) {
		toGoods();
	}
	
	@OnClick(R.id.dmefenxiang_hd)
	private void dmefenxiang_hd(View v) {
		toSubject();
	}
	
	private void toGoods() {
		sub_nodata.setVisibility(View.GONE);
		sub_nodata_text.setText("您暂未分享过商品");
		
		current = 1;
		mPullRefreshListView.setVisibility(View.VISIBLE);
		mPullRefreshListView2.setVisibility(View.GONE);
		dmefenxiang_sp.setBackgroundColor(getResources().getColor(R.color.gray));
		dmefenxiang_hd.setBackgroundColor(getResources().getColor(R.color.white));
		
		mPullRefreshListView.setRefreshing(false);
	}
	
	private void toSubject() {
		sub_nodata.setVisibility(View.GONE);
		sub_nodata_text.setText("您暂未分享过互动");
		
		current = 2;
		mPullRefreshListView.setVisibility(View.GONE);
		mPullRefreshListView2.setVisibility(View.VISIBLE);
		dmefenxiang_sp.setBackgroundColor(getResources().getColor(R.color.white));
		dmefenxiang_hd.setBackgroundColor(getResources().getColor(R.color.gray));
		
		mPullRefreshListView2.setRefreshing(false);
	}
	
	private void initView() {
		appContext = (AppContext) getApplication();

		// 头部和底部
		main_head_title.setText("分享");
		main_head_delete.setVisibility(View.VISIBLE);
		main_head_back.setVisibility(View.VISIBLE);
		
		mListItems = new LinkedList<Share>();
		mAdapter = new ListViewDmeFenxiangGoodsAdapter(this, mListItems, R.layout.layout_item_project);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				httpGoodsData(true);
			}
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				httpGoodsData(false);
			}
		});
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				ListView lView = (ListView) parent;
				final long goodsId = lView.getItemIdAtPosition(position);
				UiUtil.openGoodsOper(DmeFenxiangActivity.this, goodsId, new UiUtil.Callback() {
					@Override
					public void doSamething() {
						httpGoodsDel(goodsId, position);
					}
				});
			}
		});
		mPullRefreshListView.setAdapter(mAdapter);
		
		mListItems2 = new LinkedList<Share>();
		mAdapter2 = new ListViewDmeFenxiangSubjectAdapter(this, mListItems2, R.layout.layout_item_topic);
		mPullRefreshListView2.setMode(Mode.BOTH);
		mPullRefreshListView2.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				httpSubjectData(true);
			}
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				httpSubjectData(false);
			}
		});
		mPullRefreshListView2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				ListView lView = (ListView) parent;
				final long subjectId = lView.getItemIdAtPosition(position);
				UiUtil.openSubjectOper(DmeFenxiangActivity.this, subjectId, new UiUtil.Callback() {
					@Override
					public void doSamething() {
						httpSubjectDel(subjectId, position);
					}
				});
			}
		});
		mPullRefreshListView2.setAdapter(mAdapter2);
	}
	
	private void initDate() {
		toGoods();
	}

	private void httpGoodsDel(Long goodsID, final int position) {
		HttpUtilsHandler.send(HttpUrlUtil.URL_SHARE_DELGOODS, HttpUrlUtil.share_delgoods(appContext.getLoginUserID(), goodsID),
				new HttpCallback() {
					@Override
					public void success(Object d, String resultJson) {
						mListItems.remove(position - 1);
						mAdapter.notifyDataSetChanged();
					}
				});
	}
	
	private void httpSubjectDel(Long subjectID, final int position) {
		HttpUtilsHandler.send(HttpUrlUtil.URL_SHARE_DELSUBJECT, HttpUrlUtil.share_delsubject(appContext.getLoginUserID(), subjectID), 
				new HttpCallback() {
					@Override
					public void success(Object d, String resultJson) {
						mListItems2.remove(position-1);
						mAdapter2.notifyDataSetChanged();
					}
				});
	}
	
	private void httpSubjectClear() {
		HttpUtilsHandler.send(HttpUrlUtil.URL_SHARE_CLEARSUBJECT, HttpUrlUtil.share_clearsubject(appContext.getLoginUserID()), 
				new HttpCallback() {
					@Override
					public void success(Object d, String resultJson) {
						mListItems2.clear();
						mAdapter2.notifyDataSetChanged();
					}
				});
	}
	
	private void httpGoodsClear() {
		HttpUtilsHandler.send(HttpUrlUtil.URL_SHARE_CLEARGOODS, HttpUrlUtil.share_cleargoods(appContext.getLoginUserID()), 
				new HttpCallback() {
					@Override
					public void success(Object d, String resultJson) {
						mListItems.clear();
						mAdapter.notifyDataSetChanged();
					}
				});
	}
	
	private void httpGoodsData(final boolean reload) {
		if (reload) { offset = 0; }
		HttpUtilsHandler.send(this, HttpUrlUtil.URL_SHARE_GOODSSHARE, HttpUrlUtil.share_goodsshare(appContext.getLoginUserID(), AppContext.PAGESIZE, offset), new HttpCallback() {
			@Override
			public void success(Object d, String resultJson) {
				try {
					List<Share> nlist = ServiceResult.GSON_DT.fromJson(resultJson, SrShare.class).getD();
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
	
	private void httpSubjectData(final boolean reload) {
		if (reload) { offset2 = 0; }
		HttpUtilsHandler.send(HttpUrlUtil.URL_SHARE_SUBJECTSHARE, HttpUrlUtil.share_subjectshare(appContext.getLoginUserID(), AppContext.PAGESIZE, offset2), new HttpCallback() {
			@Override
			public void success(Object d, String resultJson) {
				try {
					List<Share> nlist = ServiceResult.GSON_DT.fromJson(resultJson, SrShare.class).getD();
					int what = nlist.size();
					if (what >= 0) {
						offset2 += what;
						if (reload) {
							mListItems2.clear();
						}
						mListItems2.addAll(nlist);
						mAdapter2.notifyDataSetChanged();
						mPullRefreshListView2.onRefreshComplete();
						if (what < AppContext.PAGESIZE) {
							mPullRefreshListView2.setMode(Mode.PULL_FROM_START);
						} else {
							mPullRefreshListView2.setMode(Mode.BOTH);
						}
					} else {
						mPullRefreshListView2.setMode(Mode.PULL_FROM_START);
					}
				} catch (JsonSyntaxException e) {
					mPullRefreshListView2.onRefreshComplete();
					e.printStackTrace();
				}
				if (reload && offset2 == 0) {
					mPullRefreshListView2.setVisibility(View.GONE);
					sub_nodata.setVisibility(View.VISIBLE);
				} else {
					mPullRefreshListView2.setVisibility(View.VISIBLE);
					sub_nodata.setVisibility(View.GONE);
				}
			}
			@Override
			public void error() {
				mPullRefreshListView2.onRefreshComplete();
			}
		});
	}

}
