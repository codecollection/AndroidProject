package com.thanone.palc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.thanone.palc.R;
import com.zcj.android.view.imageviewpager2.ImageViewPagerUtil;

import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_index, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<String> dataList = new ArrayList<>();
        dataList.add("assets/img/d_index_img1.png");
        dataList.add("assets/img/d_index_img2.png");
        new ImageViewPagerUtil(this.getActivity(), dataList);
    }

}
