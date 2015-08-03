package com.zandroid.zway.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.zandroid.zlibbmap.lbs.bean.PoiListResult;
import com.zandroid.zlibbmap.lbs.bean.PoiResult;
import com.zandroid.zway.MyApplication;
import com.zandroid.zway.MyConfig;
import com.zandroid.zway.R;
import com.zandroid.zway.adapter.LocationFragmentPagerAdapter;
import com.zandroid.zway.util.LbsSaveHelp;
import com.zcj.android.util.UtilDialog;
import com.zcj.android.web.HttpUtilsHandler;

import java.util.LinkedList;
import java.util.List;

@ContentView(R.layout.layout_map)
public class MapActivity extends FragmentActivity {

    private MyApplication application;

    @ViewInject(R.id.bmapsView)
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;

    @ViewInject(R.id.pager)
    private ViewPager pager = null;
    private FragmentPagerAdapter adapter = null;

    @ViewInject(R.id.main_button_refresh)
    private ImageButton main_button_refresh;
    @ViewInject(R.id.main_button_hiddenOrShow)
    private ImageButton main_button_hiddenOrShow;

    private int checked;// 当前选中的坐标点下标，从0开始
    private List<PoiResult> items = new LinkedList<>();
    private List<LatLng> latLngs = new LinkedList<LatLng>();// 所有坐标点latlng集合
    private List<Marker> markers = new LinkedList<Marker>();// 所有坐标点marker集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyConfig.log("MapActivity--onCreate");

        super.onCreate(savedInstanceState);

        ViewUtils.inject(this);

        // 启动服务
        application = ((MyApplication) getApplication());
        application.startAll();

        // 自动检测更新
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);

        initView();
        initData();
    }

    private void initView() {
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                pointTo(Integer.valueOf(marker.getTitle()), true);
                return true;
            }
        });

        pager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                pointTo(arg0, false);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void initData() {

        clearMap();
        initDataButton(false);

        HttpUtilsHandler.getInstance().send(
                HttpRequest.HttpMethod.GET,
                LbsSaveHelp.poiListUrl(null, null, application.getPhoneId(), 0, 100),
                null, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            PoiListResult ss = LbsSaveHelp.GSON.fromJson(responseInfo.result, PoiListResult.class);
                            if (ss.getStatus() == 0) {
                                items = ss.getPois();
                                MyConfig.log("获取位置信息成功，返回的记录数：" + items.size());
                                if (items != null && items.size() > 0) {// 有定位数据
                                    initDataMap();
                                    initDataDetail();
                                    initDataButton(true);
                                }
                            } else {
                                MyConfig.log("网络连接成功，获取位置信息失败，LBS错误代码：" + ss.getStatus());
                            }
                        } catch (Exception e) {
                            MyConfig.log("网络连接成功，保存位置信息失败，返回字符串：" + responseInfo.result);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        MyConfig.log("网络错误");
                    }
                }
        );
    }

    /**
     * 清空地图的覆盖物
     */
    private void clearMap() {
        mBaiduMap.clear();
        latLngs.clear();
        markers.clear();
        checked = 0;
        pager.setVisibility(View.INVISIBLE);
    }

    private void initDataMap() {
        if (items != null && items.size() > 0) {

            // 初始化地图上的坐标点集合 markers
            for (int i = 0; i < items.size(); i++) {

                // 初始化地图上的坐标 latLngs
                double loc[] = items.get(i).getLocation();
                LatLng latLng = new LatLng(loc[1], loc[0]);
                latLngs.add(latLng);

                // 初始化地图上的标点 markers
                if (i == 0) {
                    markers.add((Marker) mBaiduMap.addOverlay(new MarkerOptions().position(latLng).icon(application.getBitmap_begin())
                            .title(String.valueOf(i))));
                } else {
                    markers.add((Marker) mBaiduMap.addOverlay(new MarkerOptions().position(latLng).icon(application.getBitmap_uncheck())
                            .title(String.valueOf(i))));
                }
            }

            // 连线
            if (latLngs != null && latLngs.size() > 1) {
                mBaiduMap.addOverlay(new PolylineOptions().color(0xFFFF0000).points(latLngs).width(3).dottedLine(true));
            }

            if (latLngs != null && latLngs.size() > 0) {
                centerTo(latLngs.get(0), 12);
            }
        }
    }

    private void initDataDetail() {
        pager.setVisibility(View.VISIBLE);
        adapter = new LocationFragmentPagerAdapter(getSupportFragmentManager(), items);
        pager.setAdapter(adapter);
    }

    private void initDataButton(boolean hasPoint) {
        main_button_refresh.setVisibility(View.VISIBLE);
        if (hasPoint) {
            buttonHiddenOrShow(true);
            main_button_hiddenOrShow.setVisibility(View.VISIBLE);
        } else {
            main_button_hiddenOrShow.setVisibility(View.GONE);
        }
    }

    private void pointTo(int index, boolean changeBottom) {
        if (changeBottom) {
            pager.setCurrentItem(index);
        }
        centerTo(latLngs.get(index), null);
        if (markers != null && markers.size() > index && items.size() > index) {

            // 取消上一个选中的状态
            if (checked == 0) {
                markers.get(checked).setIcon(application.getBitmap_begin());
            } else {
                markers.get(checked).setIcon(application.getBitmap_uncheck());
            }

            // 设置当前的选中状态
            markers.get(index).setIcon(application.getBitmap_check());

            checked = index;
        }
    }

    private void centerTo(LatLng center, Integer zoom) {
        MapStatus mMapStatus;
        if (zoom != null) {
            mMapStatus = new MapStatus.Builder().target(center).zoom(zoom).build();
        } else {
            mMapStatus = new MapStatus.Builder().target(center).build();
        }
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    /**
     * 坐标点的显示或隐藏
     */
    private void buttonHiddenOrShow(boolean show) {
        if (show) {
            main_button_hiddenOrShow.setImageResource(R.drawable.main_button_hidden);
            main_button_hiddenOrShow.setContentDescription("1");
        } else {
            main_button_hiddenOrShow.setImageResource(R.drawable.main_button_show);
            main_button_hiddenOrShow.setContentDescription("0");
        }
    }

    private void logoutDialog() {
        UtilDialog.builderAlertDialog2(this, "提示", "确定退出？", new UtilDialog.DialogCallback() {
            @Override
            public void doSomething_ChickOK() {
                finish();
            }
        });
    }

    @OnClick(R.id.main_button_refresh)
    private void main_button_refresh(View view) {
        initData();
    }

    @OnClick(R.id.main_button_hiddenOrShow)
    private void main_button_hiddenOrShow(View view) {
        if (markers != null && markers.size() > 0) {
            if ("1".equals(main_button_hiddenOrShow.getContentDescription())) {
                for (Marker m : markers) {
                    m.setVisible(false);
                }
                pager.setVisibility(View.GONE);
                buttonHiddenOrShow(false);
            } else {
                for (Marker m : markers) {
                    m.setVisible(true);
                }
                pager.setVisibility(View.VISIBLE);
                buttonHiddenOrShow(true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            logoutDialog();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
