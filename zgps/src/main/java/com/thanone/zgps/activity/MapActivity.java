package com.thanone.zgps.activity;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.zgps.MyApplication;
import com.thanone.zgps.MyConfig;
import com.thanone.zgps.R;
import com.thanone.zgps.adapter.LocationFragmentPagerAdapter;
import com.thanone.zgps.bean.Location;
import com.thanone.zgps.bean.User;
import com.thanone.zgps.util.HttpUrlUtil;
import com.thanone.zgps.util.UiUtil;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.util.UtilAndroid;
import com.zcj.android.util.UtilAppFile;
import com.zcj.android.util.UtilDialog;
import com.zcj.android.util.UtilImage;
import com.zcj.android.web.HttpCallback;
import com.zcj.android.web.HttpUtilsHandler;
import com.zcj.android.web.ServiceResult;
import com.zcj.util.UtilDate;
import com.zcj.util.UtilString;

@ContentView(R.layout.layout_map)
public class MapActivity extends FragmentActivity {

	private MyApplication application;
	private User user;

	@ViewInject(R.id.bmapsView)
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	@ViewInject(R.id.pager)
	private ViewPager pager = null;
	private FragmentPagerAdapter adapter = null;

	@ViewInject(R.id.main_button_home)
	private ImageButton main_button_home;
	@ViewInject(R.id.main_button_refresh)
	private ImageButton main_button_refresh;
	@ViewInject(R.id.main_button_search)
	private ImageButton main_button_search;
	@ViewInject(R.id.main_button_camera)
	private ImageButton main_button_camera;
	@ViewInject(R.id.main_button_logout)
	private ImageButton main_button_logout;
	@ViewInject(R.id.main_button_hiddenOrShow)
	private ImageButton main_button_hiddenOrShow;

	private Integer mapType;
	private Long userId;
	private int checked;// 当前选中的坐标点下标，从0开始
	private List<Location> items = new LinkedList<Location>();
	private List<LatLng> latLngs = new LinkedList<LatLng>();// 所有坐标点latlng集合
	private List<Marker> markers = new LinkedList<Marker>();// 所有坐标点marker集合

	// 搜索条件
	private Date beginTime;
	private Date endTime;
	
	private static final int SPAN_INITDATE = 10 * 1000;// 10秒刷新一次
	Handler handler=new Handler();
	Runnable runnable=new Runnable() {
	    @Override
	    public void run() {
	    	boolean enabled = UtilAndroid.isGpsEnabled(application);
	    	if (enabled) {
	    		beginTime = null;
	    		endTime = null;
	    		initData();
	    	} else {
	    		UiUtil.gpsCheck(MapActivity.this);
	    	}
	    	handler.postDelayed(this, SPAN_INITDATE);
	    }
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyConfig.log("MapActivity--onCreate");

		super.onCreate(savedInstanceState);

		ViewUtils.inject(this);

		application = ((MyApplication) getApplication());
		user = application.getLoginUser();

		mapType = getIntent().getExtras().getInt(UiUtil.MAP_TYPE_KEY);
		userId = getIntent().getExtras().getLong("userId");

		initView();
		initData();
		
		handler.postDelayed(runnable, SPAN_INITDATE);
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

		// 初始化网络请求地址和参数
		String url = "";
		RequestParams params = null;
		if (mapType == UiUtil.MAP_TYPE_ALL) {
			url = HttpUrlUtil.URL_LOCATION_ALLLIST;
		} else if (mapType == UiUtil.MAP_TYPE_ONE) {
			if (endTime == null) {
				endTime = new Date();
			}
			if (beginTime == null) {
				beginTime = UtilDate.getTodayBegin();
			}
			url = HttpUrlUtil.URL_LOCATION_ONELIST;
			params = HttpUrlUtil.location_oneList(userId, beginTime, endTime);
		}

		clearMap();
		initDataButton(false);

		// 获取数据
		HttpUtilsHandler.send(this, url, params, new HttpCallback() {
			@Override
			public void success(String result) {
				try {
					items = ServiceResult.GSON_DT.fromJson(result, new TypeToken<List<Location>>() {
					}.getType());
					if (items != null && items.size() > 0) {// 有定位数据
						initDataMap();
						initDataDetail();
						initDataButton(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
					MyConfig.log("请求返回的JSON数据：" + result);
					MyConfig.log("错误信息：" + e.getMessage());
					// UiUtil.showToast(MapActivity.this, "定位数据解析出错");
				}
			}
		}, true);
	}

	/** 清空地图的覆盖物 */
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
				Location loc = items.get(i);
				LatLng latLng = new LatLng(loc.getLat(), loc.getLng());
				latLngs.add(latLng);

				// 初始化地图上的标点 markers
				if (i == 0) {
					if (mapType == UiUtil.MAP_TYPE_ONE) {
						markers.add((Marker) mBaiduMap.addOverlay(new MarkerOptions().position(latLng).icon(application.getBitmap_begin())
								.title(String.valueOf(i))));
					} else {
						markers.add((Marker) mBaiduMap.addOverlay(new MarkerOptions().position(latLng).icon(application.getBitmap_check())
								.title(String.valueOf(i))));
					}
				} else if (loc.getStates() == 1) {
					markers.add((Marker) mBaiduMap.addOverlay(new MarkerOptions().position(latLng).icon(application.getBitmap_uncheck())
							.title(String.valueOf(i))));
				} else {
					markers.add((Marker) mBaiduMap.addOverlay(new MarkerOptions().position(latLng).icon(application.getBitmap_error())
							.title(String.valueOf(i))));
				}
			}

			// 连线
			if (latLngs != null && latLngs.size() > 1 && mapType == UiUtil.MAP_TYPE_ONE) {
				mBaiduMap.addOverlay(new PolylineOptions().color(0xFFFF0000).points(latLngs).width(3).dottedLine(true));
			}

			if (latLngs != null && latLngs.size() > 0) {
				centerTo(latLngs.get(0), 12);
			}
		}
	}

	private void initDataDetail() {
		pager.setVisibility(View.VISIBLE);
		adapter = new LocationFragmentPagerAdapter(getSupportFragmentManager(), items, mapType);
		pager.setAdapter(adapter);
	}

	private void initDataButton(boolean hasPoint) {
		if (user != null && user.getRole() == User.ROLE_ADMIN && mapType == UiUtil.MAP_TYPE_ONE) {
			main_button_home.setVisibility(View.VISIBLE);
		} else {
			main_button_home.setVisibility(View.GONE);
		}

		main_button_refresh.setVisibility(View.VISIBLE);
		main_button_logout.setVisibility(View.VISIBLE);
		main_button_search.setVisibility(View.VISIBLE);

		if (hasPoint) {
			main_button_camera.setVisibility(View.VISIBLE);
			if (mapType == UiUtil.MAP_TYPE_ONE) {
				buttonHiddenOrShow(true);
				main_button_hiddenOrShow.setVisibility(View.VISIBLE);
			}
		} else {
			main_button_camera.setVisibility(View.GONE);
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
			if (checked == 0 && mapType == UiUtil.MAP_TYPE_ONE) {
				markers.get(checked).setIcon(application.getBitmap_begin());
			} else if (items.get(checked).getStates() == 1) {
				markers.get(checked).setIcon(application.getBitmap_uncheck());
			} else {
				markers.get(checked).setIcon(application.getBitmap_error());
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

	/** 坐标点的显示或隐藏 */
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
		UtilDialog.builderAlertDialog2(this, "提示", "确定退出登录？", new UtilDialog.DialogCallback() {
			@Override
			public void doSomething_ChickOK() {
				application.logout();
				finish();
			}
		});
	}

	@OnClick(R.id.main_button_refresh)
	private void main_button_refresh(View view) {
		beginTime = null;
		endTime = null;
		initData();
	}

	@OnClick(R.id.main_button_home)
	private void main_button_home(View view) {
		finish();
	}

	@OnClick(R.id.main_button_logout)
	private void main_button_logout(View view) {
		logoutDialog();
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

	@OnClick(R.id.main_button_camera)
	private void main_button_camera(View view) {
		mBaiduMap.snapshot(new SnapshotReadyCallback() {
			public void onSnapshotReady(Bitmap snapshot) {
				String filePath = MyApplication.FILESAVEPATH_SNAPSHOT;
				String fileName = UtilString.getSoleCode(new Date()) + ".jpg";
				try {
					UtilImage.saveImage(snapshot, filePath + fileName);
					UtilAppFile.scanPhoto(MapActivity.this, filePath);
					Toast.makeText(MapActivity.this, "地图截图成功，图片保存在: " + filePath + fileName, Toast.LENGTH_LONG).show();
				} catch (IOException e1) {
					e1.printStackTrace();
					Toast.makeText(MapActivity.this, "地图截图失败", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@OnClick(R.id.main_button_search)
	private void main_button_search(View view) {
		if (mapType != null && mapType == UiUtil.MAP_TYPE_ALL) {
			final EditText edit = new EditText(this);
			new AlertDialog.Builder(this).setTitle("请输入员工号查询").setIcon(android.R.drawable.ic_dialog_info).setView(edit)
					.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String val = edit.getText().toString();
							if (UtilString.isNotBlank(val)) {
								HttpUtilsHandler.send(MapActivity.this, HttpUrlUtil.URL_LOCATION_FINDUSERIDBYUSERNAME,
										HttpUrlUtil.location_findUserIdByUsername(val.trim()), new HttpCallback() {
											@Override
											public void success(String result) {
												try {
													Long userId = ServiceResult.GSON_DT.fromJson(result, Long.class);
													UiUtil.toMap(MapActivity.this, UiUtil.MAP_TYPE_ONE, userId);
												} catch (Exception e) {
													e.printStackTrace();
													UiUtil.showToast(MapActivity.this, "用户数据解析出错");
												}
											}
										}, true);
							} else {
								Toast.makeText(MapActivity.this, "请输入员工号！", Toast.LENGTH_LONG).show();
							}
						}
					}).show();
		} else if (mapType != null && mapType == UiUtil.MAP_TYPE_ONE) {
			View search = getLayoutInflater().inflate(R.layout.layout_search, null);
			final DatePicker date = (DatePicker) search.findViewById(R.id.datePicker1);
			if (beginTime != null) {
				date.updateDate(beginTime.getYear() + 1900, beginTime.getMonth(), beginTime.getDate());
			}
			final TimePicker time = (TimePicker) search.findViewById(R.id.timePicker1);
			time.setIs24HourView(true);
			if (beginTime != null) {
				time.setCurrentHour(beginTime.getHours());
				time.setCurrentMinute(beginTime.getMinutes());
			}
			final TimePicker time2 = (TimePicker) search.findViewById(R.id.timePicker2);
			time2.setIs24HourView(true);
			if (endTime != null) {
				time2.setCurrentHour(endTime.getHours());
				time2.setCurrentMinute(endTime.getMinutes());
			}

			new AlertDialog.Builder(this).setTitle("请输入时间段查询").setIcon(android.R.drawable.ic_dialog_info).setView(search)
					.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							beginTime = new Date(date.getYear() - 1900, date.getMonth(), date.getDayOfMonth(), time.getCurrentHour(), time
									.getCurrentMinute());
							endTime = new Date(date.getYear() - 1900, date.getMonth(), date.getDayOfMonth(), time2.getCurrentHour(), time2
									.getCurrentMinute());
							initData();
						}
					}).show();
		}
	}

	@Override
	protected void onStart() {
		MyConfig.log("MapActivity--onStart" + mapType);

		if (application.getLoginUser() == null) {
			finish();
		}

		super.onStart();
	}

	@Override
	protected void onResume() {
		MyConfig.log("MapActivity--onResume" + mapType);
		super.onResume();
		mMapView.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		MyConfig.log("MapActivity--onPause" + mapType);
		super.onPause();
		mMapView.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		MyConfig.log("MapActivity--onStop" + mapType);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		MyConfig.log("MapActivity--onDestroy" + mapType);
		super.onDestroy();
		handler.removeCallbacks(runnable);
		mMapView.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && (mapType == UiUtil.MAP_TYPE_ALL || user.getRole() != User.ROLE_ADMIN)) {
			logoutDialog();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
