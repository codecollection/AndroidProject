package com.thanone.appbuy.ui;

import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnFocusChange;
import com.lidroid.xutils.view.annotation.event.OnKey;
import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.adapter.ListViewCommentAdapter;
import com.thanone.appbuy.adapter.ListViewShareAdapter;
import com.thanone.appbuy.bean.Comment;
import com.thanone.appbuy.bean.OptionsBean;
import com.thanone.appbuy.bean.Share;
import com.thanone.appbuy.bean.Subject;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.common.UmengUtil;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.thanone.appbuy.web.ServiceResult;
import com.thanone.appbuy.web.SrComment;
import com.thanone.appbuy.web.SrCommentObj;
import com.thanone.appbuy.web.SrShare;
import com.thanone.appbuy.web.SrSubjectObj;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;
import com.zcj.android.util.UtilMath;
import com.zcj.android.util.UtilString;
import com.zcj.android.view.ellipsizing.EllipsizingTextView;
import com.zcj.android.view.listviewforscroll.ListViewForScrollView;
import com.zcj.android.view.numberprogressbar.NumberProgressBar;
import com.zcj.android.view.numberprogressbar.NumberProgressBar.ProgressTextVisibility;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase.OnRefreshListener;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshScrollView;

@ContentView(R.layout.layout_subject)
public class SubjectActivity extends BaseActivity {

	private AppContext appContext;// 全局Context
	private BitmapUtils bitmapUtils;
	private boolean canTp = true;// 是否可以投票
	
	private Long subjectID;
	private UmengUtil umeng;
	private String shareLogo;// 图片
	private String shareTitle;// 标题
	private String shareContent;// 内容
	private String shareUrl;// 链接
	
	// 头部
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;
	@ViewInject(R.id.main_head_back)
	private ImageView main_head_back;
	
	// 滚动条
	@ViewInject(R.id.subject_scrollview)
	private PullToRefreshScrollView subject_scrollview;
	
	// 评论列表
	@ViewInject(R.id.subject_commentlayout)
	private LinearLayout subject_commentlayout;
	@ViewInject(R.id.subject_listview_pl)
	private ListViewForScrollView mListView;
	private BaseAdapter mAdapter;
	private List<Comment> mListItems = new LinkedList<Comment>();
	int offset = 0;
	@ViewInject(value=R.id.d_loadmore, parentId=R.id.d_loadmore_pl)
	private TextView more;
	
	// 分享列表
	@ViewInject(R.id.subject_sharelayout)
	private LinearLayout subject_sharelayout;
	@ViewInject(R.id.subject_listview_fx)
	private ListViewForScrollView mListView2;
	private BaseAdapter mAdapter2;
	private List<Share> mListItems2 = new LinkedList<Share>();
	int offset2 = 0;
	@ViewInject(value=R.id.d_loadmore, parentId=R.id.d_loadmore_fx)
	private TextView more2;
	
	// 编辑器
	@ViewInject(R.id.project_comment)
	private LinearLayout project_comment;
	@ViewInject(R.id.project_editer)
	private EditText project_editer;
	@ViewInject(R.id.project_editer_submit)
	private Button project_editer_submit;
	private InputMethodManager imm;
	private Long targetUserID;
	
	// 当前话题
	@ViewInject(R.id.ctopic_describe)
	private EllipsizingTextView ctopic_describe;
	@ViewInject(R.id.ctopic_imglayout)
	private LinearLayout ctopic_imglayout;
	@ViewInject(R.id.ctopic_img)
	private ImageView ctopic_img;
	@ViewInject(R.id.ctopic_wytp)
	private TextView ctopic_wytp;
	@ViewInject(R.id.ctopic_tplayout)
	private LinearLayout ctopic_tplayout;
	@ViewInject(R.id.subject_operlayout)
	private LinearLayout subject_operlayout;
	@ViewInject(R.id.subject_commentcount)
	private TextView subject_commentcount;
	@ViewInject(R.id.subject_sharecount)
	private TextView subject_sharecount;
	@ViewInject(R.id.subject_praisecount)
	private TextView subject_praisecount;
	@ViewInject(R.id.ctopic_showall)
	private TextView ctopic_showall;
	
	@ViewInject(R.id.subject_share)
	private TextView subject_share;
	@ViewInject(R.id.subject_comment)
	private TextView subject_comment;
	@ViewInject(R.id.subject_praise)
	private TextView subject_praise;
	@ViewInject(R.id.subject_praise_layout)
	private RelativeLayout subject_praise_layout;
	
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewUtils.inject(this);
		
		umeng = new UmengUtil(this);
		
		initView();
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
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
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
		
		appContext = (AppContext) getApplication();
		bitmapUtils = new BitmapUtils(this);
		subjectID = getIntent().getExtras().getLong("id");
		
		// 头部和底部
		main_head_title.setText("互动");
		main_head_back.setVisibility(View.VISIBLE);
		
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		
		subject_scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				initDate();
			}
		});
		
		mAdapter = new ListViewCommentAdapter(this, mListItems, R.layout.layout_item_comment);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListViewForScrollView lView = (ListViewForScrollView) parent;
				long objId = lView.getItemIdAtPosition(position);
				if (appContext.getLoginUser() == null) {
					UiUtil.toLogin(SubjectActivity.this);
					return;
				}
				final Long uid = ((Comment)lView.getItemAtPosition(position)).getUserID();// 此评论的作者ID
				final String nickname = ((Comment)lView.getItemAtPosition(position)).getNickName();// 此评论的作者昵称
				if (!appContext.getLoginUserID().equals(uid)) {
					UiUtil.openCommentOper(SubjectActivity.this, objId, new UiUtil.Callback() {
						@Override
						public void doSamething() {
							openToComment(uid, nickname);
						}
					}, null);
				}
			}
		});
		mListView.setAdapter(mAdapter);
		
		mAdapter2 = new ListViewShareAdapter(this, mListItems2, R.layout.layout_item_share);
		mListView2.setAdapter(mAdapter2);
	}
	
	@OnClick(R.id.main_head_back)
	private void back(View v) {
		finish();
	}
	
	@OnClick(R.id.subject_sharecount)
	private void subject_sharecount(View v) {
		subject_sharecount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.d_top);
		subject_commentcount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		subject_sharecount.setTextColor(getResources().getColor(R.color.black));
		subject_commentcount.setTextColor(getResources().getColor(R.color.gray));
		subject_commentlayout.setVisibility(View.GONE);
		subject_sharelayout.setVisibility(View.VISIBLE);
		scrollToTop();
	}
	
	@OnClick(R.id.subject_commentcount)
	private void subject_commentcount(View v) {
		subject_sharecount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		subject_commentcount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.d_top);
		subject_sharecount.setTextColor(getResources().getColor(R.color.gray));
		subject_commentcount.setTextColor(getResources().getColor(R.color.black));
		subject_commentlayout.setVisibility(View.VISIBLE);
		subject_sharelayout.setVisibility(View.GONE);
		scrollToTop();
	}
	
	@OnClick(value=R.id.d_loadmore, parentId=R.id.d_loadmore_pl)
	private void more(View v) {
		httpComment(false);
	}
	
	@OnClick(value=R.id.d_loadmore, parentId=R.id.d_loadmore_fx)
	private void more2(View v) {
		httpShare(false);
	}
	
	private void initDate() {
		httpSubject();
		httpComment(true);
		httpShare(true);
	}
	
	// 话题详情
	private void httpSubject() {
		HttpUtilsHandler.send(this, HttpUrlUtil.URL_SUBJECT_DETAIL, HttpUrlUtil.subject_detail(appContext.getLoginUserID(), subjectID), new HttpCallback() {
			@Override
			public void success(Object d, String result) {
				Subject sub = ServiceResult.GSON_DT.fromJson(result, SrSubjectObj.class).getD();
				shareLogo = sub.getShareLogo();
				shareTitle = sub.getShareTitle();
				shareUrl = sub.getShareUrl();
				shareContent = sub.getShareContent();
				showFirstSubject(sub);
				showFirstSubjectExpand(sub);
				subject_scrollview.onRefreshComplete();
			}
			@Override
			public void error() {
				subject_scrollview.onRefreshComplete();
			}
		}, true);
	}
	
	// 评论列表
	private void httpComment(final boolean reload) {
		if (reload) { offset = 0; }
		HttpUtilsHandler.send(HttpUrlUtil.URL_COMMENT_SUBJECTCOMMENT, HttpUrlUtil.comment_subjectcomment(AppContext.PAGESIZE, offset, appContext.getLoginUserID(), subjectID), new HttpCallback() {
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
	
	// 分享列表
	private void httpShare(final boolean reload) {
		if (reload) { offset2 = 0; }
		HttpUtilsHandler.send(HttpUrlUtil.URL_SHARE_SUBJECTSHARELIST, HttpUrlUtil.share_subjectsharelist(AppContext.PAGESIZE, offset, subjectID), new HttpCallback() {
			@Override
			public void success(Object d, String result) {
				try {
					List<Share> nlist = ServiceResult.GSON_DT.fromJson(result, SrShare.class).getD();
					int what = nlist.size();
					if (what > 0) {
						offset2 += what;
						if (reload) {
							mListItems2.clear();
						}
						mListItems2.addAll(nlist);
						mAdapter2.notifyDataSetChanged();
					}
					if (what == 0 && offset2 == 0) {// 没有数据
						more2.setText(getResources().getString(R.string.more_none));
					} else if (what < AppContext.PAGESIZE) {// 最后一页
						more2.setText(getResources().getString(R.string.more_nomore));
					} else {// 还有数据
						more2.setText(getResources().getString(R.string.more_hasmore));
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
		ctopic_describe.setText("【话题】"+firstSubject.getDescribe());
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
				
//				int windowWidth = UtilAndroid.getWindowWidth(this);
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
					UiUtil.addTpListener(ctopic_wytp, this, firstSubject.getSubjectID(), names, new UiUtil.Callback() {
						@Override
						public void doSamething() {
							initDate();
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
		subject_praisecount.setText("赞"+firstSubject.getPraiseCount());
		subject_commentcount.setText("评论"+firstSubject.getCommentCount());
		subject_sharecount.setText("分享"+firstSubject.getShareCount());

		if (firstSubject.getPraised() != null && firstSubject.getPraised() == 1) {
			subject_praise.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_praise_checked_big, 0, 0, 0);
			subject_praise.setContentDescription(appContext.getPraise_checked());
		} else {
			subject_praise.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_praise_big, 0, 0, 0);
			subject_praise.setContentDescription(appContext.getPraise_uncheck());
		}
		UiUtil.addPraiseListener(subject_praise_layout, subject_praise, this, firstSubject.getSubjectID(), false, R.drawable.d_praise_big, R.drawable.d_praise_checked_big);
	}
	
	// --------------------------------------------评论----------------------------------------------
	@OnClick(R.id.subject_comment_layout)
	private void subject_comment(View v) {
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
			UiUtil.showToast(this, "请输入评论内容");
			openEditor(project_editer);
		} else {
			HttpUtilsHandler.send(this, HttpUrlUtil.URL_COMMENT_ADDSUBJECTCOMMENT, HttpUrlUtil.comment_addsubjectcomment(content, appContext.getLoginUserID(), subjectID, targetUserID),
					new HttpCallback() {
						@Override
						public void success(Object d, String result) {
							try {
								Comment obj = ServiceResult.GSON_DT.fromJson(result, SrCommentObj.class).getD();
								mListItems.add(0, obj);
								mAdapter.notifyDataSetChanged();
							} catch (JsonSyntaxException e) {
								e.printStackTrace();
							}
							project_editer.clearFocus();
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
		subject_operlayout.setVisibility(View.GONE);
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
        	subject_operlayout.setVisibility(View.VISIBLE);
    	}
    }
	// --------------------------------------------------------------------------------------------------
	
    private void scrollToTop() {
		ctopic_describe.setFocusable(true);
		ctopic_describe.setFocusableInTouchMode(true);
		ctopic_describe.requestFocus();
	}
    
    @OnClick(R.id.subject_share_layout)
	private void subject_share_layout(View v) {
		umeng.share(shareLogo, shareTitle, shareContent, shareUrl, new UmengUtil.Callback() {
			@Override
			public void doSamething() {
				UiUtil.showToast(SubjectActivity.this, "分享成功");
				if (appContext.getLoginUserID() != null) {
					HttpUtilsHandler.send(HttpUrlUtil.URL_SHARE_ADDSUBJECTSHARE, HttpUrlUtil.share_addsubjectshare(appContext.getLoginUserID(), subjectID));
					httpShare(true);
				}
			}
		});
	}
}
