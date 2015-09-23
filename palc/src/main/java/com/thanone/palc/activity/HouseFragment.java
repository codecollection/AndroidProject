package com.thanone.palc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.palc.MyApplication;
import com.thanone.palc.MyConfig;
import com.thanone.palc.R;
import com.thanone.palc.util.UiUtil;
import com.zcj.android.util.PhotoChooseUtils;
import com.zcj.android.util.UtilImage;
import com.zcj.util.UtilString;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @ViewInject(R.id.house_et_idcard)
    private EditText house_et_idcard;
    @ViewInject(R.id.house_et_address)
    private EditText house_et_address;
    @ViewInject(R.id.house_et_indate)
    private EditText house_et_indate;
    @ViewInject(R.id.house_et_tel)
    private EditText house_et_tel;

    private Float lng;
    private Float lat;
    private File idcardFile;

    // 处理拍照上传和相册选图的回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imgPath = photoChooseUtils.onActivityResult(requestCode, resultCode, data);
        if (UtilString.isNotBlank(imgPath)) {
            house_idcardimg.setImageDrawable(UtilImage.getDrawableByFilePath(imgPath));
            house_idcardimg.setVisibility(View.VISIBLE);
            idcardFile = new File(imgPath);
        }
    }

    // 点击拍照上传按钮
    @OnClick(R.id.house_idcard)
    private void house_idcard(View v) {
        photoChooseUtils.show();
    }

    @OnClick(R.id.house_address)
    private void house_address(View v) {
        UiUtil.alert(getActivity(), "打开地图");
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

        house_et_indate.setText(new SimpleDateFormat("yyyyMMdd").format(new Date()));

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

    @OnClick(R.id.house_submit)
    private void house_submit(View v) {

        String idcardString = house_et_idcard.getText().toString();
        String addressString = house_et_address.getText().toString();
        String indateString = house_et_indate.getText().toString();
        String telString = house_et_tel.getText().toString();

        if (UtilString.isBlank(idcardString) && idcardFile == null) {
            UiUtil.alert(getActivity(), "请填写身份证号码或拍照上传");
            house_et_idcard.requestFocus();
        } else if (UtilString.isBlank(addressString)) {
            UiUtil.alert(getActivity(), "请填写详细地址");
            house_et_address.requestFocus();
        } else if (UtilString.isBlank(indateString)) {
            UiUtil.alert(getActivity(), "请填写入住日期");
            house_et_indate.requestFocus();
        } else if (UtilString.isBlank(telString)) {
            UiUtil.alert(getActivity(), "请填写手机号码");
            house_et_tel.requestFocus();
        } else {

            // TODO 详细地址转换成坐标
            lat = 28.3431F;
            lng = 129.2435F;

            if (lat == null || lng == null) {
                UiUtil.alert(getActivity(), "详细地址无法识别，请重新输入");
                house_et_address.requestFocus();
            } else {

                // TODO 如果有身份证图片，先上传图片，取得图片地址

                // TODO 提交信息

            }
        }
    }

}
