package com.thanone.palc.bean;

import java.util.ArrayList;
import java.util.List;

import com.zcj.android.util.bean.InternetBean;

/**
 * 手机浏览器信息
 * 
 * @author zouchongjin@sina.com
 * @data 2015年6月12日
 */
public class Internet {

	private Long id;

	private String deviceID;// 设备ID

	private String title;
	private String url;
	private String date;
	private String type;

	public static List<Internet> converByInternetBean(List<InternetBean> list, String deviceID) {
		List<Internet> result = new ArrayList<Internet>();
		if (list != null && list.size() > 0) {
			for (InternetBean bean : list) {
				Internet c = new Internet();
				c.setDeviceID(deviceID);
				c.setTitle(bean.getTitle());
				c.setUrl(bean.getUrl());
				c.setDate(bean.getDate());
				c.setType(bean.getType());
				result.add(c);
			}
		}
		return result;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
