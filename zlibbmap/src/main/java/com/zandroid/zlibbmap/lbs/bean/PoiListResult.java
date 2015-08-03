package com.zandroid.zlibbmap.lbs.bean;

import java.util.List;

public class PoiListResult {

	private Integer status;// 0代表成功
	private Integer size;// 返回数据条数
	private Integer total;// 全部的数据条数,最多返回2000个结果
	private String message;// 响应的信息
	private List<PoiResult> pois;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<PoiResult> getPois() {
		return pois;
	}
	public void setPois(List<PoiResult> pois) {
		this.pois = pois;
	}
	
}
