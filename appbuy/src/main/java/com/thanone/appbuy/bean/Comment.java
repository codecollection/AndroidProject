package com.thanone.appbuy.bean;

/**
 * 评论
 * 
 * @author zouchongjin@sina.com
 * @data 2014年10月17日
 */
public class Comment {

	private Long commentID;
	
	private Long goodsID;// 商品ID
	private Long subjectID;// 话题ID
	private Long userID;// 评论用户ID
//	private Date createTime;// 评论时间
	private String content;// 评论内容
	private Long praiseCount;// 赞的数量
//	private String praiseArray;// 赞的人的ID串，用-隔开
//	private Long reportCount;// 举报的数量
//	private String reportArray;// 举报人的ID串，用-隔开
//	private Long targetUserID;// 回复目标用户ID
	private Integer readed;// 接收者是否已读（1-已读  0-未读）
//	private Integer mydel;// 接收者消息列表里是否已删除（1：是  0：否）
//	private Integer valid;// 是否正常显示（1：正常；0|null：被屏蔽）
	
	private String avatarUrl;// 用户头像地址（不存数据库）
	private String nickName;// 用户昵称（不存数据库）
	private Integer identity;// 评论者身份（不存数据库）
	private String createDate;// 友好格式的评论时间（不存数据库）
	private String targetUser;// 回复目标用户名（不存数据库）
	private Integer praised;// 是否赞过（1-是；0-否）（不存数据库）

	public Long getCommentID() {
		return commentID;
	}
	public void setCommentID(Long commentID) {
		this.commentID = commentID;
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
	public Integer getReaded() {
		return readed;
	}
	public void setReaded(Integer readed) {
		this.readed = readed;
	}
	public void setSubjectID(Long subjectID) {
		this.subjectID = subjectID;
	}
	public String getContent() {
		return content;
	}
	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getPraiseCount() {
		return praiseCount;
	}
	public void setPraiseCount(Long praiseCount) {
		this.praiseCount = praiseCount;
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
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getTargetUser() {
		return targetUser;
	}
	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}
	public Integer getPraised() {
		return praised;
	}
	public void setPraised(Integer praised) {
		this.praised = praised;
	}
	
}
