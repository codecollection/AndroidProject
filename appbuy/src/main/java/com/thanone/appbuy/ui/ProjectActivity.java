package com.thanone.appbuy.ui;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnFocusChange;
import com.lidroid.xutils.view.annotation.event.OnKey;
import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.adapter.ListViewCommentAdapter;
import com.thanone.appbuy.bean.Comment;
import com.thanone.appbuy.bean.Goods;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.common.UmengUtil;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.thanone.appbuy.web.ServiceResult;
import com.thanone.appbuy.web.SrComment;
import com.thanone.appbuy.web.SrCommentObj;
import com.thanone.appbuy.web.SrGoodsObj;
import com.thanone.appbuy.widget.imageviewpager.ImageViewPagerUtil;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;
import com.zcj.android.util.UtilAndroid;
import com.zcj.android.util.UtilDate;
import com.zcj.android.util.UtilIntent;
import com.zcj.android.util.UtilString;
import com.zcj.android.view.listviewforscroll.ListViewForScrollView;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase.OnRefreshListener;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshScrollView;

/**
 * 商品详细页
 * 
 * @author zouchongjin@sina.com
 * @data 2014年10月11日
 */
@ContentView(R.layout.layout_project)
public class ProjectActivity extends BaseActivity {

	private AppContext appContext;// 全局Context
	private Long goodsId;
	@SuppressWarnings("unused")
	private ImageViewPagerUtil imageViewPager;
	
	private UmengUtil umeng;
	private String shareLogo;// 图片
	private String shareTitle;// 标题
	private String shareContent;// 内容
	private String shareUrl;// 链接
	private String taobaoUrl;// 淘宝链接
	
	private Timer timer;
	private long syms = -1;// 剩余秒数(变成0的时候就结束)
	private boolean canBuy = false;// 是否可购买
	private int buyType = 0;// 1：抢购；2：预告
	
	// 头部
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;
	@ViewInject(R.id.main_head_back)
	private ImageView main_head_back;
	
	// 滚动条
	@ViewInject(R.id.project_scrollview)
	private PullToRefreshScrollView project_scrollview;
	
	@ViewInject(R.id.project_imageviewpage_viewpager)
	private ViewPager project_imageviewpage_viewpager;
	
	@ViewInject(R.id.project_imgslayout)
	private FrameLayout project_imgslayout;
	@ViewInject(R.id.project_name)
	private TextView project_name;
	@ViewInject(R.id.project_price)
	private TextView project_price;
	@ViewInject(R.id.project_price2)
	private TextView project_price2;
	@ViewInject(R.id.project_content)
	private TextView project_content;
	@ViewInject(R.id.project_favorite)
	private ImageView project_favorite;
	@ViewInject(R.id.project_imageviewpage_title)
	private TextView project_imageviewpage_title;
	@ViewInject(R.id.project_buy)
	private ImageView project_buy;
	@ViewInject(R.id.project_image_none)
	private ImageView project_image_none;
	
	// 编辑器
	@ViewInject(R.id.project_comment)
	private LinearLayout project_comment;
	@ViewInject(R.id.project_editer)
	private EditText project_editer;
	@ViewInject(R.id.project_editer_submit)
	private Button project_editer_submit;
	private InputMethodManager imm;
	private Long targetUserID;
	
	@ViewInject(R.id.project_listview)
	private ListViewForScrollView mListView;
	private BaseAdapter mAdapter;
	private List<Comment> mListItems = new LinkedList<Comment>();
	int offset = 0;
	@ViewInject(R.id.d_loadmore)
	private TextView more;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		
		umeng = new UmengUtil(this);
		
		initView();
		initData();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		umeng.onActivityResult(requestCode, resultCode, data);
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
	
	private void initView() {
		appContext = (AppContext) getApplication();
		goodsId = this.getIntent().getExtras().getLong("id");
		
		main_head_title.setText("商品信息");
		
		int windowWidth = UtilAndroid.getWindowWidth(this);
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(windowWidth, (int)(windowWidth*820/1080));
		project_imgslayout.setLayoutParams(mParams);
		
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		
		project_scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				initData();
			}
		});
		
		main_head_back.setVisibility(View.VISIBLE);
		
		mAdapter = new ListViewCommentAdapter(this, mListItems, R.layout.layout_item_comment);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListViewForScrollView lView = (ListViewForScrollView) parent;
				long objId = lView.getItemIdAtPosition(position);// 此评论的ID
				if (appContext.getLoginUser() == null) {
					UiUtil.toLogin(ProjectActivity.this);
					return;
				}
				final Long uid = ((Comment)lView.getItemAtPosition(position)).getUserID();// 此评论的作者ID
				final String nickname = ((Comment)lView.getItemAtPosition(position)).getNickName();// 此评论的作者昵称
				if (!appContext.getLoginUserID().equals(uid)) {
					UiUtil.openCommentOper(ProjectActivity.this, objId, new UiUtil.Callback() {
						@Override
						public void doSamething() {
							openToComment(uid, nickname);
						}
					}, null);
				}
			}
		});
		mListView.setAdapter(mAdapter);
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (syms > 0) {
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				} else if (syms == 0) {
					timer.cancel();
					Message message = new Message();
					message.what = 0;
					handler.sendMessage(message);
				}
			}
		}, 0, 1000);
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (buyType == 1) {
					project_imageviewpage_title.setText("仅剩"+UtilDate.durationChinese(syms--));
				} else if (buyType == 2) {
					project_imageviewpage_title.setText(UtilDate.durationChinese(syms--)+"后开团");
				}
				break;
			case 0:
				if (buyType == 1) {
					project_imageviewpage_title.setText("已下架");
					project_buy.setImageDrawable(getResources().getDrawable(R.drawable.d_project_unbuy));
					canBuy = false;
				} else if (buyType == 2) {
					project_imageviewpage_title.setText("已开团");
					project_buy.setImageDrawable(getResources().getDrawable(R.drawable.d_project_buy));
					canBuy = true;
				}
				break;
			default:
				break;
			}
			
			super.handleMessage(msg);
		}
	};
	
	private void initData() {
		httpObj();// 获取商品详细内容
		httpData(true);// 获取评论信息
	}
	
	private void httpObj() {
		HttpUtilsHandler.send(this, HttpUrlUtil.URL_GOODS_DETAIL, HttpUrlUtil.goods_detail(appContext.getLoginUserID(), goodsId), new HttpCallback() {
			@Override
			public void success(Object d, String result) {
				try {
					Goods obj = ServiceResult.GSON_DT.fromJson(result, SrGoodsObj.class).getD();
					
					shareLogo = obj.getShareLogo();
					shareTitle = obj.getShareTitle();
					shareUrl = obj.getShareUrl();
					shareContent = obj.getShareContent();
					taobaoUrl = obj.getTaobaoUrl();
					
					List<String> urlList = obj.getPictureUrls();
					if (urlList != null && urlList.size() > 0) {
						Drawable img_loading = getResources().getDrawable(R.drawable.d_default_goods_loading);
						Drawable img_error = getResources().getDrawable(R.drawable.d_default_goods_warn);
						imageViewPager = new ImageViewPagerUtil(ProjectActivity.this, urlList, img_loading, img_error);
					} else {
						project_image_none.setVisibility(View.VISIBLE);
					}
					
					try {
						Date beginDate = UtilDate.SDF_DATETIME.get().parse(obj.getBeginDate());
						Date endDate = UtilDate.SDF_DATETIME.get().parse(obj.getEndDate());
						Date nowDate = UtilDate.SDF_DATETIME.get().parse(obj.getCurDate());
						if (beginDate.after(nowDate)) {// 预告
							syms = (beginDate.getTime() - nowDate.getTime())/1000;
							buyType = 2;
							project_imageviewpage_title.setText(UtilDate.durationChinese(syms)+"后开团");
							project_buy.setImageDrawable(getResources().getDrawable(R.drawable.d_project_unbuy));
							canBuy = false;
						} else if (beginDate.before(nowDate) && endDate.after(nowDate)) {// 抢购
							syms = (endDate.getTime() - nowDate.getTime())/1000;
							buyType = 1;
							project_imageviewpage_title.setText("仅剩"+UtilDate.durationChinese(syms));
							project_buy.setImageDrawable(getResources().getDrawable(R.drawable.d_project_buy));
							canBuy = true;
						} else {
							project_imageviewpage_title.setText("已下架");
							project_buy.setImageDrawable(getResources().getDrawable(R.drawable.d_project_unbuy));
							canBuy = false;
						}
					} catch (ParseException e) {
					}
					
					
					project_name.setText(obj.getName());
					project_price.setText("￥"+obj.getPriceNow());
					if (obj.getPriceAgo() != null) {
						project_price2.setText(getResources().getText(R.string.project_price2).toString()+obj.getPriceAgo());
						project_price2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
					} else {
						project_price2.setVisibility(View.GONE);
					}
					
					if (obj.getCollected() != null && obj.getCollected() == 1) {
						project_favorite.setImageDrawable(getResources().getDrawable(R.drawable.d_sc_topic_checked));
						project_favorite.setContentDescription(getResources().getText(R.string.favorite_checked));
					}
					
					project_content.setText(obj.getIntroduce());
				} catch (Exception e) {
					e.printStackTrace();
				}
				project_scrollview.onRefreshComplete();
			}
			@Override
			public void error() {
				project_scrollview.onRefreshComplete();
			}
		}, true);
	}
	
	private void httpData(final boolean reload) {
		if (reload) { offset = 0; }
		HttpUtilsHandler.send(HttpUrlUtil.URL_COMMENT_GOODSCOMMENT, HttpUrlUtil.comment_goodscomment(AppContext.PAGESIZE, offset, appContext.getLoginUserID(), goodsId), new HttpCallback() {
			@Override
			public void success(Object d, String result) {
				try {
					List<Comment> nlist = ServiceResult.GSON_DT.fromJson(result, SrComment.class).getD();
					int what = nlist.size();
					if (what > 0) {
						offset += what;
						if (reload) {
							mListItems.clear();
						}
						mListItems.addAll(nlist);
						mAdapter.notifyDataSetChanged();
					}
					if (what == 0 && offset == 0) {// 没有数据
						more.setText(getResources().getString(R.string.more_none));
					} else if (what < AppContext.PAGESIZE) {// 最后一页
						more.setText(getResources().getString(R.string.more_nomore));
					} else {// 还有数据
						more.setText(getResources().getString(R.string.more_hasmore));
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
				if (reload) {
					scrollToTop();
				}
			}
		});
	}
	
	@OnClick(R.id.d_loadmore)
	private void more(View v) {
		httpData(false);
	}
	
	@OnClick(R.id.project_buy)
	private void project_buy(View v) {
		if (canBuy && UtilString.isNotBlank(taobaoUrl)) {
			UtilIntent.startWeb(this, taobaoUrl);
		}
	}
	
	@OnClick(R.id.project_favorite)
	private void project_favorite(View v) {
		if (appContext.getLoginUser() == null) {
			UiUtil.toLogin(this);
			return;
		}
		if (project_favorite.getContentDescription().equals(getResources().getText(R.string.favorite_uncheck))) {
			project_favorite.setImageDrawable(getResources().getDrawable(R.drawable.d_sc_topic_checked));
			project_favorite.setContentDescription(getResources().getText(R.string.favorite_checked));
			HttpUtilsHandler.send(this, HttpUrlUtil.URL_FAVORITE_ADD, HttpUrlUtil.favorite_add(appContext.getLoginUserID(), goodsId),
					new HttpCallback() {
						@Override
						public void success(Object d, String result) {
							UiUtil.showToast(ProjectActivity.this, "收藏成功");
						}
			}, true);
		} else {
			project_favorite.setImageDrawable(getResources().getDrawable(R.drawable.d_sc_topic));
			project_favorite.setContentDescription(getResources().getText(R.string.favorite_uncheck));
			HttpUtilsHandler.send(this, HttpUrlUtil.URL_FAVORITE_DEL, HttpUrlUtil.favorite_del(appContext.getLoginUserID(), goodsId),
					new HttpCallback() {
						@Override
						public void success(Object d, String result) {
							UiUtil.showToast(ProjectActivity.this, "已取消收藏");
						}
			}, true);
		}
	}
	
	@OnClick(R.id.main_head_back)
	private void back(View v) {
		finish();
	}
	
	// --------------------------------------------评论----------------------------------------------
	@OnClick(R.id.project_djpl)
	private void project_djpl(View v) {
		openToComment(null, null);
	}
	private void openToComment(Long targetUserID, String targetUserNickname) {
		if (appContext.getLoginUser() == null) {
			UiUtil.toLogin(this);
			return;
		}
		if (targetUserID == null) {
			project_editer.setHint("");
			this.targetUserID = null;
		} else {
			project_editer.setHint("回复"+targetUserNickname);
			this.targetUserID = targetUserID;
		}
		openEditor(project_editer);
	}
	@OnKey(R.id.project_editer)
	private boolean editerOnKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			project_editer.clearFocus();
			return true;
		}
		return false;
	}
	@OnClick(R.id.project_editer_submit)
	private void editerSubmit(View v) {
		final String content = project_editer.getText().toString();
		if (UtilString.isBlank(content)) {
			UiUtil.showToast(ProjectActivity.this, "请输入评论内容");
			openEditor(project_editer);
		} else {
			HttpUtilsHandler.send(this, HttpUrlUtil.URL_COMMENT_ADDGOODSCOMMENT, HttpUrlUtil.comment_addgoodscomment(appContext.getLoginUserID(), goodsId, targetUserID, content),
					new HttpCallback() {
						@Override
						public void success(Object d, String result) {
							project_editer.clearFocus();
							Comment obj = ServiceResult.GSON_DT.fromJson(result, SrCommentObj.class).getD();
							mListItems.add(0, obj);
							mAdapter.notifyDataSetChanged();
						}
						@Override
						public void error() {
							project_editer.clearFocus();
						}
			}, true);
		}
	}
	private void openEditor(EditText v) {
		v.requestFocus();
		v.requestFocusFromTouch();
		project_comment.setVisibility(View.VISIBLE);
		imm.showSoftInput(v, 0);
	}
	@OnFocusChange(R.id.project_editer)
	private void editerOnFocus(View v, boolean hasFocus) {
		if (!hasFocus) {
			hideEditor(project_editer);
		}
	}
    private void hideEditor(EditText v) {
    	if (imm.isActive()) {
    		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        	v.setText("");
        	project_comment.setVisibility(View.GONE);
    	}
    }
	// --------------------------------------------------------------------------------------------------
	
    private void scrollToTop() {
    	project_imageviewpage_viewpager.setFocusable(true);
    	project_imageviewpage_viewpager.setFocusableInTouchMode(true);
    	project_imageviewpage_viewpager.requestFocus();
	}
    
    @OnClick(R.id.project_share)
	private void project_share(View v) {
    	umeng.share(shareLogo, shareTitle, shareContent, shareUrl, new UmengUtil.Callback() {
			@Override
			public void doSamething() {
				UiUtil.showToast(ProjectActivity.this, "分享成功");
				if (appContext.getLoginUserID() != null) {
					HttpUtilsHandler.send(HttpUrlUtil.URL_SHARE_ADDGOODSSHARE, HttpUrlUtil.share_addgoodsshare(appContext.getLoginUserID(), goodsId));
				}
			}
		});
	}
}
