package com.thanone.appbuy.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.thanone.appbuy.R;
import com.thanone.appbuy.bean.Comment;
import com.zcj.android.util.UtilString;
import com.zcj.android.view.roundimageview.RoundedImageView;

public class ListViewCommentmyAdapter extends BaseAdapter {

	private List<Comment> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	private BitmapDisplayConfig config;
	private BitmapUtils bitmapUtils;

	private Drawable d_default_head_comment;// 默认头像

	static class ListItemView { // 自定义控件集合
		public RoundedImageView avatarUrl;
		public TextView nickName;
		public ImageView identity;
		public TextView createDate;
		public TextView content;
		public ImageView readed;
	}

	public ListViewCommentmyAdapter(Context context, List<Comment> data, int resource) {
		this.d_default_head_comment = context.getResources().getDrawable(R.drawable.d_default_head_comment);
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
			listItemView.avatarUrl = (RoundedImageView) convertView.findViewById(R.id.item_commentmy_avatarUrl);
			listItemView.nickName = (TextView) convertView.findViewById(R.id.item_commentmy_nickName);
			listItemView.identity = (ImageView) convertView.findViewById(R.id.item_commentmy_identity);
			listItemView.readed = (ImageView) convertView.findViewById(R.id.item_commentmy_readed);
			listItemView.createDate = (TextView) convertView.findViewById(R.id.item_commentmy_createDate);
			listItemView.content = (TextView) convertView.findViewById(R.id.item_commentmy_content);

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
		
		if (news.getReaded() != null && news.getReaded() == 1) {
			listItemView.readed.setVisibility(View.GONE);
		} else {
			listItemView.readed.setVisibility(View.VISIBLE);
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