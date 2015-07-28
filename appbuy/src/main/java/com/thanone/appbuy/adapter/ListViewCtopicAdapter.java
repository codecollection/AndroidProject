package com.thanone.appbuy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thanone.appbuy.R;
import com.thanone.appbuy.bean.Subject;

public class ListViewCtopicAdapter extends BaseAdapter {

	private List<Subject> listItems;// 数据集合
	private LayoutInflater listContainer;// 视图容器
	private int itemViewResource;// 自定义项视图源

	static class ListItemView { // 自定义控件集合
		public TextView describe;
		public TextView createDate;
	}

	public ListViewCtopicAdapter(Context context, List<Subject> data, int resource) {
		this.listContainer = LayoutInflater.from(context);
		this.itemViewResource = resource;
		this.listItems = data;
	}

	public int getCount() {
		return listItems.size();
	}

	public Object getItem(int position) {
		return listItems.get(position);
	}

	public long getItemId(int position) {
		Subject news = listItems.get(position);
		return news.getSubjectID();
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		// 自定义视图
		ListItemView listItemView = null;

		if (convertView == null) {
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();
			listItemView.describe = (TextView) convertView.findViewById(R.id.item_topic_describe);
			listItemView.createDate = (TextView) convertView.findViewById(R.id.item_topic_createDate);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		Subject news = listItems.get(position);
		
		listItemView.describe.setTag(news);
		listItemView.describe.setText(news.getDescribe());
		listItemView.createDate.setText(news.getCreateDate());
		
		return convertView;
	}
}