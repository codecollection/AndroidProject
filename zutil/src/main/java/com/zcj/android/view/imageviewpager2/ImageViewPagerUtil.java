package com.zcj.android.view.imageviewpager2;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.BitmapUtils;
import com.zcj.android.R;

import java.util.ArrayList;
import java.util.List;

public class ImageViewPagerUtil {

    private Activity context;

    private ViewPager viewPager;
    private String[] imgurls;
    private List<View> dots;
    private BitmapUtils bitmapUtils;
    private LinearLayout pointlayout;

    private ImageView.ScaleType scaleType;

    public ImageViewPagerUtil(Activity context, List<String> dataList) {
        new ImageViewPagerUtil(context, dataList, ImageView.ScaleType.CENTER_CROP);
    }

    public ImageViewPagerUtil(Activity context, List<String> dataList, ImageView.ScaleType scaleType) {
        this.context = context;
        this.scaleType = scaleType;

        bitmapUtils = new BitmapUtils(context);

        imgurls = new String[dataList.size()];
        dots = new ArrayList<View>();
        pointlayout = (LinearLayout) context.findViewById(R.id.imageviewpage_pointlayout2);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
        layoutParams.setMargins(2, 0, 2, 3);

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

        viewPager = (ViewPager) context.findViewById(R.id.imageviewpage_viewpager2);
        viewPager.setAdapter(new MyAdapter());
        viewPager.addOnPageChangeListener(new OnPageChangeListener() {
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

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgurls.length;
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(scaleType);
            bitmapUtils.display(imageView, imgurls[arg1]);
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
