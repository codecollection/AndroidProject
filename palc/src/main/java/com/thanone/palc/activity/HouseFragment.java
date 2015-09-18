package com.thanone.palc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.palc.MyApplication;
import com.thanone.palc.MyConfig;
import com.thanone.palc.R;
import com.zcj.android.util.PhotoChooseUtils;
import com.zcj.android.util.UtilImage;
import com.zcj.util.UtilString;

public class HouseFragment extends Fragment {

    private MyApplication application;
    private MainsActivity activity;

    private PhotoChooseUtils photoChooseUtils;

    @ViewInject(R.id.header_title)
    private TextView header_title;
    @ViewInject(R.id.header_back)
    private ImageView header_back;

    @ViewInject(R.id.house_idcardimg)
    private ImageView house_idcardimg;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imgPath = photoChooseUtils.onActivityResult(requestCode, data);
        if (UtilString.isNotBlank(imgPath)) {
            house_idcardimg.setVisibility(View.VISIBLE);
            house_idcardimg.setImageDrawable(UtilImage.getDrawableByFilePath(imgPath));
        }
    }

    @OnClick(R.id.house_idcard)
    private void house_idcard(View v) {
        photoChooseUtils.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_house, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        application = (MyApplication) getActivity().getApplication();
        activity = (MainsActivity) getActivity();

        header_title.setText("出租房登记");
        header_back.setVisibility(View.GONE);

        photoChooseUtils = new PhotoChooseUtils(getActivity(), this, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            MyConfig.log("HouseFragment onHiddenChanged 隐藏了");
        } else {
            MyConfig.log("HouseFragment onHiddenChanged 显示了");
        }
    }

}
