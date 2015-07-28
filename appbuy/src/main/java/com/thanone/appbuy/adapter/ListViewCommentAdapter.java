package com.thanone.appbuy.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.bean.Comment;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.zcj.android.util.UtilString;
import com.zcj.android.view.roundimageview.RoundedImageView;

public class ListViewCommentAdapter extends BaseAdapter {

	private Context context;
	private List<Comment> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	private BitmapDisplayConfig config;
	private BitmapUtils bitmapUtils;
	private Long loginUserId;

	private Drawable d_praise;// 未赞
	private Drawable d_praise_checked;// 已赞
	private Drawable d_default_head_comment;// 默认头像
	private String praise_checked;// 已赞
	private String praise_uncheck;// 未赞

	static class ListItemView { // 自定义控件集合
		public RoundedImageView avatarUrl;
		public TextView nickName;
		public ImageView identity;
		public TextView praiseCount;
		public TextView createDate;
		public TextView content;
	}

	public ListViewCommentAdapter(Context context, List<Comment> data, int resource) {
		this.context = context;
		this.loginUserId = ((AppContext) context.getApplicationContext()).getLoginUserID();
		this.d_default_head_comment = context.getResources().getDrawable(R.drawable.d_default_head_comment);
		this.d_praise = context.getResources().getDrawable(R.drawable.d_praise);
		this.d_praise_checked = context.getResources().getDrawable(R.drawable.d_praise_checked);
		this.praise_checked = context.getResources().getText(R.string.praise_checked).toString();
		this.praise_uncheck = context.getResources().getText(R.string.praise_uncheck).toString();
		this.listContainer = LayoutInflater.from(context);
		this.itemViewResource = resource;
		this.listItems = data;
		config = new BitmapDisplayConfig();
		config.setLoadFailedDrawable(d_default_head_comment);
		bitmapUtils = new BitmapUtils(context);
	}

	public int getCount() {
		return listItems.size();
	}

	public Object getItem(int arg0) {
		return listItems.get(arg0);
	}

	public long getItemId(int arg0) {
		return listItems.get(arg0).getCommentID();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		// 自定义视图
		ListItemView listItemView = null;

		if (convertView == null) {
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();
			listItemView.avatarUrl = (RoundedImageView) convertView.findViewById(R.id.item_comment_avatarUrl);
			listItemView.nickName = (TextView) convertView.findViewById(R.id.item_comment_nickName);
			listItemView.identity = (ImageView) convertView.findViewById(R.id.item_comment_identity);
			listItemView.praiseCount = (TextView) convertView.findViewById(R.id.item_comment_praiseCount);
			listItemView.createDate = (TextView) convertView.findViewById(R.id.item_comment_createDate);
			listItemView.content = (TextView) convertView.findViewById(R.id.item_comment_content);

			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		final Comment news = listItems.get(position);

		listItemView.nickName.setTag(news);

		if (UtilString.isNotBlank(news.getAvatarUrl())) {
			bitmapUtils.display(listItemView.avatarUrl, news.getAvatarUrl(), config);
		} else {
			listItemView.avatarUrl.setImageResource(R.drawable.d_default_head_comment);
		}

		listItemView.nickName.setText(UtilString.isNotBlank(news.getNickName()) ? news.getNickName() : "");
		if (news.getIdentity() != null && news.getIdentity() == 1) {
			listItemView.identity.setVisibility(View.VISIBLE);
		} else {
			listItemView.identity.setVisibility(View.GONE);
		}

		if (news.getPraised() != null && news.getPraised() == 1) {
			listItemView.praiseCount.setCompoundDrawablesWithIntrinsicBounds(d_praise_checked, null, null, null);
			listItemView.praiseCount.setContentDescription(praise_checked);
		} else {
			listItemView.praiseCount.setCompoundDrawablesWithIntrinsicBounds(d_praise, null, null, null);
			listItemView.praiseCount.setContentDescription(praise_uncheck);
		}
		listItemView.praiseCount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (loginUserId == null) {
					UiUtil.toLogin(context);
					return;
				}
				
				TextView tv = (TextView) v;
				Long pc = 0L;
				try {
					pc = Long.valueOf(tv.getText().toString());
				} catch (NumberFormatException e) {
				}
				boolean checked = praise_checked.equals(tv.getContentDescription());
				if (checked) {// 取消赞
					tv.setCompoundDrawablesWithIntrinsicBounds(d_praise, null, null, null);
					tv.setContentDescription(praise_uncheck);
					tv.setText(String.valueOf((pc - 1)));
					HttpUtilsHandler.send(HttpUrlUtil.URL_COMMENT_UNFAVOUR, HttpUrlUtil.comment_unfavour(loginUserId, news.getCommentID()));
				} else {// 赞
					tv.setCompoundDrawablesWithIntrinsicBounds(d_praise_checked, null, null, null);
					tv.setContentDescription(praise_checked);
					tv.setText(String.valueOf((pc + 1)));
					HttpUtilsHandler.send(HttpUrlUtil.URL_COMMENT_FAVOUR, HttpUrlUtil.comment_favour(loginUserId, news.getCommentID()));
				}
			}
		});

		if (news.getPraiseCount() != null) {
			listItemView.praiseCount.setText(String.valueOf(news.getPraiseCount()));
		} else {
			listItemView.praiseCount.setText("0");
		}
		
		listItemView.createDate.setText(UtilString.isNotBlank(news.getCreateDate()) ? news.getCreateDate() : "");
		
		if (UtilString.isNotBlank(news.getTargetUser())) {
			listItemView.content.setText("回复@"+news.getTargetUser()+"："+news.getContent());
		} else {			
			listItemView.content.setText(news.getContent());
		}

		return convertView;
	}

}