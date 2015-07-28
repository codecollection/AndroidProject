package com.thanone.appbuy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.thanone.appbuy.R;
import com.thanone.appbuy.bean.Share;
import com.zcj.android.util.UtilAndroid;
import com.zcj.android.util.UtilString;

/**
 * 用户的商品分享列表
 * @author zouchongjin@sina.com
 * @data 2014年11月5日
 */
public class ListViewDmeFenxiangGoodsAdapter extends BaseAdapter {

	private int windowWidth;
	private List<Share> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源
	private BitmapUtils bitmapUtils;
	private BitmapDisplayConfig bitmapConfig;

	static class ListItemView { // 自定义控件集合
		public ImageView img;
		public ImageView timeimg;
		public TextView time;
	}

	public ListViewDmeFenxiangGoodsAdapter(Context context, List<Share> data, int resource) {
		this.windowWidth = UtilAndroid.getWindowWidth(context);
		this.listContainer = LayoutInflater.from(context);
		this.itemViewResource = resource;
		this.listItems = data;
		bitmapUtils = new BitmapUtils(context);
		bitmapConfig = new BitmapDisplayConfig();
		bitmapConfig.setLoadingDrawable(context.getResources().getDrawable(R.drawable.d_default_goods));
		bitmapConfig.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.d_default_goods));
	}

	public int getCount() {
		return listItems.size();
	}

	public Object getItem(int arg0) {
		return listItems.get(arg0);
	}

	public long getItemId(int arg0) {
		return listItems.get(arg0).getGoodsID();
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		// 自定义视图
		ListItemView listItemView = null;

		if (convertView == null) {
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();
			listItemView.img = (ImageView) convertView.findViewById(R.id.item_project_img);
			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(windowWidth, (int)windowWidth/3);
			listItemView.img.setLayoutParams(mParams);
			listItemView.time = (TextView) convertView.findViewById(R.id.item_project_time);
			listItemView.timeimg = (ImageView) convertView.findViewById(R.id.item_project_timeimg);

			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		Share news = listItems.get(position);

		listItemView.time.setTag(news);
		listItemView.time.setVisibility(View.INVISIBLE);
		listItemView.timeimg.setVisibility(View.INVISIBLE);
		if (UtilString.isNotBlank(news.getPictureUrl())) {
			bitmapUtils.display(listItemView.img, news.getPictureUrl(), bitmapConfig);
		}
		
		return convertView;
	}
}