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
import com.thanone.appbuy.bean.Share;
import com.zcj.android.util.UtilString;
import com.zcj.android.view.roundimageview.RoundedImageView;

/**
 * 话题的分享列表
 * @author zouchongjin@sina.com
 * @data 2014年11月5日
 */
public class ListViewShareAdapter extends BaseAdapter {

	private List<Share> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	private BitmapDisplayConfig config;
	private BitmapUtils bitmapUtils;

	private Drawable d_default_head_comment;// 默认头像

	static class ListItemView { // 自定义控件集合
		public RoundedImageView avatarUrl;
		public TextView nickName;
		public ImageView identity;
		public TextView ctime;
	}

	public ListViewShareAdapter(Context context, List<Share> data, int resource) {
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
		return listItems.get(arg0).getShareID();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		// 自定义视图
		ListItemView listItemView = null;

		if (convertView == null) {
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();
			listItemView.avatarUrl = (RoundedImageView) convertView.findViewById(R.id.item_share_avatarUrl);
			listItemView.nickName = (TextView) convertView.findViewById(R.id.item_share_nickName);
			listItemView.identity = (ImageView) convertView.findViewById(R.id.item_share_identity);
			listItemView.ctime = (TextView) convertView.findViewById(R.id.item_share_ctime);

			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		final Share news = listItems.get(position);

		listItemView.nickName.setTag(news);

		if (UtilString.isNotBlank(news.getAvatarUrl())) {
			bitmapUtils.display(listItemView.avatarUrl, news.getAvatarUrl(), config);
		}

		listItemView.nickName.setText(UtilString.isNotBlank(news.getNickName()) ? news.getNickName() : "");
		if (news.getIdentity() != null && news.getIdentity() == 1) {
			listItemView.identity.setVisibility(View.VISIBLE);
		} else {
			listItemView.identity.setVisibility(View.GONE);
		}
		listItemView.ctime.setText(UtilString.isNotBlank(news.getCtime()) ? news.getCtime() : "");
		
		return convertView;
	}

}