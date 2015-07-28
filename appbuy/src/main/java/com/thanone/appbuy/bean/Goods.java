package com.thanone.appbuy.bean;

import java.util.List;


/**
 * 商品
 * 
 * @author ZCJ
 * @data 2013-4-2
 */
public class Goods {

	private Long goodsID;
	private String name;// 商品名称
	private Float priceAgo;// 原价
	private Float priceNow;// 现价
	private String pictureUrl;// 单张图片地址
//	private String pictureUrlss;// 多张图片地址，用逗号隔开
	private String introduce;// 商品介绍
//	private Date beginDate;// 售卖开始时间
//	private Date endDate;// 售卖结束时间
//	private Integer orders;// 排序号
//	private Integer valid;// 是否有效（1：有效；0|null：无效）
	
	private String remainTime;// 剩余时间（不存数据库）
	private Integer collected;// 用户是否已收藏（1：已收藏；0|其他：未收藏）（不存数据库）
//	private Date curDate;// 系统当前时间（不存数据库）
//	private String[] pictureUrls;// 多张图片集合（不存数据库）
	private String taobaoUrl;// 淘宝链接
	
	// 分享信息
	private String shareLogo;// 图片
	private String shareTitle;// 标题
	private String shareContent;// 内容
	private String shareUrl;// 链接
	
	// APP
	private List<String> pictureUrls;// 多张图片集合（不存数据库）
	private String curDate;// 系统当前时间（不存数据库）
	private String beginDate;// 售卖开始时间
	private String endDate;// 售卖结束时间
	
	public Long getGoodsID() {
		return goodsID;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(String remainTime) {
		this.remainTime = remainTime;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getName() {
		return name;
	}

	public String getShareLogo() {
		return shareLogo;
	}

	public String getTaobaoUrl() {
		return taobaoUrl;
	}

	public void setTaobaoUrl(String taobaoUrl) {
		this.taobaoUrl = taobaoUrl;
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

	public void setName(String name) {
		this.name = name;
	}

	public Float getPriceAgo() {
		return priceAgo;
	}

	public void setPriceAgo(Float priceAgo) {
		this.priceAgo = priceAgo;
	}

	public Float getPriceNow() {
		return priceNow;
	}

	public void setPriceNow(Float priceNow) {
		this.priceNow = priceNow;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public Integer getCollected() {
		return collected;
	}

	public void setCollected(Integer collected) {
		this.collected = collected;
	}

	public String getCurDate() {
		return curDate;
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}

	public List<String> getPictureUrls() {
		return pictureUrls;
	}

	public void setPictureUrls(List<String> pictureUrls) {
		this.pictureUrls = pictureUrls;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setGoodsID(Long goodsID) {
		this.goodsID = goodsID;
	}
	
}
