package com.zandroid.zloc.util;

public class PoiCreateResult {

	private Integer status;// 0代表成功
	private Integer id;// 新增的数据的id
	private String message;// 响应的信息
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
