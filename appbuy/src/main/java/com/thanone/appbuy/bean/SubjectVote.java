package com.thanone.appbuy.bean;

/**
 * 话题投票记录
 * 
 * @author zouchongjin@sina.com
 * @data 2014年10月17日
 */
public class SubjectVote {

	private Long subjectVoteID;
	
	private Long subjectID;// 话题ID
	private Long userID;// 投票用户
	private Integer item;// 用户投了第几项，下标从0开始
	
	public Long getSubjectVoteID() {
		return subjectVoteID;
	}
	public void setSubjectVoteID(Long subjectVoteID) {
		this.subjectVoteID = subjectVoteID;
	}
	public Long getSubjectID() {
		return subjectID;
	}
	public void setSubjectID(Long subjectID) {
		this.subjectID = subjectID;
	}
	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public Integer getItem() {
		return item;
	}
	public void setItem(Integer item) {
		this.item = item;
	}
	
}