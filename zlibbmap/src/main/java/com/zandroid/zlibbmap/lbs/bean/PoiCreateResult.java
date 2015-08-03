package com.zandroid.zlibbmap.lbs.bean;

public class PoiCreateResult {

	private Integer status;// 0代表成功
	private Long id;// 新增的数据的id
	private String message;// 响应的信息
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
