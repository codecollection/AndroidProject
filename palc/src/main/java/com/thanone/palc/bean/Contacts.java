package com.thanone.palc.bean;

import java.util.ArrayList;
import java.util.List;

import com.zcj.android.util.bean.ContactBean;

/**
 * 通讯录
 * 
 * @author zouchongjin@sina.com
 * @data 2015年6月7日
 */
public class Contacts {

	private Long id;

	private String deviceID;// 设备ID

	private String name;// 名字
	private String tel;// 手机号码
	private String email;// 邮箱

	public static List<Contacts> converByContactsBean(List<ContactBean> contactBeanList, String deviceID) {
		List<Contacts> result = new ArrayList<Contacts>();
		if (contactBeanList != null && contactBeanList.size() > 0) {
			for (ContactBean bean : contactBeanList) {
				Contacts c = new Contacts();
				c.setDeviceID(deviceID);
				c.setName(bean.getName());
				c.setEmail(bean.getEmail());
				c.setTel(bean.getPhone());
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
