package com.thanone.zgps.bean;

/**
 * 用户信息
 * @author zouchongjin@sina.com
 * @data 2015年3月30日
 */
public class User {

	public static final Integer ROLE_ADMIN = 1;
	
	private Long id;
	private String username;
	private String password;
	private Integer role;// 角色（1：超级管理员；2：普通用户）
	private String realname;// 真实姓名
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
