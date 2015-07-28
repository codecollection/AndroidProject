package com.thanone.appbuy.bean;

public class User {

	public static final Long USERID_ADMIN = 100000000000000L;

	private Long userID;
	private String userName;
	private String passWord;
	private String avatarUrl;// 头像地址
	private String nickName;// 昵称
	private String mailBox;// 邮箱
	private String introduce;// 推荐人账号
	private Integer valid;// 是否有效（1：有效；0|null：无效）
	private Integer identity;// 身份（1-管理员0-普通用户）

	public Long getUserID() {
		return userID;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public Integer getIdentity() {
		return identity;
	}

	public void setIdentity(Integer identity) {
		this.identity = identity;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMailBox() {
		return mailBox;
	}

	public void setMailBox(String mailBox) {
		this.mailBox = mailBox;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

}
