package com.thanone.zgps.adapter;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.thanone.zgps.activity.LocationFragment;
import com.thanone.zgps.bean.Location;
import com.thanone.zgps.util.UiUtil;
import com.zcj.util.UtilDate;

public class LocationFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<Location> items;
	private int mapType = 0;// 0：空界面；1：查看所有员工的界面；2：查看指定员工轨迹的界面

	public LocationFragmentPagerAdapter(FragmentManager fm, List<Location> itemList, int mapType) {
		super(fm);
		this.items = itemList;
		this.mapType = mapType;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = new LocationFragment();
		Bundle args = new Bundle();
		Location loc = items.get(position);
		args.putLong("userId", loc.getUserId());
		args.putString("userRealname", loc.getUsername()+"."+loc.getUserRealname());
		args.putString("locationTime", UtilDate.SDF_DATETIME.get().format(loc.getLocationTime()));
		args.putInt(UiUtil.MAP_TYPE_KEY, mapType);
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
