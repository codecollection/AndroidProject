package com.thanone.appbuy.ui;

import java.util.LinkedList;
import java.util.List;

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
import android.widget.ListView;
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
import com.thanone.appbuy.adapter.ListViewCommentmyAdapter;
import com.thanone.appbuy.bean.Comment;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.thanone.appbuy.web.ServiceResult;
import com.thanone.appbuy.web.SrComment;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;
import com.zcj.android.util.UtilDialog;
import com.zcj.android.util.UtilString;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase.Mode;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshBase.OnRefreshListener2;
import com.zcj.android.view.pulltorefreshbyhandmark.PullToRefreshListView;

/**
 * 我的消息列表
 * 
 * @author zouchongjin@sina.com
 * @data 2014年11月3日
 */
@ContentView(R.layout.layout_commentmy)
public class CommentmyActivity extends BaseActivity {

	private AppContext appContext;// 全局Context

	// 头部
	@ViewInject(R.id.main_head_title)
	private TextView main_head_title;
	@ViewInject(R.id.main_head_back)
	private ImageView main_head_back;
	@ViewInject(R.id.main_head_delete)
	private ImageView main_head_delete;

	// 消息列表
	@ViewInject(R.id.commentmy_pull_refresh_list)
	private PullToRefreshListView mPullRefreshListView;
	private LinkedList<Comment> mListItems;
	private BaseAdapter mAdapter;
	private int offset = 0;
	
	// 编辑器
	@ViewInject(R.id.project_comment)
	private LinearLayout project_comment;
	@ViewInject(R.id.project_editer)
	private EditText project_editer;
	@ViewInject(R.id.project_editer_submit)
	private Button project_editer_submit;
	private InputMethodManager imm;
	private Long targetUserID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);

		appContext = (AppContext) getApplication();
		
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		
		// 头部和底部
		main_head_title.setText("消息");
		main_head_back.setVisibility(View.VISIBLE);
		main_head_delete.setVisibility(View.VISIBLE);

		// 内容
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

	private void initDate() {
		mListItems = new LinkedList<Comment>();
		mAdapter = new ListViewCommentmyAdapter(this, mListItems, R.layout.layout_item_commentmy);
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
				final Long uid = ((Comment)lView.getItemAtPosition(position)).getUserID();// 此评论的作者ID
				final String nickname = ((Comment)lView.getItemAtPosition(position)).getNickName();// 此评论的作者昵称
				httpSetReaded(objId, position);
				UiUtil.openCommentOper(CommentmyActivity.this, objId, new UiUtil.Callback() {
					@Override
					public void doSamething() {
						// 评论
						openToComment(uid, nickname);
					}
				}, new UiUtil.Callback() {
					@Override
					public void doSamething() {
						// 删除
						httpDel(objId, position);
					}
				});
			}
		});
		mPullRefreshListView.setAdapter(mAdapter);
		mPullRefreshListView.setRefreshing(false);
	}
	
	private void httpSetReaded(Long commentID, final int position) {
		HttpUtilsHandler.send(HttpUrlUtil.URL_COMMENT_READ,
				HttpUrlUtil.comment_read(commentID), new HttpCallback() {
					@Override
					public void success(Object d, String result) {
						mListItems.get(position-1).setReaded(1);
						mAdapter.notifyDataSetChanged();
					}
				});
	}
	
	private void httpDel(Long commentID, final int position) {
		HttpUtilsHandler.send(this, HttpUrlUtil.URL_COMMENT_MYDEL,
				HttpUrlUtil.comment_mydel(commentID), new HttpCallback() {
					@Override
					public void success(Object d, String result) {
						mListItems.remove(position-1);
						mAdapter.notifyDataSetChanged();
					}
				}, true);
	}

	private void httpData(final boolean reload) {
		if (reload) {
			offset = 0;
		}
		HttpUtilsHandler.send(HttpUrlUtil.URL_COMMENT_MYLIST,
				HttpUrlUtil.comment_mylist(appContext.getLoginUserID(), AppContext.PAGESIZE, offset), new HttpCallback() {
					@Override
					public void success(Object d, String result) {
						try {
							List<Comment> nlist = ServiceResult.GSON_DT.fromJson(result, SrComment.class).getD();
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
				});
	}

	// --------------------------------------------评论----------------------------------------------
	private void openToComment(Long targetUserID, String targetUserNickname) {
		if (appContext.getLoginUser() == null) {
			UiUtil.toLogin(this);
			return;
		}
		if (targetUserID == null) {
			project_editer.setHint("");
			this.targetUserID = null;
		} else {
			project_editer.setHint("回复" + targetUserNickname);
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
			UiUtil.showToast(CommentmyActivity.this, "请输入评论内容");
			openEditor(project_editer);
		} else {
			HttpUtilsHandler.send(this, HttpUrlUtil.URL_COMMENT_ADDCOMMENT,
					HttpUrlUtil.comment_addcomment(appContext.getLoginUserID(), targetUserID, content), new HttpCallback() {
						@Override
						public void success(Object d, String result) {
							project_editer.clearFocus();
							UiUtil.showToast(CommentmyActivity.this, "发送成功");
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

	@OnClick(R.id.main_head_back)
	private void main_head_back(View v) {
		finish();
	}

	@OnClick(R.id.main_head_delete)
	private void main_head_delete(View v) {
		UtilDialog.builderAlertDialog2(this, "提示", "确定清空消息？", new UtilDialog.DialogCallback() {
			@Override
			public void doSomething_ChickOK() {
				httpClear();
			}
		});
	}
	
	private void httpClear() {
		HttpUtilsHandler.send(HttpUrlUtil.URL_COMMENT_CLEAR,
				HttpUrlUtil.comment_clear(appContext.getLoginUserID()), new HttpCallback() {
					@Override
					public void success(Object d, String result) {
						mListItems.clear();
						mAdapter.notifyDataSetChanged();
					}
				});
	}

}
