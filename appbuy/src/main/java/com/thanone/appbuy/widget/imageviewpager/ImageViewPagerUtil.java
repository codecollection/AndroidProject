package com.thanone.appbuy.widget.imageviewpager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.thanone.appbuy.R;

public class ImageViewPagerUtil {

	private Activity context;
	private BitmapDisplayConfig config;
	private BitmapUtils bitmapUtils;

	private String[] imgurls;
	private List<View> dots;
	private LinearLayout pointlayout;
	private ViewPager viewPager;

	public ImageViewPagerUtil(Activity context, List<String> dataList, Drawable img_loading, Drawable img_error) {
		this.context = context;
		this.config = new BitmapDisplayConfig();
		config.setLoadingDrawable(img_loading);
		config.setLoadFailedDrawable(img_error);
		this.bitmapUtils = new BitmapUtils(context);
		
		if (dataList != null && dataList.size() > 0) {
			imgurls = new String[dataList.size()];
			dots = new ArrayList<View>();
			pointlayout = (LinearLayout) context.findViewById(R.id.project_imageviewpage_pointlayout);
			pointlayout.removeAllViews();
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(12, 12);
			layoutParams.setMargins(4, 0, 4, 0);
			for (int i = 0; i < dataList.size(); i++) {
				imgurls[i] = dataList.get(i);
				View dot4 = new View(context);
				if (i == 0) {
					dot4.setBackgroundResource(R.drawable.zandroid_imageviewpager_dot_focused);
				} else {
					dot4.setBackgroundResource(R.drawable.zandroid_imageviewpager_dot_normal);
				}
				dot4.setLayoutParams(layoutParams);
				dots.add(dot4);
				pointlayout.addView(dot4);
			}

			viewPager = (ViewPager) context.findViewById(R.id.project_imageviewpage_viewpager);
			viewPager.setAdapter(new MyAdapter());
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {
				private int oldPosition = 0;

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageSelected(int position) {
					dots.get(oldPosition).setBackgroundResource(R.drawable.zandroid_imageviewpager_dot_normal);
					dots.get(position).setBackgroundResource(R.drawable.zandroid_imageviewpager_dot_focused);
					oldPosition = position;
				}
			});
		}
	}
	
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imgurls.length;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			ImageView imageView = new ImageView(context);
			imageView.setLayoutParams(mParams);
			imageView.setScaleType(ScaleType.FIT_XY);
			bitmapUtils.display(imageView, imgurls[arg1], config);
			((ViewPager) arg0).addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}
}
