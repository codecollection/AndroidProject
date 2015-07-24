package com.thanone.zgps.bean;

import java.util.Date;

/**
 * 定位信息
 * 
 * @author zouchongjin@sina.com
 * @data 2015年3月27日
 */
public class Location {

	private Long id;
	private Long userId;
	private Date locationTime;
	private Double lat;
	private Double lng;
	private String address;

	private Integer states;// 状态（1：正常；0：异常）
	private String userRealname;// 用户真实姓名
	private String username;// 用户员工号

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserRealname() {
		return userRealname;
	}

	public void setUserRealname(String userRealname) {
		this.userRealname = userRealname;
	}

	public Double getLat() {
		return lat;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
	}

	public Date getLocationTime() {
		return locationTime;
	}

	public void setLocationTime(Date locationTime) {
		this.locationTime = locationTime;
	}

}
