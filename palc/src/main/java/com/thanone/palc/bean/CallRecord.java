package com.thanone.palc.bean;

import java.util.ArrayList;
import java.util.List;

import com.zcj.android.util.bean.CallRecordBean;

/**
 * 通话记录
 * 
 * @author zouchongjin@sina.com
 * @data 2015年6月8日
 */
public class CallRecord {

	private Long id;

	private String deviceID;// 设备ID

	private String linkman;// 联系人
	private String number;// 联系号码
	private String callDate;// 呼叫时间
	private String type;// 联系类型
	private String durction;// 通话时长

	public static List<CallRecord> converByCallRecordBean(List<CallRecordBean> callRecordBeanList, String deviceID) {
		List<CallRecord> result = new ArrayList<CallRecord>();
		if (callRecordBeanList != null && callRecordBeanList.size() > 0) {
			for (CallRecordBean bean : callRecordBeanList) {
				CallRecord c = new CallRecord();
				c.setDeviceID(deviceID);
				c.setLinkman(bean.getLinkman());
				c.setNumber(bean.getNumber());
				c.setCallDate(bean.getCallDate());
				c.setType(bean.getType());
				c.setDurction(bean.getDurction());
				result.add(c);
			}
		}
		return result;
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

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getCallDate() {
		return callDate;
	}

	public void setCallDate(String callDate) {
		this.callDate = callDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDurction() {
		return durction;
	}

	public void setDurction(String durction) {
		this.durction = durction;
	}

}
