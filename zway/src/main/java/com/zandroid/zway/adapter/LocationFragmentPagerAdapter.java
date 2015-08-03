package com.zandroid.zway.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zandroid.zlibbmap.lbs.bean.PoiResult;
import com.zandroid.zway.activity.LocationFragment;

import java.util.List;

public class LocationFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<PoiResult> items;

	public LocationFragmentPagerAdapter(FragmentManager fm, List<PoiResult> itemList) {
		super(fm);
		this.items = itemList;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = new LocationFragment();
		Bundle args = new Bundle();
		PoiResult loc = items.get(position);
		args.putString("userRealname", loc.getAddress());
		args.putString("locationTime", loc.getTitle());
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return null;
	}

	@Override
	public int getCount() {
		return items.size();
	}

}
