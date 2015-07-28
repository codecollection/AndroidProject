package com.thanone.appbuy.bean;


/**
 * 商品分享和话题分享
 * 
 * @author zouchongjin@sina.com
 * @data 2014年10月17日
 */
public class Share {

	public static final Integer TYPE_GOODS = 1;// 商品分享
	public static final Integer TYPE_SUBJECT = 2;// 话题分享
	
	private Long shareID;
	
	private Long userID;// 用户ID
	private Integer type;// 分享类型（1：商品分享；2：话题分享）
	private Long goodsID;// 商品ID
	private Long subjectID;// 话题ID
	
	private String pictureUrl;// 商品图片（不存数据库）
	private String describe;// 话题描述（不存数据库）
//	private Date createDate;// 话题发布时间（不存数据库）
	private String avatarUrl;// 用户头像地址（不存数据库）
	private String nickName;// 用户昵称（不存数据库）
	private Integer identity;// 分享者身份（1-管理员0-普通用户）（不存数据库）
	
	// APP专用
	private String ctime;// 分享时间
	private String createDate;// 话题发布时间
	
	public Long getShareID() {
		return shareID;
	}
	public void setShareID(Long shareID) {
		this.shareID = shareID;
	}
	public Long getUserID() {
		return userID;
	}
	public String getCtime() {
		return ctime;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getGoodsID() {
		return goodsID;
	}
	public void setGoodsID(Long goodsID) {
		this.goodsID = goodsID;
	}
	public Long getSubjectID() {
		return subjectID;
	}
	public void setSubjectID(Long subjectID) {
		this.subjectID = subjectID;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
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
	public Integer getIdentity() {
		return identity;
	}
	public void setIdentity(Integer identity) {
		this.identity = identity;
	}
	
}
