package com.thanone.zgps.bean;

import java.util.Date;

/**
 * 用户操作日志
 * 
 * @author ZCJ
 * @data 2013-4-2
 */
public class Logs {

	public static final String TYPE_LOGIN = "登录/注销";
	public static final String TYPE_GPS = "开启/关闭GPS";
	public static final String TYPE_LOCERR = "位置异常";

	public static final String CONTENT_LOGIN = "登录APP";
	public static final String CONTENT_LOGOUT = "注销APP";
	public static final String CONTENT_GPSOPEN = "开启GPS";
	public static final String CONTENT_GPSCLOSE = "关闭GPS";
	public static final String CONTENT_LOCERROR = "位置异常";

	private Long id;
	private Long userId;
	private String type;
	private String content;// 内容
	private Date ctime;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

}
