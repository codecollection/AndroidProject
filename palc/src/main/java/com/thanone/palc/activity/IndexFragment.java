package com.thanone.palc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.palc.MyApplication;
import com.thanone.palc.R;
import com.thanone.palc.bean.LocationBean;
import com.thanone.palc.util.HttpUrlUtil;
import com.thanone.palc.util.UiUtil;
import com.zcj.android.view.imageviewpager2.ImageViewPagerUtil;
import com.zcj.android.web.HttpCallback;
import com.zcj.android.web.HttpUtilsHandler;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends Fragment {

    private MyApplication application;
    private MainsActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_index, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        application = (MyApplication) getActivity().getApplication();
        activity = (MainsActivity) getActivity();

        List<String> dataList = new ArrayList<>();
        dataList.add("assets/img/d_index_img1.png");
        dataList.add("assets/img/d_index_img2.png");
        new ImageViewPagerUtil(this.getActivity(), dataList);
    }

    @OnClick(R.id.index_lcjwgc)
    private void lcjwgc(View v) {
        if (application.getLoginUserId() != null) {
            HttpUtilsHandler.send(HttpUrlUtil.URL_CLICKCATALOG, HttpUrlUtil.url_clickCatalog(application.getLoginUserId(), 1));
        }
        UiUtil.toLcjwgc(getActivity());
    }

    @OnClick(R.id.index_jsyb)
    private void jsyb(View v) {
        if (application.getLoginUserId() != null) {
            HttpUtilsHandler.send(HttpUrlUtil.URL_CLICKCATALOG, HttpUrlUtil.url_clickCatalog(application.getLoginUserId(), 2));
        }
        UiUtil.toJsyb(getActivity());
    }

    @OnClick(R.id.index_crj)
    private void crj(View v) {
        if (application.getLoginUserId() != null) {
            HttpUtilsHandler.send(HttpUrlUtil.URL_CLICKCATALOG, HttpUrlUtil.url_clickCatalog(application.getLoginUserId(), 4));
        }
        UiUtil.toCrj(getActivity());
    }

    @OnClick(R.id.index_ywcx)
    private void ywcx(View v) {
        if (application.getLoginUserId() != null) {
            HttpUtilsHandler.send(HttpUrlUtil.URL_CLICKCATALOG, HttpUrlUtil.url_clickCatalog(application.getLoginUserId(), 3));
        }
        UiUtil.toYwcx(getActivity());
    }

    @OnClick(R.id.index_sjhy)
    private void sjhy(View v) {
        httpHeyan(application.getPhoneId());
    }

    @OnClick(R.id.index_jfcx)
    private void jfcx(View v) {
        if (application.getLoginUserId() == null) {
            activity.toFragment(R.id.main_footer_4);
        } else {
            httpScore(application.getLoginUserId());
        }
    }

    @OnClick(R.id.index_ejsk)
    private void ejsk(View v) {
        if (application.getLoginUserId() == null) {
            activity.toFragment(R.id.main_footer_4);
        } else {
            LocationBean loc = application.getLastLocation();
            if (loc == null) {
                UiUtil.alert(getActivity(), "获取信息失败，请稍后再试");
            } else {
                httpEcard(application.getLoginUserId(), loc.getLongitude(), loc.getLatitude(), loc.getAddrStr());
            }
        }
    }

    // 验证是否已经核验过。如果失败，则弹出提示；如果成功，则转到核验页面
    private void httpHeyan(String phoneId) {
        HttpUtilsHandler.send(application, HttpUrlUtil.URL_HEYAN, HttpUrlUtil.url_heyan(phoneId), new HttpCallback() {
            @Override
            public void success(String dataJsonString) {
                UiUtil.toSjhy(getActivity());
            }
        }, true);
    }

    // 查询积分。如果失败，则弹出提示；如果成功，则弹出提示。
    private void httpScore(Long memberId) {
        HttpUtilsHandler.send(application, HttpUrlUtil.URL_SCORE, HttpUrlUtil.url_score(memberId), new HttpCallback() {
            @Override
            public void success(String dataJsonString) {
                UiUtil.alert(getActivity(), dataJsonString);
            }
        }, true);
    }

    // E居刷卡。如果失败，则弹出提示；如果成功，则弹出提示。
    private void httpEcard(Long memberId, String lng, String lat, String address) {
        HttpUtilsHandler.send(application, HttpUrlUtil.URL_ECARD, HttpUrlUtil.url_ecard(memberId, lng, lat, address), new HttpCallback() {
            @Override
            public void success(String dataJsonString) {
                UiUtil.alert(getActivity(), dataJsonString);
            }
        }, true);
    }

}
