package com.thanone.palc.bean;

import java.util.ArrayList;
import java.util.List;

import com.zcj.android.util.bean.MessageBean;

/**
 * 短信内容
 * 
 * @author zouchongjin@sina.com
 * @data 2015年6月8日
 */
public class Messages {

	private Long id;

	private String deviceID;// 设备ID

	private String linkman;// 联系人
	private String number;// 联系号码
	private String content;// 短信内容
	private String date;// 短信时间
	private String type;// 短信类型

	public static List<Messages> converByMessageBean(List<MessageBean> messageBeanList, String deviceID) {
		List<Messages> result = new ArrayList<Messages>();
		if (messageBeanList != null && messageBeanList.size() > 0) {
			for (MessageBean bean : messageBeanList) {
				Messages c = new Messages();
				c.setDeviceID(deviceID);
				c.setLinkman(bean.getLinkman());
				c.setNumber(bean.getNumber());
				c.setContent(bean.getContent());
				c.setDate(bean.getDate());
				c.setType(bean.getType());
				result.add(c);
			}
		}
		return result;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getNumber() {
		return number;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
