package com.zandroid.zway.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zandroid.zway.R;

public class LocationFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View contextView = inflater.inflate(R.layout.fragment_location, container, false);
		TextView mTextView = (TextView) contextView.findViewById(R.id.fragment_location_userRealname);
		TextView mTextView2 = (TextView) contextView.findViewById(R.id.fragment_location_locationTime);

		Bundle mBundle = getArguments();

		mTextView.setText(mBundle.getString("userRealname"));
		mTextView2.setText(mBundle.getString("locationTime"));

		return contextView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}