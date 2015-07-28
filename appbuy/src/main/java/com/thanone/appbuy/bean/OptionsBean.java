package com.thanone.appbuy.bean;

/**
 * 投票属性（不存数据库）
 * 
 * @author zouchongjin@sina.com
 * @data 2014年10月17日
 */
public class OptionsBean {

	private String name;// 选项文本
	private Long count;// 票数
	private Integer voted;// 用户是否已投票（1-已投票0-未投票）
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Integer getVoted() {
		return voted;
	}
	public void setVoted(Integer voted) {
		this.voted = voted;
	}
	
}
