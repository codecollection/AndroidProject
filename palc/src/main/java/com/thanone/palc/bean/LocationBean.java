package com.thanone.palc.bean;

import java.util.Date;

/**
 * 记录每次的定位数据，供其他功能快捷使用
 * 
 * @author zouchongjin@sina.com
 * @data 2015年7月7日
 */
public class LocationBean {

	private Long id;
	
	private String latitude;
	private String longitude;
	private String addrStr;
	private String time;// 位置的时间

	private String locType;// 定位类型，（61 ： GPS定位结果；...）
	private String radius;// 获取定位精度半径，单位是米
	private String operators;// 获得运营商(0:未知运营商;1:中国移动;2:中国联通;3:中国电信)

	private Date ctime;// 成功获取定位信息的时间

	public static Location coverToLocation(LocationBean bean) {
		if (bean == null) {
			return null;
		}
		Location loc = new Location();
		loc.setAddress(bean.getAddrStr());
		loc.setLatitude(bean.getLatitude());
		loc.setLongitude(bean.getLongitude());
		loc.setTime(bean.getTime());
		return loc;
	}

	public LocationBean() {
		super();
	}

	public LocationBean(String latitude, String longitude, String addrStr, String time, Date ctime) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.addrStr = addrStr;
		this.time = time;
		this.ctime = ctime;
	}

	public String getLatitude() {
		return latitude;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAddrStr() {
		return addrStr;
	}

	public void setAddrStr(String addrStr) {
		this.addrStr = addrStr;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLocType() {
		return locType;
	}

	public void setLocType(String locType) {
		this.locType = locType;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getOperators() {
		return operators;
	}

	public void setOperators(String operators) {
		this.operators = operators;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

}
