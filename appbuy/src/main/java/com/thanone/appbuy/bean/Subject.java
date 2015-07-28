package com.thanone.appbuy.bean;

import java.util.List;


/**
 * 话题
 * 
 * @author zouchongjin@sina.com
 * @data 2014年10月17日
 */
public class Subject {

	private Long subjectID;
	
	private Integer type;// 类型（0-普通  1-投票）
	private String describe;// 内容描述
	private Long shareCount;// 分享数量
	private Long commentCount;// 评论数量
	private Long praiseCount;// 赞的数量
//	private String praiseArray;// 赞的用户ID集合，用-隔开
//	private Date createDate;// 创建时间
//	private Integer orders;// 排序号
//	private Integer valid;// 是否有效（1：有效；0|null：无效）
	
	// 分享信息
	private String shareLogo;// 图片
	private String shareTitle;// 标题
	private String shareContent;// 内容
	private String shareUrl;// 链接
	
	// 普通话题
	private String pictureUrl;// 图片地址
	
	// 投票话题
//	private String nameArray;// 选项字符串，用-隔开，如：春-夏-秋-冬
//	private String countArray;// 票数字符串，用-隔开，如：50-5-25-0
//	private OptionsBean[] options;// 投票属性（不存数据库）
	
	private Integer praised;// 用户是否已经赞过（1：赞过；0：未赞过）（不存数据库）

	private List<OptionsBean> options;// 投票属性（不存数据库）
	
	private String createDate;// 创建时间

	public Long getSubjectID() {
		return subjectID;
	}

	public void setSubjectID(Long subjectID) {
		this.subjectID = subjectID;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Long getShareCount() {
		return shareCount;
	}

	public String getShareLogo() {
		return shareLogo;
	}

	public void setShareLogo(String shareLogo) {
		this.shareLogo = shareLogo;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareContent() {
		return shareContent;
	}

	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public void setShareCount(Long shareCount) {
		this.shareCount = shareCount;
	}

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public Long getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(Long praiseCount) {
		this.praiseCount = praiseCount;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public List<OptionsBean> getOptions() {
		return options;
	}

	public void setOptions(List<OptionsBean> options) {
		this.options = options;
	}

	public Integer getPraised() {
		return praised;
	}

	public void setPraised(Integer praised) {
		this.praised = praised;
	}

}
