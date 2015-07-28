package com.thanone.appbuy.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.adapter.ListViewCtopicAdapter;
import com.thanone.appbuy.bean.OptionsBean;
import com.thanone.appbuy.bean.Subject;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.common.UmengUtil;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.thanone.appbuy.web.ServiceResult;
import com.thanone.appbuy.web.SrSubject;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.util.UtilMath;
import com.zcj.android.util.UtilString;
import com.zcj.android.view.ellipsizing.EllipsizingTextView;
import com.zcj.android.view.listviewforscroll.ListViewForScrollView;
import com.zcj.android.view.numberprogressbar.NumberProgressBar;
import com.zcj.android.view.numberprogressbar.NumberProgressBar.ProgressTextVisibility;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase.OnRefreshListener;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshScrollView;

public class CtopicActivity extends Fragment {

	private AppContext appContext;// 全局Context
	private BitmapUtils bitmapUtils;
	private boolean canTp = true;// 是否可以投票
	private Timer unreadTimer;
	
	private Long subjectID;
	private UmengUtil umeng;
	private String shareLogo;// 图片
	private String shareTitle;// 标题
	private String shareContent;// 内容
	private String shareUrl;// 链接
	
	// 头部
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;
	@ViewInject(R.id.main_head_unread)
	private ImageView main_head_unread;
	
	// 滚动条
	@ViewInject(R.id.ctopic_scrollview)
	private PullToRefreshScrollView ctopic_scrollview;
	
	// 以往话题
	@ViewInject(R.id.ctopic_listview)
	private ListViewForScrollView mListView;
	private BaseAdapter mAdapter;
	private List<Subject> mListItems = new LinkedList<Subject>();
	int offset = 0;
	@ViewInject(R.id.d_loadmore)
	private TextView more;
	
	// 当前话题
	@ViewInject(R.id.ctopic_describe)
	private EllipsizingTextView ctopic_describe;
	@ViewInject(R.id.ctopic_imglayout)
	private LinearLayout ctopic_imglayout;// 图片
	@ViewInject(R.id.ctopic_img)
	private ImageView ctopic_img;
	@ViewInject(R.id.ctopic_wytp)
	private TextView ctopic_wytp;
	@ViewInject(R.id.ctopic_tplayout)
	private LinearLayout ctopic_tplayout;// 投票
	@ViewInject(R.id.ctopic_tv_share)
	private TextView ctopic_tv_share;
	@ViewInject(R.id.ctopic_tv_comment)
	private TextView ctopic_tv_comment;
	@ViewInject(R.id.ctopic_tv_praise)
	private TextView ctopic_tv_praise;
	@ViewInject(R.id.ctopic_tv_praise_layout)
	private RelativeLayout ctopic_tv_praise_layout;
	@ViewInject(R.id.ctopic_showall)
	private TextView ctopic_showall;
	
	// 当前投票话题
	@ViewInject(R.id.ctopic_tp_1)
	private LinearLayout ctopic_tp_1;
	@ViewInject(R.id.ctopic_tp_1_text)
	private TextView ctopic_tp_1_text;
	@ViewInject(R.id.ctopic_tp_1_bar)
	private NumberProgressBar ctopic_tp_1_bar;
	@ViewInject(R.id.ctopic_tp_2)
	private LinearLayout ctopic_tp_2;
	@ViewInject(R.id.ctopic_tp_2_text)
	private TextView ctopic_tp_2_text;
	@ViewInject(R.id.ctopic_tp_2_bar)
	private NumberProgressBar ctopic_tp_2_bar;
	@ViewInject(R.id.ctopic_tp_3)
	private LinearLayout ctopic_tp_3;
	@ViewInject(R.id.ctopic_tp_3_text)
	private TextView ctopic_tp_3_text;
	@ViewInject(R.id.ctopic_tp_3_bar)
	private NumberProgressBar ctopic_tp_3_bar;
	@ViewInject(R.id.ctopic_tp_4)
	private LinearLayout ctopic_tp_4;
	@ViewInject(R.id.ctopic_tp_4_text)
	private TextView ctopic_tp_4_text;
	@ViewInject(R.id.ctopic_tp_4_bar)
	private NumberProgressBar ctopic_tp_4_bar;
	@ViewInject(R.id.ctopic_tp_5)
	private LinearLayout ctopic_tp_5;
	@ViewInject(R.id.ctopic_tp_5_text)
	private TextView ctopic_tp_5_text;
	@ViewInject(R.id.ctopic_tp_5_bar)
	private NumberProgressBar ctopic_tp_5_bar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_ctopic, container, false);
	    ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		umeng = new UmengUtil(getActivity());
		
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
	    MobclickAgent.onPageStart("Ctopic");
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("Ctopic");
	}
	
	private void initView() {
		
		ctopic_imglayout.setVisibility(View.GONE);
		ctopic_tplayout.setVisibility(View.GONE);
		ctopic_tp_1.setVisibility(View.GONE);
		ctopic_tp_2.setVisibility(View.GONE);
		ctopic_tp_3.setVisibility(View.GONE);
		ctopic_tp_4.setVisibility(View.GONE);
		ctopic_tp_5.setVisibility(View.GONE);
		ctopic_showall.setVisibility(View.GONE);
		
		appContext = (AppContext) getActivity().getApplication();
		bitmapUtils = new BitmapUtils(getActivity());
		
		ctopic_scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				initData();
			}
		});
		
		main_head_title.setText("消息");
		main_head_unread.setVisibility(View.GONE);
		
		mAdapter = new ListViewCtopicAdapter(getActivity(), mListItems, R.layout.layout_item_topic);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListViewForScrollView lView = (ListViewForScrollView) parent;
				long objId = lView.getItemIdAtPosition(position);
				UiUtil.toSubject(getActivity(), objId);
			}
		});
		mListView.setAdapter(mAdapter);
		
		unreadTimer = new Timer();
		unreadTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				if (appContext.getUnread() != null && appContext.getUnread().booleanValue()) {
					message.what = 1;
				} else {
					message.what = 0;
				}
				handler.sendMessage(message);
			}
		}, 100, 1000);
	}
	
	@OnClick(R.id.d_loadmore)
	private void more(View v) {
		httpData(false);
	}
	
	private void initData() {
		httpData(true);
	}
	
	private void httpData(final boolean reload) {
		if (reload) {
			offset = 0;
		}
		HttpUtilsHandler.send(getActivity(), HttpUrlUtil.URL_SUBJECT_LIST,
				HttpUrlUtil.subject_list(AppContext.PAGESIZE, offset, appContext.getLoginUserID()), new HttpCallback() {
					@Override
					public void success(Object d, String result) {
						try {
							List<Subject> nlist = ServiceResult.GSON_DT.fromJson(result, SrSubject.class).getD();
							int what = nlist.size();
							if (what > 0) {

								if (reload) {
									Subject firstSubject = nlist.remove(0);// 当前互动
									subjectID = firstSubject.getSubjectID();
									shareLogo = firstSubject.getShareLogo();
									shareTitle = firstSubject.getShareTitle();
									shareUrl = firstSubject.getShareUrl();
									shareContent = firstSubject.getShareContent();
									showFirstSubject(firstSubject);
									showFirstSubjectExpand(firstSubject);
								}

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

							if (reload) {
								scrollToTop();
							}
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						}

						ctopic_scrollview.onRefreshComplete();
					}

					@Override
					public void error() {
						ctopic_scrollview.onRefreshComplete();
					}
				}, true);
	}
	
	private boolean ctopic_describe_open = false;
	@OnClick(R.id.ctopic_showall)
	private void ctopic_showall(View v) {
		if (ctopic_describe_open) {
			ctopic_describe.setMaxLines(4);
			ctopic_showall.setText(getResources().getString(R.string.showall));
			ctopic_describe_open = false;
		} else {
			ctopic_describe.setMaxLines(100);
			ctopic_showall.setText(getResources().getString(R.string.showonly));
			ctopic_describe_open = true;
		}
	}
	
	/** 加载当前互动的内容 */
	private void showFirstSubject(final Subject firstSubject) {
		ctopic_describe.setText(firstSubject.getDescribe());
		if (ctopic_describe.getLineCount() > 4) {
			ctopic_showall.setVisibility(View.VISIBLE);
			ctopic_describe.setMaxLines(4);
			ctopic_showall.setText(getResources().getString(R.string.showall));
			ctopic_describe_open = false;
		} else {
			ctopic_showall.setVisibility(View.GONE);
		}
		if (firstSubject.getType() == 0) {// 图文话题
			ctopic_tplayout.setVisibility(View.GONE);
			if (UtilString.isNotBlank(firstSubject.getPictureUrl())) {									
				ctopic_imglayout.setVisibility(View.VISIBLE);
				
//				int windowWidth = UtilAndroid.getWindowWidth(getActivity());
//				LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(windowWidth, (int)windowWidth/2);
//				ctopic_img.setLayoutParams(mParams);
				
				bitmapUtils.display(ctopic_img, firstSubject.getPictureUrl());
			} else {
				ctopic_imglayout.setVisibility(View.GONE);
			}
		} else {// 投票话题
			ctopic_imglayout.setVisibility(View.GONE);
			ctopic_tplayout.setVisibility(View.VISIBLE);
			
			List<OptionsBean> optionList = firstSubject.getOptions();
			if (optionList != null && optionList.size() > 0) {
				final String[] names = new String[optionList.size()];
				Long[] counts = new Long[optionList.size()];
				Integer already = null;
				Long allCounts = 0L;// 共多少人投票
				for (int i = 0; i < optionList.size(); i++) {
					names[i] = optionList.get(i).getName();
					counts[i] = optionList.get(i).getCount();
					allCounts += optionList.get(i).getCount();
					if (optionList.get(i).getVoted() != null && optionList.get(i).getVoted() == 1) {
						already = i;
						canTp = false;
					}
				}
				if (optionList.size() > 0) {
					ctopic_tp_1.setVisibility(View.VISIBLE);
					ctopic_tp_1_text.setCompoundDrawablesWithIntrinsicBounds((already != null && 0 == already)? R.drawable.d_diamonds:R.drawable.d_diamonds_uncheck, 0, 0, 0);
					if (allCounts == 0) {
						ctopic_tp_1_text.setText(names[0] + "【0票】");
						ctopic_tp_1_bar.setProgress(0);
					} else {
						ctopic_tp_1_text.setText(names[0] + "【" + counts[0] + "票"+UtilMath.percent(counts[0],allCounts)+"】");
						ctopic_tp_1_bar.setProgress((int)(counts[0]*100/allCounts));
					}
					ctopic_tp_1_bar.setProgressTextVisibility(ProgressTextVisibility.Invisible);
				}
				if (optionList.size() > 1) {
					ctopic_tp_2.setVisibility(View.VISIBLE);
					ctopic_tp_2_text.setCompoundDrawablesWithIntrinsicBounds((already != null && 1 == already)? R.drawable.d_diamonds:R.drawable.d_diamonds_uncheck, 0, 0, 0);
					if (allCounts == 0) {
						ctopic_tp_2_text.setText(names[1] + "【0票】");
						ctopic_tp_2_bar.setProgress(0);
					} else {
						ctopic_tp_2_text.setText(names[1] + "【" + counts[1] + "票"+UtilMath.percent(counts[1],allCounts)+"】");
						ctopic_tp_2_bar.setProgress((int)(counts[1]*100/allCounts));
					}
					ctopic_tp_2_bar.setProgressTextVisibility(ProgressTextVisibility.Invisible);
				}
				if (optionList.size() > 2) {
					ctopic_tp_3.setVisibility(View.VISIBLE);
					ctopic_tp_3_text.setCompoundDrawablesWithIntrinsicBounds((already != null && 2 == already)? R.drawable.d_diamonds:R.drawable.d_diamonds_uncheck, 0, 0, 0);
					if (allCounts == 0) {
						ctopic_tp_3_text.setText(names[2] + "【0票】");
						ctopic_tp_3_bar.setProgress(0);
					} else {
						ctopic_tp_3_text.setText(names[2] + "【" + counts[2] + "票"+UtilMath.percent(counts[2],allCounts)+"】");						
						ctopic_tp_3_bar.setProgress((int)(counts[2]*100/allCounts));
					}
					ctopic_tp_3_bar.setProgressTextVisibility(ProgressTextVisibility.Invisible);
				}
				if (optionList.size() > 3) {
					ctopic_tp_4.setVisibility(View.VISIBLE);
					ctopic_tp_4_text.setCompoundDrawablesWithIntrinsicBounds((already != null && 3 == already)? R.drawable.d_diamonds:R.drawable.d_diamonds_uncheck, 0, 0, 0);
					if (allCounts == 0) {
						ctopic_tp_4_text.setText(names[3] + "【0票】");
						ctopic_tp_4_bar.setProgress(0);
					} else {
						ctopic_tp_4_text.setText(names[3] + "【" + counts[3] + "票"+UtilMath.percent(counts[3],allCounts)+"】");						
						ctopic_tp_4_bar.setProgress((int)(counts[3]*100/allCounts));
					}
					ctopic_tp_4_bar.setProgressTextVisibility(ProgressTextVisibility.Invisible);
				}
				if (optionList.size() > 4) {
					ctopic_tp_5.setVisibility(View.VISIBLE);
					ctopic_tp_5_text.setCompoundDrawablesWithIntrinsicBounds((already != null && 4 == already)? R.drawable.d_diamonds:R.drawable.d_diamonds_uncheck, 0, 0, 0);
					if (allCounts == 0) {
						ctopic_tp_5_text.setText(names[4] + "【0票】");
						ctopic_tp_5_bar.setProgress(0);
					} else {
						ctopic_tp_5_text.setText(names[4] + "【" + counts[4] + "票"+UtilMath.percent(counts[4],allCounts)+"】");						
						ctopic_tp_5_bar.setProgress((int)(counts[4]*100/allCounts));
					}
					ctopic_tp_5_bar.setProgressTextVisibility(ProgressTextVisibility.Invisible);
				}
				if (canTp) {
					UiUtil.addTpListener(ctopic_wytp, getActivity(), firstSubject.getSubjectID(), names, new UiUtil.Callback() {
						@Override
						public void doSamething() {
							initData();
						}
					});
				} else {
					ctopic_wytp.setOnClickListener(null);
					ctopic_wytp.setText("您已经投过票了");
				}
			}
		}
	}

	/** 绑定当前互动的事件和加载转发等数据 */
	private void showFirstSubjectExpand(final Subject firstSubject) {
		ctopic_describe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UiUtil.toSubject(getActivity(), firstSubject.getSubjectID());
			}
		});
		ctopic_tv_share.setText(String.valueOf(firstSubject.getShareCount()));
		ctopic_tv_comment.setText(String.valueOf(firstSubject.getCommentCount()));
		ctopic_tv_praise.setText(String.valueOf(firstSubject.getPraiseCount()));
		if (firstSubject.getPraised() != null && firstSubject.getPraised() == 1) {
			ctopic_tv_praise.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_praise_checked, 0, 0, 0);
			ctopic_tv_praise.setContentDescription(appContext.getPraise_checked());
		} else {
			ctopic_tv_praise.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_praise, 0, 0, 0);
			ctopic_tv_praise.setContentDescription(appContext.getPraise_uncheck());
		}
		UiUtil.addPraiseListener(ctopic_tv_praise_layout, ctopic_tv_praise, getActivity(), firstSubject.getSubjectID(), true);
	}
	
	@OnClick(R.id.main_head_title)
	private void main_head_title(View v) {
		if (appContext.getLoginUser() == null) {
			UiUtil.toLogin(getActivity());
		} else {
			UiUtil.toCommentmy(getActivity());
		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				main_head_unread.setVisibility(View.VISIBLE);
				break;
			case 0:
				main_head_unread.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	private void scrollToTop() {
		ctopic_describe.setFocusable(true);
		ctopic_describe.setFocusableInTouchMode(true);
		ctopic_describe.requestFocus();
	}
	
	@OnClick(R.id.ctopic_tv_share_layout)
	private void ctopic_tv_share_layout(View v) {
		umeng.share(shareLogo, shareTitle, shareContent, shareUrl, new UmengUtil.Callback() {
			@Override
			public void doSamething() {
				UiUtil.showToast(getActivity(), "分享成功");
				if (appContext.getLoginUserID() != null) {
					HttpUtilsHandler.send(HttpUrlUtil.URL_SHARE_ADDSUBJECTSHARE, HttpUrlUtil.share_addsubjectshare(appContext.getLoginUserID(), subjectID));
				}
			}
		});
	}
	
	@OnClick(R.id.ctopic_tv_comment_layout)
	private void ctopic_tv_comment_layout(View v) {
		if (subjectID != null) {			
			UiUtil.toSubject(getActivity(), subjectID);
		}
	}

}
