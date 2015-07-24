package com.thanone.zgps.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thanone.zgps.R;
import com.thanone.zgps.util.UiUtil;

public class LocationFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View contextView = inflater.inflate(R.layout.fragment_location, container, false);
		TextView mTextView = (TextView) contextView.findViewById(R.id.fragment_location_userRealname);
		TextView mTextView2 = (TextView) contextView.findViewById(R.id.fragment_location_locationTime);
		ImageView detailButton = (ImageView) contextView.findViewById(R.id.fragment_location_detail);

		Bundle mBundle = getArguments();

		mTextView.setText(mBundle.getString("userRealname"));
		mTextView2.setText(mBundle.getString("locationTime"));
		Integer mapType = mBundle.getInt(UiUtil.MAP_TYPE_KEY);
		if (mapType != null && mapType == UiUtil.MAP_TYPE_ALL) {
			final Long id = mBundle.getLong("userId");
			detailButton.setVisibility(View.VISIBLE);
			detailButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					UiUtil.toMap(getActivity(), UiUtil.MAP_TYPE_ONE, id);
				}
			});
		} else {
			detailButton.setVisibility(View.GONE);
			detailButton.setOnClickListener(null);
		}
		
		return contextView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}