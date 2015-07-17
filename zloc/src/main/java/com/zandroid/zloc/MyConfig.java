package com.zandroid.zloc;

public class MyConfig {

	/** 百度地图定位 */
	public static final String LOCATION_AK = "Yy6VqHqRRaULM9LXAL9Bzff9";// 移动端
	public static final String LBS_AK = "566b7f727ea9fe87503114c0a479a724";// 服务端
	public static final int SCANSPAN_BEGIN = 60*1000;// 定位成功前：60秒请求定位1次
	public static final int SCANSPAN_UPDATE = 5*60*1000;// 定位成功后：5分钟更新定位1次
	public static final int LBS_TABLE_ID = 41690;
	public static final int LBS_COORD_TYPE = 3;
	public static final boolean OPEN_GPS = false;// 是否控制手机打开GPS
	public static final boolean OPEN_WAKELOCK = false;// 是否开启手机电源锁，开启会影响耗电
	
	public static final boolean DEBUG = true;
	public static final String TAG = "zloc";
}
