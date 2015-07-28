package com.thanone.appbuy.bean;

/**
 * 收藏
 * 
 * @author zouchongjin@sina.com
 * @data 2014年10月17日
 */
public class Favorite {

//	private Long favoriteID;
	
	private Long goodsID;
	private Long userID;
	
	private String pictureUrl;// 商品图片地址（不存数据库）

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public Long getGoodsID() {
		return goodsID;
	}

	public void setGoodsID(Long goodsID) {
		this.goodsID = goodsID;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
}